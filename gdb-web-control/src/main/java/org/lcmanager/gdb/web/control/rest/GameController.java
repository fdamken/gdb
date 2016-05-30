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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.lcmanager.gdb.base.Paged;
import org.lcmanager.gdb.base.Sorting;
import org.lcmanager.gdb.base.Sorting.Direction;
import org.lcmanager.gdb.base.exception.GdbException;
import org.lcmanager.gdb.service.cs.ComputerSystemService;
import org.lcmanager.gdb.service.data.model.Category;
import org.lcmanager.gdb.service.data.model.ComputerSystem;
import org.lcmanager.gdb.service.data.model.Game;
import org.lcmanager.gdb.service.data.model.User;
import org.lcmanager.gdb.service.data.util.OsFamily;
import org.lcmanager.gdb.service.game.GameCompareResult;
import org.lcmanager.gdb.service.game.GameCompareService;
import org.lcmanager.gdb.service.game.GameQuery;
import org.lcmanager.gdb.service.game.GameService;
import org.lcmanager.gdb.service.user.UserService;
import org.lcmanager.gdb.web.control.status.GenerateStatus;
import org.lcmanager.gdb.web.control.util.ControllerUtil;
import org.lcmanager.gdb.web.control.util.exception.NullContentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
     * The {@link UserService}.
     * 
     */
    @Autowired
    private UserService userService;
    /**
     * The {@link GameService}.
     * 
     */
    @Autowired
    private GameService gameService;
    /**
     * The {@link ComputerSystemService}.
     * 
     */
    @Autowired
    private ComputerSystemService computerSystemService;
    /**
     * The {@link GameCompareService}.
     * 
     */
    @Autowired
    private GameCompareService gameCompareService;

    /**
     * Searches for games.
     *
     * @param page
     *            The page to load.
     * @param term
     *            The {@link GameQuery#getTerm() term} to set.
     * @param categories1
     *            The {@link GameQuery#getCategories1() first categories} to
     *            set.
     * @param categories2
     *            The {@link GameQuery#getCategories2() second categories} to
     *            set.
     * @param platforms
     *            The {@link GameQuery#getPlatforms() platforms} to set.
     * @param sortingTerm
     *            The {@link Sorting#getTerm() term} of the
     *            {@link GameQuery#getSorting() sorting} to set.
     * @param sortingDirection
     *            The {@link Sorting#getDirection() direction} of the
     *            {@link GameQuery#getSorting() sorting} to set.
     * @return All found games as returned by
     *         {@link GameService#retrieveGames(GameQuery, int)}.
     * @throws GdbException
     *             If any error occurs.
     * @see org.lcmanager.gdb.service.game.GameService#retrieveGames(org.lcmanager.gdb.service.game.GameQuery,
     *      int)
     */
    @RequestMapping
    @Cacheable
    public ResponseEntity<ResourceSupport> handleFind(@RequestParam(defaultValue = "1") final int page, //
            @RequestParam(defaultValue = "") final String term, //
            @RequestParam(defaultValue = "") final int[] categories1, //
            @RequestParam(defaultValue = "") final int[] categories2, //
            @RequestParam(defaultValue = "") final OsFamily[] platforms, //
            @RequestParam(defaultValue = "") final String sortingTerm, //
            @RequestParam(defaultValue = "ASC") final Direction sortingDirection) throws GdbException {
        final GameQuery query = new GameQuery(term, //
                Arrays.stream(categories1) //
                        .mapToObj(categoryId -> new Category().setId(categoryId)) //
                        .collect(Collectors.toList()), //
                Arrays.stream(categories2) //
                        .mapToObj(categoryId -> new Category().setId(categoryId)) //
                        .collect(Collectors.toList()), //
                Arrays.stream(platforms) //
                        .collect(Collectors.toList()), //
                new Sorting(sortingDirection, sortingTerm));
        final Paged<Game> games = this.gameService.retrieveGames(query, page);
        final PagedResources<Game> resource = ControllerUtil.createResources(games.getItems(), games.getPaginationMetadata());

        resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(GameController.class).handleFind(page, term,
                categories1, categories2, platforms, sortingTerm, sortingDirection)).withSelfRel());

        return ControllerUtil.createResponse(resource);
    }

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

    /**
     * Compares the game with the given ID to all computer systems the current
     * user has.
     *
     * @param gameId
     *            The ID of the game to compare.
     * @return The result of the comparison.
     * @throws GdbException
     *             If any error occurs.
     */
    @RequestMapping("/{gameId}/compare")
    @PreAuthorize("hasRole('USER')")
    @Cacheable
    public ResponseEntity<ResourceSupport> handleCompare(@PathVariable final int gameId) throws GdbException {
        final User user = this.userService.retrieveUser();
        final Game game = this.gameService.retrieveGame(gameId);
        if (game == null) {
            throw new NullContentException();
        }
        final List<ComputerSystem> computerSystems = this.computerSystemService.retrieveComputerSystems(user);
        if (computerSystems == null) {
            throw new NullContentException();
        }
        final List<GameCompareResult> gameCompareResult = computerSystems.stream().parallel() //
                .map(computerSystem -> this.gameCompareService.compare(game, computerSystem)) //
                .collect(Collectors.toList());

        final Resources<GameCompareResult> resource = ControllerUtil.createResources(gameCompareResult);

        resource.add(ControllerLinkBuilder
                .linkTo(ControllerLinkBuilder.methodOn(GameController.class).handleComparePrimary(gameId)).withSelfRel());

        return ControllerUtil.createResponse(resource);
    }

    /**
     * Compares the game with the given ID to the primary computer system of the
     * current user.
     *
     * @param gameId
     *            The ID of the game to compare.
     * @return The result of the comparison.
     * @throws GdbException
     *             If any error occurs.
     */
    @RequestMapping("/{gameId}/compare/primary")
    @PreAuthorize("hasRole('USER')")
    @Cacheable
    public ResponseEntity<ResourceSupport> handleComparePrimary(@PathVariable final int gameId) throws GdbException {
        final User user = this.userService.retrieveUser();
        final Game game = this.gameService.retrieveGame(gameId);
        if (game == null) {
            throw new NullContentException();
        }
        final ComputerSystem computerSystem = this.computerSystemService.retrievePrimaryComputerSystem(user);
        if (computerSystem == null) {
            throw new NullContentException();
        }
        final GameCompareResult gameCompareResult = this.gameCompareService.compare(game, computerSystem);

        final Resource<GameCompareResult> resource = ControllerUtil.createResource(gameCompareResult);

        resource.add(ControllerLinkBuilder
                .linkTo(ControllerLinkBuilder.methodOn(GameController.class).handleComparePrimary(gameId)).withSelfRel());

        return ControllerUtil.createResponse(resource);
    }
}
