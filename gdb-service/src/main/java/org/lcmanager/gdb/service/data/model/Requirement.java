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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lcmanager.gdb.base.CollectionUtil;
import org.lcmanager.gdb.service.data.util.OsFamily;
import org.lcmanager.gdb.service.data.util.OsFamilyAware;
import org.lcmanager.gdb.service.data.util.OsFamilyAwareOperatingSystemList;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

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
    private Map<Brand, Processor> processors = new HashMap<>();
    /**
     * The graphics (card) of this requirement.
     * 
     */
    private Map<Brand, Graphics> graphics = new HashMap<>();

    // ~ Transient ~
    /**
     * A list that is used by MyBatis only to add processors without mapping
     * them explicitly to a map.
     * 
     */
    @Getter(AccessLevel.PRIVATE)
    private final transient List<Processor> processorAdder = CollectionUtil.createAddOnlyList(this::addProcessor);
    /**
     * A list that is used by MyBatis only to add graphics cards without mapping
     * them explicitly to a map.
     * 
     */
    @Getter(AccessLevel.PRIVATE)
    private final transient List<Graphics> graphicsAdder = CollectionUtil.createAddOnlyList(this::addGraphics);

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

    /**
     * Adds the given processor to {@link #processors}.
     *
     * <p>
     * This method is also invoked when invoking the {@link List#add(Object)} on
     * {@link #getProcessorAdder()}.
     * </p>
     * 
     * @param processor
     *            The processor to add.
     */
    private void addProcessor(final Processor processor) {
        if (processor == null || processor.getBrand() == null) {
            throw new IllegalArgumentException("Neither processor not processor.brand shall be null!");
        }

        this.processors.put(processor.getBrand(), processor);
    }

    /**
     * Adds the given graphics card to {@link #graphics}.
     *
     * <p>
     * This method is also invoked when invoking the {@link List#add(Object)} on
     * {@link #getGraphicsAdder()}.
     * </p>
     * 
     * @param graphics
     *            The graphics card to add.
     */
    private void addGraphics(final Graphics graphics) {
        if (graphics == null || graphics.getBrand() == null) {
            throw new IllegalArgumentException("Neither graphics not graphics.brand shall be null!");
        }

        this.graphics.put(graphics.getBrand(), graphics);
    }
}
