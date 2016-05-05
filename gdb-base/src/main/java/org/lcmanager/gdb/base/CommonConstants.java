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
package org.lcmanager.gdb.base;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Defines a lot of common constants that may be used in all modules.
 *
 */
public interface CommonConstants {
    /**
     * Whether GDB is running in debug mode or not.
     * 
     */
    boolean DEV = Boolean.parseBoolean(System.getProperty("dev"));

    /**
     * Defines all available roles (authorities).
     *
     */
    interface Role {
        /**
         * {@value #USER_ROLE}
         * 
         */
        String USER_ROLE = "ROLE_USER";
        /**
         * {@value #ADMIN_ROLE}
         * 
         */
        String ADMIN_ROLE = "ROLE_ADMIN";

        /**
         * The authority for {@value #USER_ROLE}.
         * 
         */
        GrantedAuthority USER_AUTHORITY = new SimpleGrantedAuthority(CommonConstants.Role.USER_ROLE);
        /**
         * The authority for {@value #ADMIN_ROLE}.
         * 
         */
        GrantedAuthority ADMIN_AUTHORITY = new SimpleGrantedAuthority(CommonConstants.Role.ADMIN_ROLE);
    }
}
