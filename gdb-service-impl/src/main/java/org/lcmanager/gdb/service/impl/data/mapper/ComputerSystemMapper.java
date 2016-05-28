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

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.lcmanager.gdb.service.data.model.ComputerSystem;

/**
 * Mapper for {@link ComputerSystem}.
 *
 */
public interface ComputerSystemMapper extends BaseMapper<ComputerSystem, Integer> {
    /**
     * Deletes all computer systems owned by the user with the given ID.
     *
     * @param userId
     *            The ID of the user that owns all computer systems to delete.
     */
    void deleteByUser(@Param("userId") int userId);

    /**
     * Finds the primary computer system of the given user.
     *
     * @param userId
     *            The user to find the primary computer system for.
     * @return The primary computer system. If the user has to computer systems
     *         configured, <code>null</code>.
     */
    ComputerSystem findPrimaryByUser(@Param("userId") int userId);

    /**
     * Finds all computer system of the given user starting with the primary
     * ones.
     *
     * @param userId
     *            The user to find all computer systems of.
     * @return All found computer systems.
     */
    List<ComputerSystem> findByUser(@Param("userId") int userId);
}
