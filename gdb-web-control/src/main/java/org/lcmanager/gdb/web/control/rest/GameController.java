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
package org.lcmanager.gdb.web.control.rest;

import org.lcmanager.gdb.base.exception.GdbException;
import org.lcmanager.gdb.service.data.model.Game;
import org.lcmanager.gdb.service.game.GameService;
import org.lcmanager.gdb.web.control.status.GenerateStatus;
import org.lcmanager.gdb.web.control.util.ControllerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles the interaction with the game REST API.
 *
 */
@RestController
@RequestMapping("/api/game")
@GenerateStatus
@CacheConfig(cacheNames = "game-controller")
public class GameController {
    /**
     * The {@link GameService}.
     * 
     */
    @Autowired
    private GameService gameService;

    /**
     * Retrieves a single game.
     *
     * @param gameId
     *            The ID the of game to retrieve.
     * @return The retrieved game wrapped by a {@link ResponseEntity}.
     * @throws GdbException
     *             If any GDB error occurs.
     */
    @RequestMapping("/{gameId}")
    @Cacheable
    public ResponseEntity<ResourceSupport> handleById(@PathVariable final int gameId) throws GdbException {
        final Resource<Game> resource = ControllerUtil.createResource(this.gameService.retrieveGame(gameId));

        resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(GameController.class).handleById(gameId))
                .withSelfRel());

        return ControllerUtil.createResponse(resource);
    }
}
