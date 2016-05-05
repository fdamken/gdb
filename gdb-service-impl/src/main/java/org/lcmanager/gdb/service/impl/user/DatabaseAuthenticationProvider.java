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

import org.lcmanager.gdb.service.data.model.User;
import org.lcmanager.gdb.service.impl.data.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A custom implementation of {@link AuthenticationProvider} that delegates the
 * requests to the database.
 *
 */
@Service("authProvider")
public class DatabaseAuthenticationProvider implements AuthenticationProvider {
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
     * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
     */
    @Override
    @Transactional(readOnly = true)
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final UsernamePasswordAuthenticationToken auth;
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            if (token.getPrincipal() instanceof String && token.getCredentials() instanceof String) {
                final String userName = (String) token.getPrincipal();
                final String password = (String) token.getCredentials();
                if (this.passwordEncoder.matches(password, this.userMapper.findPasswordByUserName(userName))) {
                    final User user = this.userMapper.findByUserName(userName);
                    auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
                    auth.setDetails(user);
                } else {
                    auth = null;
                }
            } else {
                auth = null;
            }
        } else {
            auth = null;
        }
        return auth;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.springframework.security.authentication.AuthenticationProvider#supports(java.lang.Class)
     */
    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
