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
package org.lcmanager.gdb.service.user;

import java.util.List;

import org.lcmanager.gdb.base.StreamUtil;
import org.lcmanager.gdb.service.data.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * The user service is used to all interaction with Spring Security and the user
 * management.
 *
 */
public interface UserService extends UserDetailsManager {
    /**
     * {@inheritDoc}
     *
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    User loadUserByUsername(String userName) throws UsernameNotFoundException;

    /**
     * {@inheritDoc}
     * 
     * <p>
     * <b> NOTE: This operation is not supported! Use
     * {@link #createUser(User, String)} instead! </b>
     * </p>
     *
     * @deprecated This operation is not supported! Use
     *             {@link #createUser(User, String)} instead!
     * @see org.springframework.security.provisioning.UserDetailsManager#createUser(org.springframework.security.core.userdetails.UserDetails)
     * @see org.lcmanager.gdb.service.user.UserService#createUser(org.lcmanager.gdb.service.data.model.User,
     *      java.lang.String)
     */
    @Deprecated
    @Override
    default void createUser(final UserDetails user) {
        throw new UnsupportedOperationException("Use org.lcmanager.gdb.service.user.UserService"
                + "#createUser(org.lcmanager.gdb.service.data.model.User, java.lang.String) instead!");
    }

    /**
     * {@inheritDoc}
     *
     * @see org.springframework.security.provisioning.UserDetailsManager#updateUser(org.springframework.security.core.userdetails.UserDetails)
     */
    @Override
    void updateUser(UserDetails user);

    /**
     * {@inheritDoc}
     *
     * @see org.springframework.security.provisioning.UserDetailsManager#deleteUser(java.lang.String)
     */
    @Override
    default void deleteUser(final String userName) {
        this.deleteUser(this.retrieveUser(userName).getId());
    }

    /**
     * {@inheritDoc}
     *
     * @see org.springframework.security.provisioning.UserDetailsManager#changePassword(java.lang.String,
     *      java.lang.String)
     */
    @Override
    void changePassword(String oldPassword, String newPassword);

    /**
     * Check if a user with the supplied login name exists in the system.
     */
    @Override
    boolean userExists(String username);

    /**
     * Creates the given user with the given password.
     *
     * @param user
     *            The user to create.
     * @param password
     *            The password of the user to create.
     */
    void createUser(final User user, final String password);

    /**
     * Deletes the given user.
     *
     * @param user
     *            The user to delete.
     */
    void deleteUser(final User user);

    /**
     * Deletes the user with the given ID.
     *
     * @param id
     *            The ID of the user to delete.
     */
    default void deleteUser(final int id) {
        this.deleteUser(this.retrieveUser(id));
    }

    // ~ Read Only ~
    /**
     * Retrieves all user.
     *
     * @return ALl users.
     */
    List<User> retrieveUsers();

    /**
     * Retrieves the user with the given user name.
     *
     * <p>
     * <b> NOTE: If the given user name is an integer it is treated as a user
     * ID! </b>
     * </p>
     *
     * @param userName
     *            The user name to find.
     * @return The user, if any. Otherwise <code>null</code>.
     */
    default User retrieveUser(final String userName) {
        User user = null;
        if (userName == null || userName.isEmpty()) {
            final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && !auth.getName().equals("anonymousUser") && auth.isAuthenticated()) {
                if (auth.getDetails() instanceof UserDetails) {
                    user = User.makeUser((UserDetails) auth.getDetails());
                } else {
                    throw new IllegalStateException("Illegal details type: " + auth.getDetails().getClass());
                }
            }
        } else {
            try {
                user = this.loadUserByUsername(userName);
            } catch (final UsernameNotFoundException dummy) {
                // User not found --> Return null.
            }
        }
        return user;
    }

    /**
     * Retrieves the user with the given ID.
     *
     * @param id
     *            The ID to search for.
     * @return The user, if any. Otherwise <code>null</code>.
     */
    default User retrieveUser(final int id) {
        return this.retrieveUser(String.valueOf(id));
    }

    /**
     * Retrieves the current user.
     *
     * @return The user, if any. Otherwise <code>null</code>.
     */
    default User retrieveUser() {
        return this.retrieveUser(null);
    }

    /**
     * Checks whether any user is authenticated.
     *
     * @return Whether any user is authenticated.
     */
    default boolean isAuthenticated() {
        return this.retrieveUser() != null;
    }

    /**
     * Checks the credentials of the given user.
     *
     * @param userName
     *            The user to check the password for.
     * @param password
     *            The password to check.
     * @return Whether the credentials are correct.
     */
    default boolean checkCredentials(final String userName, final String password) {
        return this.authenticate(userName, password, true) != null;
    }

    /**
     * Checks whether the given user has the given authority (role).
     * 
     * <p>
     * The available authorities are listed in
     * {@link org.lcmanager.gdb.base.CommonConstants.Role}.
     * </p>
     *
     * @param authority
     *            The authority to check.
     * @param user
     *            The user to check.
     * @return Whether the given user has the given authority. If the given user
     *         is <code>null</code>, <code>false</code>.
     */
    default boolean hasAuthority(final String authority, final User user) {
        if (user == null) {
            return false;
        }
        return !StreamUtil.isEmpty(user.getAuthorities().stream().parallel()
                .filter(grantedAuthority -> grantedAuthority.getAuthority().equalsIgnoreCase(authority)));
    }

    /**
     * Checks whether the current user has the given authority.
     *
     * <p>
     * The available authorities are listed in
     * {@link org.lcmanager.gdb.base.CommonConstants.Role}.
     * </p>
     * 
     * @param authority
     *            The authority to check.
     * @return Whether the current user has the given authority.
     */
    default boolean hasAuthority(final String authority) {
        return this.hasAuthority(authority, this.retrieveUser());
    }

    // ~ Read and Write ~
    /**
     * Authenticates the given authentication token but <b>does not</b> log the
     * user if.
     *
     * @param token
     *            The token to authenticate.
     * @return The authenticated authentication.
     * @throws AuthenticationException
     *             If the authentication process failed (e.g. due to wrong
     *             credentials).
     */
    Authentication auth(Authentication token) throws AuthenticationException;

    /**
     * Authenticated the given credentials.
     *
     * @param userName
     *            The user name of the credentials.
     * @param password
     *            The password.
     * @param checkOnly
     *            Whether to check the credentials only. If <code>false</code>
     *            the user gets logged in if the authentication succeeds.
     * @return The authenticated user if the authentication succedeed. Otherwise
     *         <code>null</code>.
     */
    default User authenticate(final String userName, final String password, final boolean checkOnly) {
        User user = null;
        try {
            final Authentication auth = this.auth(new UsernamePasswordAuthenticationToken(userName, password));
            if (!checkOnly) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            user = (User) auth.getDetails();
        } catch (final AuthenticationException dummy) {
            // Authentication failed --> Return null.
        }
        return user;
    }

    /**
     * Authenticates the given credentials and logs the user in if the
     * authentication succeeds.
     *
     * @param userName
     *            The user name of the credentials.
     * @param password
     *            The password.
     * @return The authenticated user if the authentication succeeded. Otherwise
     *         <code>null</code>.
     */
    default User authenticate(final String userName, final String password) {
        return this.authenticate(userName, password, false);
    }

    /**
     * Assigns the given authority to the given user.
     * 
     * <p>
     * The available authorities are listed in
     * {@link org.lcmanager.gdb.base.CommonConstants.Role}.
     * </p>
     *
     * @param authority
     *            The authority to assign
     * @param user
     *            The user to assign the authority to.
     */
    void assignAuthority(final GrantedAuthority authority, final User user);

    /**
     * Assigns the given authority to the given user.
     * 
     * <p>
     * The available authorities are listed in
     * {@link org.lcmanager.gdb.base.CommonConstants.Role}.
     * </p>
     *
     * @param authority
     *            The authority to assign.
     * @param user
     *            The user to assign the authority to.
     */
    default void assignAuthority(final String authority, final User user) {
        this.assignAuthority(new SimpleGrantedAuthority(authority), user);
    }

    /**
     * Assigns the given authority to the current user.
     * 
     * <p>
     * The available authorities are listed in
     * {@link org.lcmanager.gdb.base.CommonConstants.Role}.
     * </p>
     *
     * @param authority
     *            The authority to assign.
     */
    default void assignAuthority(final GrantedAuthority authority) {
        this.assignAuthority(authority, this.retrieveUser());
    }

    /**
     * Assigns the given authority to the current user.
     * 
     * <p>
     * The available authorities are listed in
     * {@link org.lcmanager.gdb.base.CommonConstants.Role}.
     * </p>
     *
     * @param authority
     *            The authority to assign.
     */
    default void assignAuthority(final String authority) {
        this.assignAuthority(new SimpleGrantedAuthority(authority));
    }

    /**
     * Evicts the given authority from the given user.
     * 
     * <p>
     * The available authorities are listed in
     * {@link org.lcmanager.gdb.base.CommonConstants.Role}.
     * </p>
     *
     * @param authority
     *            The authority to evict.
     * @param user
     *            The user to evict the authority from.
     */
    void evictAuthority(final GrantedAuthority authority, final User user);

    /**
     * Evicts the given authority from the given user.
     * 
     * <p>
     * The available authorities are listed in
     * {@link org.lcmanager.gdb.base.CommonConstants.Role}.
     * </p>
     *
     * @param authority
     *            The authority to evict.
     * @param user
     *            The user to evict the authority from.
     */
    default void evictAuthority(final String authority, final User user) {
        this.evictAuthority(new SimpleGrantedAuthority(authority), user);
    }

    /**
     * Evicts the given authority from the current user.
     * 
     * <p>
     * The available authorities are listed in
     * {@link org.lcmanager.gdb.base.CommonConstants.Role}.
     * </p>
     *
     * @param authority
     *            The authority to evict.
     */
    default void evictAuthority(final GrantedAuthority authority) {
        this.evictAuthority(authority, this.retrieveUser());
    }

    /**
     * Evicts the given authority from the current user.
     * 
     * <p>
     * The available authorities are listed in
     * {@link org.lcmanager.gdb.base.CommonConstants.Role}.
     * </p>
     *
     * @param authority
     *            The authority to evict.
     */
    default void evictAuthority(final String authority) {
        this.evictAuthority(new SimpleGrantedAuthority(authority));
    }

    /**
     * Evicts all authorities from the given user.
     *
     * @param user
     *            The user to evict all authorities from.
     */
    void evictAllAuthorities(final User user);

    /**
     * Evicts all authorities from the current user.
     *
     */
    default void evictAllAuthorities() {
        this.evictAllAuthorities(this.retrieveUser());
    }
}
