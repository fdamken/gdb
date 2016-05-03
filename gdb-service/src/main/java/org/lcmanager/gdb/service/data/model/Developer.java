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

/**
 * Represents a developer that can be mapped to any 'developable' thing (e.g.
 * software).
 *
 */
@Data
@Accessors(chain = true)
public class Developer implements BaseModel<Integer> {
    /**
     * The serial version UID.
     *
     */
    private static final long serialVersionUID = -5489358678708889235L;

    /**
     * The ID of this developer.
     * 
     */
    private Integer id;
    /**
     * The name of this developer.
     * 
     */
    private String name;
}
