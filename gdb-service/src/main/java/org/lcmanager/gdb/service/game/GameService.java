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
package org.lcmanager.gdb.service.game;

import org.lcmanager.gdb.base.Pagination;
import org.lcmanager.gdb.base.PaginationMetadata;
import org.lcmanager.gdb.service.data.model.Game;

/**
 * The game service is used for any interaction with games.
 *
 */
public interface GameService {
    /**
     * Retrieves all games that match the given game query.
     *
     * @param query
     *            The game query to retrieve the games for.
     * @param paginationMetadata
     *            The {@link PaginationMetadata} that is used to page the
     *            result.
     * @return The pagination containing the games that match the given query.
     */
    Pagination<Game> retrieveGames(final GameQuery query, final PaginationMetadata paginationMetadata);

    /**
     * Retrieves the game with the given ID.
     *
     * @param gameId
     *            The ID of the game to retrieve.
     * @return The {@link Game}, if any. Otherwise <code>null</code>.
     */
    Game retrieveGame(final int gameId);
}
