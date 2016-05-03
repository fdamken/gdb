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
package org.lcmanager.gdb.service.data.util;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The requirement type represents the type of a requirement (e.g. minimum).
 *
 */
@RequiredArgsConstructor
@Getter
public enum RequirementType {
    /**
     * Marks a requirement as minimum.
     * 
     */
    MINIMUM(1),
    /**
     * Marks a requirement as recommended.
     * 
     */
    RECOMMENDED(2);

    /**
     * The ID of this requirement type.
     * 
     */
    private final int id;

    /**
     * Finds the requirement type with the given ID.
     *
     * @param id
     *            The ID to search for.
     * @return The requirement type.
     * @throws IllegalArgumentException
     *             If the requirement type ID was not found.
     */
    public static RequirementType getById(final int id) {
        final RequirementType result = Arrays.stream(RequirementType.values()).filter(value -> value.getId() == id).findFirst()
                .get();
        if (result == null) {
            throw new IllegalArgumentException("Invalid requirement type! ID " + id + " does not exist!");
        }
        return result;
    }
}
