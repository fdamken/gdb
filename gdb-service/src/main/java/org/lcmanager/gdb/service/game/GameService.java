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

import org.lcmanager.gdb.base.Paged;
import org.lcmanager.gdb.service.data.model.Game;
import org.lcmanager.gdb.service.game.exception.GameServiceException;

/**
 * The game service is used for any interaction with games.
 *
 */
public interface GameService {
    /**
     * The site of any page. {@value #PAGE_SIZE}.
     * 
     */
    int PAGE_SIZE = 25;

    /**
     * Retrieves the games that match the given {@link GameQuery query} on the
     * given page. Each page contains {@value #PAGE_SIZE} items.
     * 
     * <p>
     * To speed up the loading of the items, <code>loadAll</code> can be set to
     * <code>false</code>. If so, not all properties of every item are loaded
     * but only the following:
     * <ul>
     * <li>ID</li>
     * <li>name</li>
     * <li>platforms</li>
     * <li>release date</li>
     * </ul>
     * <p>
     * Loading all properties can be really slow and the approximated duration
     * is about 15 seconds. Loading only the above properties takes, depending
     * on the implementation, less than 1 second. That more than 15 times
     * faster!
     * </p>
     * </p>
     * 
     * @param query
     *            The {@link GameQuery query} to search for.
     * @param page
     *            The page to load. Must not be less than one.
     * @param loadAll
     *            Whether to load all properties or not (see above).
     * @return All found games and the page meta-data.
     * @throws GameServiceException
     *             If any error occurs whilst fetching the games.
     */
    Paged<Game> retrieveGames(final GameQuery query, final int page, final boolean loadAll) throws GameServiceException;

    /**
     * Delegates to {@link #retrieveGames(GameQuery, int, boolean)} with
     * <code>loadAll = true</code>
     *
     * @param query
     *            Passed to {@link #retrieveGames(GameQuery, int, boolean)}.
     * @param page
     *            Passed to {@link #retrieveGames(GameQuery, int, boolean)}.
     * @return The result of {@link #retrieveGames(GameQuery, int, boolean)}.
     * @throws GameServiceException
     *             If {@link #retrieveGames(GameQuery, int, boolean)} throws it.
     * @see org.lcmanager.gdb.service.game.GameService#retrieveGames(org.lcmanager.gdb.service.game.GameQuery,
     *      int, boolean)
     */
    default Paged<Game> retrieveGames(final GameQuery query, final int page) throws GameServiceException {
        return this.retrieveGames(query, page, false);
    }

    /**
     * Retrieves the game with the given ID.
     *
     * @param gameId
     *            The ID of the game to retrieve.
     * @return The {@link Game}, if any. Otherwise <code>null</code>.
     * @throws GameServiceException
     *             If any error occurs whilst retrieving the game.
     */
    Game retrieveGame(final int gameId) throws GameServiceException;
}
