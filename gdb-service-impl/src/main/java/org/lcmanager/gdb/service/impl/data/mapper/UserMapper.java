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
package org.lcmanager.gdb.service.impl.data.mapper;

import org.apache.ibatis.annotations.Param;
import org.lcmanager.gdb.service.data.model.User;

/**
 * Mapper for {@link User}.
 *
 */
public interface UserMapper extends BaseMapper<User, Integer> {
    /**
     * {@inheritDoc}
     *
     * @deprecated This operation is not supported! Use
     *             {@link #insert(User, String)} instead!
     * @see org.lcmanager.gdb.service.impl.data.mapper.BaseMapper#insert(org.lcmanager.gdb.service.data.model.BaseModel)
     * @see org.lcmanager.gdb.service.impl.data.mapper.UserMapper#insert(org.lcmanager.gdb.service.data.model.User,
     *      java.lang.String)
     */
    @Override
    @Deprecated
    default int insert(final User user) {
        throw new UnsupportedOperationException("Use org.lcmanager.gdb.service.impl.data.mapper.UserMapper"
                + "#insert(org.lcmanager.gdb.service.data.model.User, java.lang.String) instead!");
    }

    /**
     * Inserts the given user and sets its password to the given (encoded!)
     * password.
     * 
     * <p>
     * If the user was already created, an error occurs.
     * </p>
     *
     * @param user
     *            The user to insert.
     * @param password
     *            The password to set. This <b>does not</b> encode the password!
     * @return The count of the inserted rows.
     */
    int insert(@Param("user") final User user, @Param("password") final String password);

    /**
     * Updates the password of the given user.
     * 
     * <p>
     * If the user does not exist, nothing happens.
     * </p>
     *
     * @param userName
     *            The name of the user to update the password for.
     * @param password
     *            The password to set.
     * @return The count of the updated rows.
     */
    int updatePassword(@Param("userName") String userName, @Param("password") String password);

    /**
     * Deletes the user with the given name.
     * 
     * <p>
     * If the user does not exist, nothing happens.
     * </p>
     *
     * @param userName
     *            The name of the user to delete.
     * @return The count of the deleted rows.
     */
    int deleteUserName(@Param("userName") String userName);

    /**
     * Associates the given authority with the given user ID.
     * 
     * <p>
     * If no user with the given ID exists, an error occurs.
     * </p>
     * <p>
     * If the association already exists, nothing happens.
     * </p>
     *
     * @param id
     *            The ID of the user to associate.
     * @param authority
     *            The authority to associate.
     */
    void addAuthority(@Param("id") int id, @Param("authority") String authority);

    /**
     * Removes the association of the given authority with the given user ID.
     *
     * <p>
     * If no association between the given ID and the given authority exists,
     * nothing happens.
     * </p>
     *
     * @param id
     *            The ID of the user that is associated with the given
     *            authority.
     * @param authority
     *            The associated authority.
     */
    void removeAuthority(@Param("id") int id, @Param("authority") String authority);

    /**
     * Checks whether any user with the given user name exists.
     *
     * @param userName
     *            The user name to check.
     * @return Whether a user with the given user name exists or not.
     */
    boolean existsUserName(@Param("userName") String userName);

    /**
     * Finds the user with the given user name.
     *
     * @param userName
     *            The name of the user to find.
     * @return The user, if any. Otherwise <code>null</code>.
     */
    User findByUserName(@Param("userName") String userName);

    /**
     * Finds the password of the given user name.
     *
     * @param userName
     *            The name of the user to find the password of.
     * @return The password, if any. Otherwise <code>null</code>.
     */
    String findPasswordByUserName(@Param("userName") String userName);
}
