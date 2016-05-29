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
package org.lcmanager.gdb.service.data.model;

import java.util.Locale;

import org.lcmanager.gdb.base.Formatable;
import org.lcmanager.gdb.base.Version;
import org.lcmanager.gdb.service.data.util.OsFamily;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Represents an operating system including its version and the developer.
 *
 */
@Data
@Accessors(chain = true)
public class OperatingSystem implements BaseModel<Integer>, Formatable {
    /**
     * The serial version UID.
     *
     */
    private static final long serialVersionUID = 5104011880865239861L;

    // ~ Direct ~
    /**
     * The ID of this OS.
     * 
     */
    private Integer id;
    /**
     * The OS family this operating system belongs to.
     * 
     */
    private OsFamily osFamily;
    /**
     * The name of this operation system not including the version (e.g.
     * <code>Windows</code>).
     * 
     */
    private String name;
    /**
     * The version of this operation system.
     * 
     */
    private Version version;

    // ~ Mapped ~
    /**
     * The developer of this operation system (may be a company or a person,
     * respectively).
     * 
     */
    private Developer developer;

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.base.Formatable#format(java.util.Locale)
     */
    @Override
    public String format(final Locale locale) {
        return this.developer.getName() + " " + this.name + " " + this.version;
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.format();
    }
}
