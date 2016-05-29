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

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Represents a processor and all its relevant technical data.
 *
 */
@Data
@Accessors(chain = true)
public class Processor implements BaseModel<Integer>, Formatable {
    /**
     * The serial version UID.
     *
     */
    private static final long serialVersionUID = -1608938574683528407L;

    /**
     * The ID of this processor.
     * 
     */
    private Integer id;
    /**
     * The model ID of the processor.
     * 
     */
    private String model;
    /**
     * The number of processor cores.
     * 
     */
    private Integer cores;
    /**
     * The number of processor threads.
     * 
     */
    private Integer threads;
    /**
     * The processor frequency in megahertz.
     * 
     */
    private Integer frequency;
    /**
     * The with of the instruction set (e.g. 32 or 64 Bit).
     * 
     */
    private Integer instructionSet;

    // ~ Mapped ~
    /**
     * The brand of the processor.
     * 
     */
    private Brand brand;

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.base.Formatable#format(java.util.Locale)
     */
    @Override
    public String format(final Locale locale) {
        return this.brand.format() + " " + this.model;
    }
}
