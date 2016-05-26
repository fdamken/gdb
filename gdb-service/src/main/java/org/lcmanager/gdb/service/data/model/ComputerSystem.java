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

import org.lcmanager.gdb.service.data.util.OsFamily;
import org.lcmanager.gdb.service.data.util.OsFamilyAware;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * A computer system represents the setup of a user's computer (e.g. which
 * graphics card he has).
 *
 */
@Data
@Accessors(chain = true)
public class ComputerSystem implements BaseModel<Integer>, OsFamilyAware {
    /**
     * The serial version UID.
     *
     */
    private static final long serialVersionUID = -3419779176506771984L;

    // ~ Direct ~
    /**
     * The ID of this system.
     * 
     */
    private Integer id;
    /**
     * The OS family of this system.
     * 
     */
    private OsFamily osFamily;
    /**
     * The memory of this system in mebibyte.
     * 
     */
    private int memory;
    /**
     * The storage of this system is mebibyte.
     * 
     */
    private int storage;

    // ~ Mapped ~
    /**
     * The operating system that is installed in this system.
     * 
     */
    private OperatingSystem operatingSystem;
    /**
     * The processor of this system.
     * 
     */
    private Processor processor;
    /**
     * The graphics card of this system.
     * 
     */
    private Graphics graphics;
}
