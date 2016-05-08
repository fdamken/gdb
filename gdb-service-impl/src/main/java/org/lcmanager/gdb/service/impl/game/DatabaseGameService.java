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
package org.lcmanager.gdb.service.impl.game;

import org.lcmanager.gdb.base.Paged;
import org.lcmanager.gdb.service.annotation.Generic;
import org.lcmanager.gdb.service.data.model.Game;
import org.lcmanager.gdb.service.game.GameQuery;
import org.lcmanager.gdb.service.game.GameService;
import org.lcmanager.gdb.service.impl.data.mapper.GameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A generic implementation of {@link GameService} that accesses the database.
 *
 */
@Service("dbGameService")
@Generic
@CacheConfig(cacheNames = "db-game-service")
public class DatabaseGameService implements GameService {
    /**
     * The {@link Game} mapper.
     * 
     */
    @Autowired
    private GameMapper gameMapper;

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.game.GameService#retrieveGames(org.lcmanager.gdb.service.game.GameQuery,
     *      int)
     */
    @Override
    public Paged<Game> retrieveGames(final GameQuery query, final int page) {
        throw new UnsupportedOperationException("This operation is not supported! Use a branded service instead!");
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.game.GameService#retrieveGame(int)
     */
    @Transactional(readOnly = true)
    @Override
    @Cacheable
    public Game retrieveGame(final int gameId) {
        if (!this.gameMapper.exists(gameId)) {
            return null;
        }
        return this.gameMapper.findById(gameId);
    }
}
