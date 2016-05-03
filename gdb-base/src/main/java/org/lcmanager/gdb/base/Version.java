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

import lombok.Value;

/**
 * Represents a version of a software in the form
 * <code>&lt;major&gt;.&lt;minor&gt;.&lt;bugfix&gt;.&lt;build&gt;</code> (e.g.
 * <code>1.0.0.0</code>).
 *
 */
@Value
public class Version {
    /**
     * The code name of this version.
     * 
     */
    String codename;

    /**
     * The major version.
     * 
     */
    int major;
    /**
     * The minor version.
     * 
     * <p>
     * If <code>&lt;0</code>, treated as not set.
     * </p>
     * 
     */
    int minor;
    /**
     * The bugfix version.
     * 
     * <p>
     * If <code>&lt;0</code>, treated as not set.
     * </p>
     * 
     */
    int bugfx;
    /**
     * The build number.
     * 
     * <p>
     * If <code>&lt;0</code>, treated as not set.
     * </p>
     * 
     */
    int build;

    /**
     * Constructor of Version.
     *
     * @param codename
     *            The code name.
     * @param major
     *            The major version.
     * @param minor
     *            The minor version.
     * @param bugfx
     *            The bugfix version.
     * @param build
     *            The build number.
     */
    public Version(final String codename, final int major, final int minor, final int bugfx, final int build) {
        this.codename = codename;
        this.major = major;
        this.minor = minor < 0 ? -1 : minor;
        this.bugfx = minor < 0 || bugfx < 0 ? -1 : bugfx;
        this.build = minor < 0 || bugfx < 0 || build < 0 ? -1 : bugfx;
    }

    /**
     * Constructor of Version.
     * 
     * <p>
     * <b> NOTE: This makes MyBatis happy. </b>
     * </p>
     * 
     * @param codename
     *            The code name.
     * @param major
     *            The major version. If <code>null</code>, treated as
     *            <code>-1</code>.
     * @param minor
     *            The minor version. If <code>null</code>, treated as
     *            <code>-1</code>.
     * @param bugfx
     *            The bugfix version. If <code>null</code>, treated as
     *            <code>-1</code>.
     * @param build
     *            The build number. If <code>null</code>, treated as
     *            <code>-1</code>.
     */
    public Version(final String codename, final Integer major, final Integer minor, final Integer bugfx, final Integer build) {
        this(codename, major == null ? -1 : major, minor == null ? -1 : minor, bugfx == null ? -1 : bugfx, build == null ? -1
                : build);
    }

    /**
     * Constructor of Version.
     *
     * @param codename
     *            The code name.
     * @param major
     *            The major version.
     * @param minor
     *            The minor version.
     * @param bugfx
     *            The bugfix version.
     */
    public Version(final String codename, final int major, final int minor, final int bugfx) {
        this(codename, major, minor, bugfx, 0);
    }

    /**
     * Constructor of Version.
     *
     * @param codename
     *            The code name.
     * @param major
     *            The major version.
     * @param minor
     *            The minor version.
     */
    public Version(final String codename, final int major, final int minor) {
        this(codename, major, minor, 0);
    }

    /**
     * Constructor of Version.
     *
     * @param codename
     *            The code name.
     * @param major
     *            The major version.
     */
    public Version(final String codename, final int major) {
        this(codename, major, 0);
    }

    /**
     * Constructor of Version.
     *
     * @param major
     *            The major version.
     * @param minor
     *            The minor version.
     * @param bugfx
     *            The bugfix version.
     * @param build
     *            The build number.
     */
    public Version(final int major, final int minor, final int bugfx, final int build) {
        this(null, major, minor, bugfx, build);
    }

    /**
     * Constructor of Version.
     *
     * @param major
     *            The major version.
     * @param minor
     *            The minor version.
     * @param bugfx
     *            The bugfix version.
     */
    public Version(final int major, final int minor, final int bugfx) {
        this(null, major, minor, bugfx);
    }

    /**
     * Constructor of Version.
     *
     * @param major
     *            The major version.
     * @param minor
     *            The minor version.
     */
    public Version(final int major, final int minor) {
        this(null, major, minor);
    }

    /**
     * Constructor of Version.
     *
     * @param major
     *            The major version.
     */
    public Version(final int major) {
        this(null, major);
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final String result;
        if (this.codename == null) {
            final StringBuilder resultBuilder = new StringBuilder(String.valueOf(this.major));
            if (this.minor >= 0) {
                resultBuilder.append(".").append(this.minor);
            }
            if (this.bugfx >= 0) {
                resultBuilder.append(".").append(this.bugfx);
            }
            if (this.build >= 0) {
                resultBuilder.append(".").append(this.build);
            }
            result = resultBuilder.toString();
        } else {
            result = this.codename;
        }
        return result;
    }
}
