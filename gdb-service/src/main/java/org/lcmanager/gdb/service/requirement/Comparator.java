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
package org.lcmanager.gdb.service.requirement;

import org.lcmanager.gdb.service.data.model.ComputerSystem;
import org.lcmanager.gdb.service.data.model.Requirement;

/**
 * A comparator can compare {@link Requirement requirements} with
 * {@link ComputerSystem computer systems} and produces a {@link CompareResult}
 * that contains the result data.
 *
 * @param <T>
 *            The type of the {@link CompareResult} that is produced.
 */
public interface Comparator<T extends CompareResult> {
    /**
     * Compares the given {@link Requirement requirement} with the given
     * {@link ComputerSystem computer system}.
     * 
     * <p>
     * <b> NOTE: This may not compare all properties of a requirement! Please
     * lookup the resulting {@link CompareResult} type for the actual compared
     * properties or read the documentation of the implementing class. </b>
     * </p>
     * 
     * @param requirement
     *            The {@link Requirement requirement} to compare.
     * @param computerSystem
     *            The {@link ComputerSystem computer system} to compare.
     * @return The result of the comparison as a {@link CompareResult}.
     */
    T compare(final Requirement requirement, final ComputerSystem computerSystem);
}
