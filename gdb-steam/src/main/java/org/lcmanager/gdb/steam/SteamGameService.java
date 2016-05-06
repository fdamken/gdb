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
package org.lcmanager.gdb.steam;

import org.lcmanager.gdb.base.Pagination;
import org.lcmanager.gdb.base.PaginationMetadata;
import org.lcmanager.gdb.service.annotation.Branded;
import org.lcmanager.gdb.service.data.model.Game;
import org.lcmanager.gdb.service.game.GameQuery;
import org.lcmanager.gdb.service.game.GameService;
import org.springframework.stereotype.Service;

/**
 * The Steam implementation of {@link GameService}.
 *
 */
@Service
@Branded
public class SteamGameService implements GameService {
    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.game.GameService#retrieveGames(org.lcmanager.gdb.service.game.GameQuery,
     *      org.lcmanager.gdb.base.PaginationMetadata)
     */
    @Override
    public Pagination<Game> retrieveGames(final GameQuery query, final PaginationMetadata paginationMetadata) {
        return /* TODO: some steam magic */ null;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.game.GameService#retrieveGame(int)
     */
    @Override
    public Game retrieveGame(final int gameId) {
        // TODO Auto-generated method body.
        return null;
    }
}
