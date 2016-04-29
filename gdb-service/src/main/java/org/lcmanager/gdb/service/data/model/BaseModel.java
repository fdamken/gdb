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

import java.io.Serializable;

/**
 * Represents the base interface for every model.
 *
 * @param <I>
 *            The type of the ID (the primary key).
 */
public interface BaseModel<I extends Serializable> extends Serializable {
    /**
     *
     * @return The ID.
     */
    I getId();

    /**
     * Sets the ID.
     *
     * @param id
     *            The ID to set.
     */
    void setId(I id);
}
