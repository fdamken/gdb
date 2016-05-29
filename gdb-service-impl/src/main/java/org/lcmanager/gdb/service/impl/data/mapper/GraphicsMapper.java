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
import org.lcmanager.gdb.service.data.model.Graphics;

/**
 * Mapper for {@link Graphics}.
 *
 */
public interface GraphicsMapper extends BaseMapper<Graphics, Integer> {
    /**
     * Checks whether any graphics card with the given model by the given brand
     * exists.
     *
     * @param brand
     *            The brand to check.
     * @param model
     *            The model to check.
     * @return Whether any graphics card with with the given model by the given
     *         brand exists or not.
     */
    boolean existsBrandModel(@Param("brand") Brand brand, @Param("model") String model);

    /**
     * Finds the graphics card with the given model by the given brand.
     *
     * @param brand
     *            The brand of the graphics card to find.
     * @param model
     *            The model of the graphics card to find.
     * @return The found graphics card, if any.
     */
    Graphics findByBrandAndModel(@Param("brand") Brand brand, @Param("model") String model);
}
