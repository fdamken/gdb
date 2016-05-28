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
package org.lcmanager.gdb.service.data.model;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Provides an expendable user class to the basic {@link UserDetails} of Spring.
 * It represents a user within the GDB.
 *
 */
@Data
@Accessors(chain = true)
public class User implements BaseModel<Integer>, UserDetails {
    /**
     * The serial version UID.
     *
     */
    private static final long serialVersionUID = -9194527890649151000L;

    // ~ Direct ~
    /**
     * The ID of this user.
     * 
     */
    private Integer id;
    /**
     * The name of this user. Must be unique.
     * 
     */
    private String username;
    /**
     * The display name of this user.
     * 
     */
    private String displayName;
    /**
     * Whether this user is enabled. Defaults to <code>true</code>.
     * 
     */
    private boolean enabled = true;

    // ~ Mapped ~
    /**
     * The {@link GrantedAuthority authorities} this user is mapped to.
     * 
     */
    @Getter(onMethod = @__({ @JsonIgnore }))
    private Collection<? extends GrantedAuthority> authorities;

    // ~ Static fields for Spring ~
    /**
     * The password. Always <code>null</code>.
     * 
     */
    // The password must never, every be saved into a User object as it may be
    // populated via a REST API.
    private final transient String password = null;
    /**
     * Whether the account is not expired. Always <code>true</code>.
     * 
     */
    private final transient boolean accountNonExpired = true;
    /**
     * Whether the account is not locker. Always <code>true</code>.
     * 
     */
    private final transient boolean accountNonLocked = true;
    /**
     * Whether the credentials are not expired. Always <code>true</code>.
     * 
     */
    private final transient boolean credentialsNonExpired = true;

    /**
     * Creates a user by casting the given user details if the type is correct
     * or by copying the basic data.
     *
     * @param userDetails
     *            The user details to create the user from.
     * @return The created user.
     */
    public static User makeUser(final UserDetails userDetails) {
        final User user;
        if (userDetails instanceof User) {
            user = (User) userDetails;
        } else {
            user = new User();
            user.setAuthorities(userDetails.getAuthorities());
            user.setEnabled(userDetails.isEnabled());
            user.setUsername(userDetails.getUsername());
        }
        return user;
    }

    /**
     * Maps {@link #getAuthorities()} to a list of strings of the authority
     * names.
     *
     * @return {@link #getAuthorities()} as a list of strings of the authority
     *         names.
     */
    @JsonProperty("authorities")
    public List<String> getAuthorityList() {
        return this.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    /**
     * Sets {@link #setAuthorities(Collection)} by mapping the list of string to
     * a list of authorities.
     *
     * @param authorityList
     *            The list of the names of authorities to set.
     */
    @JsonSetter("authorities")
    public void setAuthorityList(final List<String> authorityList) {
        this.setAuthorities(authorityList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }
}
