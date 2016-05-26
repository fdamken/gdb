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

import lombok.experimental.UtilityClass;

/**
 * A utility class for math operations.
 *
 */
@UtilityClass
public strictfp class MathUtil {
    /**
     * Normalizes the given percentage value. That is reducing it to
     * <code>100</code> if it is higher and increases it to <code>0</code> if it
     * is lower.
     *
     * @param percentage
     *            The percentage value to normalize.
     * @return The normalized percentage value.-
     */
    public static int normalizePercentage(final int percentage) {
        return percentage < 0 ? 0 : percentage > 100 ? 100 : percentage;
    }

    public static int calulatePercentage(final int basicValue, final int percentageValue) {
        return (int) ((double) percentageValue / (double) basicValue * 100);
    }
}
