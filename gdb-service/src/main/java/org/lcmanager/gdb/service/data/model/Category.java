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
 * Represents a category that can be assigned to a game.
 *
 */
@Data
@Accessors(chain = true)
public class Category implements BaseModel<Integer>, Formatable {
    /**
     * The serial version UID.
     *
     */
    private static final long serialVersionUID = -1996113727890542205L;

    // ~ Direct ~
    /**
     * The ID of this category. Represents a steam category ID.
     * 
     */
    private Integer id;
    /**
     * The description of this category-
     * 
     */
    private String description;

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.base.Formatable#format(java.util.Locale)
     */
    @Override
    public String format(final Locale locale) {
        return this.description;
    }
}
