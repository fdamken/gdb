/*
 * #%L
 * Game Database
 * %%
 * Copyright (C) 2016 - 2016 LCManager Group
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.lcmanager.gdb.service.impl.user;

import java.util.List;

import org.lcmanager.gdb.service.data.model.User;
import org.lcmanager.gdb.service.impl.data.mapper.UserMapper;
import org.lcmanager.gdb.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link UserService}.
 *
 */
@Service("userDetailsManager")
@CacheConfig(cacheNames = UserServiceImpl.CACHE)
public class UserServiceImpl implements UserService {
    /**
     * The name of the user cache.
     * 
     */
    protected static final String CACHE = "user-service";

    /**
     * The {@link AuthenticationManager}.
     * 
     */
    @Autowired
    private AuthenticationManager authenticationManager;
    /**
     * The user {@link Cache cache}.
     * 
     */
    @Value("#{ cacheManager.getCache(\"" + UserServiceImpl.CACHE + "\") }")
    private Cache cache;
    /**
     * The {@link PasswordEncoder}.
     * 
     */
    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * The {@link UserMapper}.
     * 
     */
    @Autowired
    private UserMapper userMapper;

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.user.UserService#loadUserByUsername(java.lang.String)
     */
    @Override
    @PreAuthorize("(authentication.name == #userName) || hasRole('ADMIN')")
    @Transactional(readOnly = true)
    @Cacheable
    public User loadUserByUsername(final String userName) throws UsernameNotFoundException {
        int id = -1;
        try {
            id = Integer.parseInt(userName);
        } catch (final NumberFormatException dummy) {
            // The userName was to user ID.
        }

        final User user;
        if (id > 0) {
            user = this.userMapper.findById(id);
        } else {
            user = this.userMapper.findByUserName(userName);
        }

        if (user == null) {
            if (id > 0) {
                throw new UsernameNotFoundException("No user was found with the ID " + id + "!");
            }
            throw new UsernameNotFoundException("No user was found with the name '" + userName + "'!");
        }

        return user;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.user.UserService#updateUser(org.springframework.security.core.userdetails.UserDetails)
     */
    @Override
    @PreAuthorize("(authentication.name == #userDetails.username) || hasRole('ADMIN')")
    @Transactional
    @CacheEvict(key = "#userDetails.username")
    public void updateUser(final UserDetails userDetails) {
        this.userMapper.update(User.makeUser(userDetails));
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.user.UserService#deleteUser(java.lang.String)
     */
    @Override
    @PreAuthorize("(authentication.name == #userName) || hasRole('ADMIN')")
    @Transactional
    @CacheEvict
    public void deleteUser(final String userName) {
        this.userMapper.deleteUserName(userName);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.user.UserService#changePassword(java.lang.String,
     *      java.lang.String)
     */
    @Override
    @Transactional
    public void changePassword(final String oldPassword, final String newPassword) {
        final User user = this.retrieveUser();
        if (user == null) {
            throw new AccessDeniedException("Unable to retrieve current authentication! Is the user authenticated?");
        }

        final String userName = user.getUsername();

        if (!this.checkCredentials(userName, oldPassword)) {
            throw new AccessDeniedException("The old password is not correct!");
        }

        this.userMapper.updatePassword(userName, this.passwordEncoder.encode(newPassword));

        this.cache.evict(userName);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.user.UserService#userExists(java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable
    public boolean userExists(final String userName) {
        return this.userMapper.existsUserName(userName);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.user.UserService#createUser(org.lcmanager.gdb.service.data.model.User,
     *      java.lang.String)
     */
    @Override
    public void createUser(final User user, final String password) {
        this.userMapper.insert(user, this.passwordEncoder.encode(password));
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.user.UserService#retrieveUsers()
     */
    @Override
    public List<User> retrieveUsers() {
        return this.userMapper.find();
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.user.UserService#auth(org.springframework.security.core.Authentication)
     */
    @Transactional(readOnly = true)
    @Override
    public Authentication auth(final Authentication token) throws AuthenticationException {
        return this.authenticationManager.authenticate(token);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.user.UserService#assignAuthority(org.springframework.security.core.GrantedAuthority,
     *      org.lcmanager.gdb.service.data.model.User)
     */
    @Override
    @Transactional
    @CacheEvict(key = "#user.username")
    public void assignAuthority(final GrantedAuthority authority, final User user) {
        this.userMapper.addAuthority(user.getId(), authority.getAuthority());
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.user.UserService#evictAuthority(org.springframework.security.core.GrantedAuthority,
     *      org.lcmanager.gdb.service.data.model.User)
     */
    @Override
    @Transactional
    @CacheEvict(key = "#user.username")
    public void evictAuthority(final GrantedAuthority authority, final User user) {
        this.userMapper.removeAuthority(user.getId(), authority.getAuthority());
    }
}
