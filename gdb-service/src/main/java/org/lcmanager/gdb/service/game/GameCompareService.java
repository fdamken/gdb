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
package org.lcmanager.gdb.service.game;

import org.lcmanager.gdb.service.data.model.ComputerSystem;
import org.lcmanager.gdb.service.data.model.Game;

/**
 * This service is used to compare {@link Game games} to {@link ComputerSystem
 * computer systems}.
 *
 */
public interface GameCompareService {
    /**
     * Compares the given game to the given computer system.
     * 
     * <p>
     * The comparison is OS-family aware. That is only requirements of the same
     * OS family as the OS family of the operating system of the given computer
     * systems are compared.
     * </p>
     *
     * @param game
     *            The game to compare.
     * @param computerSystem
     *            The computer system to compare.
     * @return The result of the comparison.
     */
    GameCompareResult compare(final Game game, final ComputerSystem computerSystem);
}
