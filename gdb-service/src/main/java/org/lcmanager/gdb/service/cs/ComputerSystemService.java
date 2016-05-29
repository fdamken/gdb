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
package org.lcmanager.gdb.service.cs;

import java.util.SortedSet;

import org.lcmanager.gdb.base.health.HealthRelevant;
import org.lcmanager.gdb.service.data.model.ComputerSystem;
import org.lcmanager.gdb.service.data.model.User;

/**
 * This service is used to manage the computer systems that a user can
 * configure.
 *
 */
public interface ComputerSystemService extends HealthRelevant {
    /**
     * Adds the given computer system to the given user.
     *
     * @param user
     *            The user to add the computer system to.
     * @param computerSystem
     *            The computer system to add.
     */
    void addComputerSystem(final User user, final ComputerSystem computerSystem);

    /**
     * Deletes the computer system with the given ID from the given user.
     *
     * @param user
     *            The user to delete the computer system from.
     * @param computerSystemId
     *            The IF of the computer system to delete.
     */
    void deleteComputerSystem(final User user, final int computerSystemId);

    /**
     * Updates the given computer system of the given user by re-setting all
     * properties.
     *
     * @param user
     *            The user to update the computer system from-
     * @param computerSystem
     *            The computer system to update including the new properties.
     */
    void updateComputerSystem(final User user, final ComputerSystem computerSystem);

    /**
     * Retrieves the primary computer system of the given user.
     *
     * @param user
     *            The user to retrieve the primary computer system for.
     * @return The primary computer system of the given user, if any.
     */
    ComputerSystem retrievePrimaryComputerSystem(final User user);

    /**
     * Retrieves all computer systems of the given user.
     *
     * @param user
     *            The user to retrieve all computer systems for.
     * @return All computer systems of the given user.
     */
    SortedSet<ComputerSystem> retrieveComputerSystems(final User user);

    /**
     * Checks whether the given user owns the computer system with the given ID.
     *
     * @param user
     *            The user to check the ownership for.
     * @param computerSystemId
     *            The ID of the computer system to check the ownership for.
     * @return Whether the given user owns the given computer system or not. If
     *         the given computer systems does not exist, <code>true</code>.
     */
    boolean isOwnedBy(final User user, final int computerSystemId);

    /**
     * Checks whether the given user owns the computer system with the given ID.
     * 
     * <p>
     * If the given user does not own the computer system, an
     * {@link SecurityException} is thrown. Otherwise, nothing happens.
     * </p>
     *
     * @param user
     *            The user to check the ownership for.
     * @param computerSystemId
     *            The ID of the computer system to check the ownership for.
     * @throws SecurityException
     *             If the given user does not own the given computer system.
     */
    default void checkOwnership(final User user, final int computerSystemId) throws SecurityException {
        if (!this.isOwnedBy(user, computerSystemId)) {
            throw new SecurityException("The given user does not own the computer system with the given ID!");
        }
    }

    /**
     * Checks whether the given user owns the given computer system.
     * 
     * <p>
     * If the given user does not own the computer system, an
     * {@link SecurityException} is thrown. Otherwise, nothing happens.
     * </p>
     *
     * @param user
     *            The user to check the ownership for.
     * @param computerSystem
     *            The computer system to check the ownership for. If
     *            <code>null</code>, nothing happens.
     * @throws SecurityException
     *             If the given user does not own the given computer system.
     */
    default void checkOwnership(final User user, final ComputerSystem computerSystem) throws SecurityException {
        if (computerSystem != null) {
            this.checkOwnership(user, computerSystem.getId());
        }
    }
}
