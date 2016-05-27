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
import org.lcmanager.gdb.service.data.model.Game;
import org.lcmanager.gdb.service.data.util.OsFamily;
import org.lcmanager.gdb.service.data.util.RequirementType;

/**
 * Mapper for {@link Game}.
 *
 */
public interface GameMapper extends BaseMapper<Game, Integer> {
    /**
     * Adds the developer with the given ID to the game with the given ID.
     *
     * @param id
     *            The ID of the game to add the developer to.
     * @param developerId
     *            The ID of the developer to add to the game.
     */
    void addDeveloper(@Param("id") int id, @Param("developerId") int developerId);

    /**
     * Removes the developer with the given ID from the game with the given ID.
     *
     * @param id
     *            The ID of the game to remove the developer from.
     * @param developerId
     *            The ID of the developer to remove from the game.
     */
    void removeDeveloper(@Param("id") int id, @Param("developerId") int developerId);

    /**
     * Adds the given platform to the game with the given ID.
     *
     * @param id
     *            The ID of the game to add the platform to.
     * @param platform
     *            The platform to add to the game.
     */
    void addPlatform(@Param("id") int id, @Param("platform") OsFamily platform);

    /**
     * Removes the given platform from the game with the given ID.
     *
     * @param id
     *            The ID of the game to remove the platform from.
     * @param platform
     *            The platform to remove from the game.
     */
    void removePlatform(@Param("id") int id, @Param("platform") OsFamily platform);

    /**
     * Adds the publisher with the given ID to the game with the given ID.
     *
     * @param id
     *            The ID of the game to add the publisher to.
     * @param publisherId
     *            The ID of the publisher to add to the game.
     */
    void addPublisher(@Param("id") int id, @Param("publisherId") int publisherId);

    /**
     * Removes the publisher with the given ID from the game with the given ID.
     *
     * @param id
     *            The ID of the game to remove the publisher from.
     * @param publisherId
     *            The ID of the publisher to remove from the game.
     */
    void removePublisher(@Param("id") int id, @Param("publisherId") int publisherId);

    /**
     * Adds the category with the given ID to the game with the given ID.
     *
     * @param id
     *            The ID of the game to add the category to.
     * @param categoryId
     *            The ID of the category to add to the game.
     */
    void addCategory(@Param("id") int id, @Param("categoryId") int categoryId);

    /**
     * Removes the category with the given ID from the game with the given ID.
     *
     * @param id
     *            The ID of the game to remove the category from.
     * @param categoryId
     *            The ID of the category to remove from the game.
     */
    void removeCategory(@Param("id") int id, @Param("categoryId") int categoryId);

    /**
     * Adds the genre with the given ID to the game with the given ID.
     *
     * @param id
     *            The ID of the game to add the genre to.
     * @param genreId
     *            The ID of the genre to add to the game.
     */
    void addGenre(@Param("id") int id, @Param("genreId") int genreId);

    /**
     * Removes the genre with the given ID from the game with the given ID.
     *
     * @param id
     *            The ID of the game to remove the genre from.
     * @param genreId
     *            The ID of the genre to remove from the game.
     */
    void removeGenre(@Param("id") int id, @Param("genreId") int genreId);

    /**
     * Adds the screenshot with the given ID to the game with the given ID.
     *
     * @param id
     *            The ID of the game to add the screenshot to.
     * @param screenshotId
     *            The ID of the screenshot to add to the game.
     */
    void addScreenshot(@Param("id") int id, @Param("screenshotId") int screenshotId);

    /**
     * Removes the screenshot with the given ID from the game with the given ID.
     *
     * @param id
     *            The ID of the game to remove the screenshot from.
     * @param screenshotId
     *            The ID of the screenshot to remove from the game.
     */
    void removeScreenshot(@Param("id") int id, @Param("screenshotId") int screenshotId);

    /**
     * Adds the requirement with the given requirement type for the given
     * operating system family to the given game.
     *
     * @param id
     *            The ID of the game to add the requirement to.
     * @param platform
     *            The operating system family the requirement is relating to.
     * @param requirementId
     *            The ID of the requirement to add.
     * @param requirementType
     *            The type of the requirement to add.
     */
    void addRequirement(@Param("id") int id, @Param("platform") OsFamily platform, @Param("requirementId") int requirementId,
            @Param("requirementType") RequirementType requirementType);

    /**
     * Removes the requirement for the given operating system family from the
     * given game.
     *
     * @param id
     *            The ID of the game to remove the requirement from.
     * @param platform
     *            The operating system family the requirement to remove is for.
     */
    void removeRequirement(@Param("id") int id, @Param("platform") OsFamily platform);
}
