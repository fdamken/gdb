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
package org.lcmanager.gdb.service.impl.data.mapper;

import org.apache.ibatis.annotations.Param;
import org.lcmanager.gdb.service.data.model.Brand;

/**
 * Mapper for {@link Brand}.
 *
 */
public interface BrandMapper extends BaseMapper<Brand, Integer> {
    /**
     * Checks whether any brand with the given name exist.
     *
     * @param name
     *            The name to check.
     * @return Whether any brand with the given name exists or not.
     */
    boolean existsName(@Param("name") String name);

    /**
     * Finds the brand with the given name.
     *
     * @param name
     *            The name of the brand to find.
     * @return The found brand, if any.
     */
    Brand findByName(@Param("name") String name);
}
