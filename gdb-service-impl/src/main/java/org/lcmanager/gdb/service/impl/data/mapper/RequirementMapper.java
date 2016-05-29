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
import org.lcmanager.gdb.service.data.model.Requirement;

/**
 * Mapper for {@link Requirement}.
 *
 */
public interface RequirementMapper extends BaseMapper<Requirement, Integer> {
    /**
     * Adds the processor with the given ID to the game with the given ID.
     *
     * @param id
     *            The ID of the game to add the processor to.
     * @param processorId
     *            The ID of the processor to add to the game.
     */
    void addProcessor(@Param("id") int id, @Param("processorId") int processorId);

    /**
     * Removes the processor with the given ID from the game with the given ID.
     *
     * @param id
     *            The ID of the game to remove the processor from.
     * @param processorId
     *            The ID of the processor to remove from the game.
     */
    void removeProcessor(@Param("id") int id, @Param("processorId") int processorId);

    /**
     * Adds the graphics with the given ID to the game with the given ID.
     *
     * @param id
     *            The ID of the game to add the graphics to.
     * @param graphicsId
     *            The ID of the graphics to add to the game.
     */
    void addGraphics(@Param("id") int id, @Param("graphicsId") int graphicsId);

    /**
     * Removes the graphics with the given ID from the game with the given ID.
     *
     * @param id
     *            The ID of the game to remove the graphics from.
     * @param graphicsId
     *            The ID of the graphics to remove from the game.
     */
    void removeGraphics(@Param("id") int id, @Param("graphicsId") int graphicsId);
}
