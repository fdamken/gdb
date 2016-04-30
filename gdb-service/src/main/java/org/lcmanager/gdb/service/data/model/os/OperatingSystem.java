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
package org.lcmanager.gdb.service.data.model.os;

import lombok.Value;

import org.lcmanager.gdb.base.Version;

/**
 * Represents an operating system including its version and the developer.
 *
 */
@Value
public class OperatingSystem {
    /**
     * The OS family this operating system belongs to.
     * 
     */
    OsFamily osFamily;
    /**
     * The developer of this operation system (may be a company or a person,
     * respectively).
     * 
     */
    String developer;
    /**
     * The name of this operation system not including the version (e.g.
     * <code>Windows</code>).
     * 
     */
    String name;
    /**
     * The version of this operation system.
     * 
     */
    Version version;

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.developer + " " + this.name + " " + this.version;
    }
}
