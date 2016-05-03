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
import java.util.Locale;

import lombok.Getter;
import lombok.ToString;

import org.lcmanager.gdb.base.Formatable;

/**
 * An OS family contains multiple operating system that are built upon the same
 * technologies (e.g. the <code>WINDOWS</code> family contains
 * <code>Windows XP</code>, <code>Windows 2000</code> and so on).
 * 
 *
 */
@Getter
@ToString
public enum OsFamily implements Formatable {
    /**
     * Windows.
     * 
     */
    WINDOWS(1, "Windows"),
    /**
     * Unix.
     * 
     */
    UNIX(2, "Unix", "Linux"),
    /**
     * Mac OS.
     * 
     */
    MAC(3, "Mac OS"),
    /**
     * Other families that are not explicitly listed.
     * 
     */
    OTHER(4, "Other");

    /**
     * The ID of this operating system family.
     * 
     */
    private final int id;
    /**
     * The name of this operating system family.
     * 
     */
    private final String name;
    /**
     * The aliases of this operating system or other families that are mapped to
     * the same family (e.g. clones of Unix).
     * 
     */
    private final String[] aliases;

    /**
     * Constructor of OsFamily.
     * 
     * @param id
     *            The {@link #id} to set.
     * @param name
     *            The {@link #name} to set.
     * @param aliases
     *            The {@link #aliases} to set.
     */
    private OsFamily(final int id, final String name, final String... aliases) {
        this.id = id;
        this.name = name;
        this.aliases = aliases;
    }

    /**
     * Finds the OS family with the given ID.
     *
     * @param id
     *            The ID of the OS family to find.
     * @return The OS family.
     * @throws IllegalArgumentException
     *             If the OS family was not found.
     */
    public static OsFamily getById(final int id) {
        final OsFamily result = Arrays.stream(OsFamily.values()).filter(value -> value.getId() == id).findFirst().get();
        if (result == null) {
            throw new IllegalArgumentException("Invalid id: " + id + ". Not found!");
        }
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.base.Formatable#format(java.util.Locale)
     */
    @Override
    public String format(final Locale locale) {
        return this.name;
    }
}
