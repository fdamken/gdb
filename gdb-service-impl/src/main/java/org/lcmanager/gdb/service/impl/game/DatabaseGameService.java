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

import org.lcmanager.gdb.service.annotation.Generic;
import org.lcmanager.gdb.service.data.model.Game;
import org.lcmanager.gdb.service.game.GameService;
import org.lcmanager.gdb.service.impl.data.mapper.GameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A generic implementation of {@link GameService} that accesses the database.
 *
 */
@Component
@Generic
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
     * @see org.lcmanager.gdb.service.game.GameService#retrieveGame(int)
     */
    // @Transactional(readOnly = true)
    @Override
    public Game retrieveGame(final int gameId) {
        return this.gameMapper.findById(gameId);
    }
}