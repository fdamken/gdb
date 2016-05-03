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

import lombok.Data;
import lombok.experimental.Accessors;

import org.lcmanager.gdb.service.data.util.OsFamily;
import org.lcmanager.gdb.service.data.util.OsFamilyAware;
import org.lcmanager.gdb.service.data.util.OsFamilyAwareOperatingSystemList;

/**
 * A requirement represents the system setup that is required to run any game.
 *
 */
@Data
@Accessors(chain = true)
public class Requirement implements BaseModel<Integer>, OsFamilyAware {
    /**
     * The serial version UID.
     *
     */
    private static final long serialVersionUID = 1710929911278504094L;

    // ~ Direct ~
    /**
     * The ID of this requirement.
     * 
     */
    private Integer id;
    /**
     * The OS family of this requirement.
     * 
     */
    private OsFamily osFamily;
    /**
     * The memory of this requirement in mebibyte.
     * 
     */
    private int memory;
    /**
     * The storage of this requirement is mebibyte.
     * 
     */
    private int storage;

    // ~ Mapped ~
    /**
     * The operating systems that are supported.
     * 
     */
    private OsFamilyAwareOperatingSystemList operatingSystems;
    /**
     * The processor of this requirement.
     * 
     */
    private Processor processor;
    /**
     * The graphics (card) of this requirement.
     * 
     */
    private Graphics graphics;

    /**
     * Sets {@link #operatingSystems}.
     * 
     * <p>
     * <b> WARNING: If the OS family of the given list is not <code>null</code>
     * or equal to {@link #osFamily}, an error occurs. </b>
     * </p>
     * 
     * @param operatingSystems
     *            The {@link #operatingSystems} to set.
     * @return <code>this</code>
     */
    public Requirement setOperatingSystems(final OsFamilyAwareOperatingSystemList operatingSystems) {
        if (operatingSystems.getOsFamily() != null && !operatingSystems.getOsFamily().equals(this.osFamily)) {
            throw new IllegalArgumentException("OperatingSystems must belong to the OS family " + this.osFamily + "!");
        }

        this.operatingSystems = operatingSystems;

        return this;
    }
}
