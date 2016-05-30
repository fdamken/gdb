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
package org.lcmanager.gdb.service.compare;

import org.lcmanager.gdb.base.MathUtil;
import org.lcmanager.gdb.service.data.model.ComputerSystem;
import org.lcmanager.gdb.service.data.model.Requirement;

/**
 * A compare result is the result of a comparison executed by a
 * {@link Comparator comparator}.
 *
 */
public interface CompareResult {
    /**
     * Calculates the percentage score the compared {@link ComputerSystem
     * computer system} matches the {@link Requirement requirements}.
     * 
     * <p>
     * If the compare result contains multiple properties, the returned value is
     * a summary of these.
     * </p>
     *
     * @return The percentage score.
     */
    int getPercentage();

    /**
     *
     * @return The normalized result of {@link #getPercentage()}.
     * @see org.lcmanager.gdb.base.MathUtil#normalizePercentage(int)
     */
    default int getNormalizedPercentage() {
        return MathUtil.normalizePercentage(this.getPercentage());
    }

    /**
     *
     * @return Whether the requirements have been satisfied or not. That is if
     *         {@link #getPercentage()} returns a value equal to or higher than
     *         <code>100</code>.
     */
    default boolean isSatisfied() {
        return this.getPercentage() >= 100;
    }
}
