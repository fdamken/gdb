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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;

/**
 * A utility class for working with numbers.
 *
 */
@UtilityClass
public class NumberUtil {
    /**
     * A regular expression to extract a number from a string. The number group
     * is named <code>num</code>.
     * 
     */
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^.*?(?<num>\\d+).*$");
    /**
     * A regular expression to extract a floating point number from a string.
     * The group for the integer part is named <code>num</code> and the group
     * for the decimal part is named <code>numdec</code>.
     * 
     */
    private static final Pattern NUMBER_PATTERN_FLOAT = Pattern.compile("^.*?(?<num>\\d+)([,\\.](?<numdec>\\d+))?.*$");

    /**
     * Extracts the first valid number from the given string.
     *
     * @param str
     *            The string to extract the number from.
     * @return The extracted number.
     */
    public static int extractNumber(final String str) {
        final Matcher matcher = NumberUtil.NUMBER_PATTERN.matcher(str);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Str does not match the regular expression " + NumberUtil.NUMBER_PATTERN.pattern() + "!");
        }
        return Integer.parseInt(matcher.group("num"));
    }

    /**
     * Extracts the first valid floating point number from the given string. The
     * integer part can be separated from the decimal part with a dot (
     * <code>.</code>) or a comma (<code>,</code>).
     *
     * @param str
     *            The string to extract the floating point number from.
     * @return The extracted number.
     */
    public static double extractNumberFloat(final String str) {
        final Matcher matcher = NumberUtil.NUMBER_PATTERN_FLOAT.matcher(str);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Str does not match the regular expression " + NumberUtil.NUMBER_PATTERN.pattern() + "!");
        }

        final String groupNum = matcher.group("num");
        final String groupNumDec = matcher.group("numdec");
        final double result;
        if (groupNumDec == null) {
            result = Integer.parseInt(groupNum);
        } else {
            result = Double.parseDouble(groupNum + "." + groupNumDec);
        }
        return result;
    }
}
