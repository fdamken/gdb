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

import java.util.Arrays;
import java.util.function.ToIntFunction;

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

    /**
     * Calculates the percentage of the given basic value and the given
     * percentage value.
     * 
     * <p>
     * As of regular percentage calculation, this executes the following
     * calculation:
     * 
     * <pre>
     * B := Basic Value
     * P := Percentage Value
     * p := Percentage
     * 
     * <math>
     *   <semantics>
     *     <mrow>
     *       <mi>p</mi>
     *       <mi>%</mi>
     *       <mo>=</mo>
     *       <mfrac>
     *         <mrow>
     *           <mi>P</mi>
     *         </mrow>
     *         <mrow>
     *           <mi>B</mi>
     *         </mrow>
     *       </mfrac>
     *     </mrow>
     *   </semantics>
     * </math>
     * <math>
     *   <semantics>
     *     <mrow>
     *       <mi>p</mi>
     *       <mo>=</mo>
     *       <mi>100</mi>
     *       <mo>&InvisibleTimes;</mo>
     *       <mfrac>
     *         <mrow>
     *           <mi>P</mi>
     *         </mrow>
     *         <mrow>
     *           <mi>B</mi>
     *         </mrow>
     *       </mfrac>
     *     </mrow>
     *   </semantics>
     * </math>
     * </pre>
     * </p>
     *
     * @param basicValue
     *            The basic value.
     * @param percentageValue
     *            The percentage value.
     * @return The percentage.
     */
    public static int calulatePercentage(final int basicValue, final int percentageValue) {
        return (int) ((double) percentageValue / (double) basicValue * 100);
    }

    /**
     * Calculates the average of the given values.
     *
     * @param values
     *            The values to calculate the average for.
     * @return The average of the given values.
     */
    public static int average(final int... values) {
        return Arrays.stream(values).sum() / values.length;
    }

    /**
     * Calculates the average of the given values by applying the given to-int
     * function on them.
     *
     * @param numberFunction
     *            The to-int function to apply on each value.
     * @param values
     *            The values to calculate the average for.
     * @return The average of the given values.
     */
    @SafeVarargs
    public static <T> int average(final ToIntFunction<T> numberFunction, final T... values) {
        return Arrays.stream(values).mapToInt(numberFunction).sum() / values.length;
    }
}
