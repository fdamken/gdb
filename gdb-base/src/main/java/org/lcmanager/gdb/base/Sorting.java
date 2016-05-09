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

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Represents a sorting containing the direction and the term (or column) to
 * sort by.
 *
 */
@Value
public class Sorting {
    /**
     * The sorting direction.
     * 
     */
    @NonNull
    Direction direction;
    /**
     * The sorting term.
     * 
     */
    @NonNull
    String term;

    /**
     * Represents a sorting direction.
     *
     */
    @Getter
    @RequiredArgsConstructor
    public static enum Direction {
        /**
         * Ascending sorting.
         * 
         */
        ASCENDING("ASC"),
        /**
         * Descending sorting.
         * 
         */
        DESCENDING("DESC");

        /**
         * The abbreviation for this sorting direction.
         * 
         */
        private final String abbreviation;

        /**
         * Finds the direction thats name or abbreviation matches the given name
         * or abbreviation (case-insensitive).
         *
         * @param name
         *            The name or abbreviation to get the direction for.
         * @return The direction, if any.
         * @throws IllegalArgumentException
         *             If no direction was found for the given name or
         *             abbreviation.
         */
        public static Direction get(final String name) {
            final Direction result = Arrays.stream(Direction.values()).filter(
                    direction -> direction.name().equalsIgnoreCase(name) || direction.getAbbreviation().equalsIgnoreCase(name))
                    .findAny().orElse(null);
            if (result == null) {
                throw new IllegalArgumentException("Unsupported sorting direction name or abbreviation: " + name);
            }
            return result;
        }
    }
}
