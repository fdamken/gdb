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

import java.util.Map;
import java.util.Set;

import org.lcmanager.gdb.base.CollectionUtil;
import org.lcmanager.gdb.base.Paged;
import org.lcmanager.gdb.service.annotation.Generic;
import org.lcmanager.gdb.service.data.model.Brand;
import org.lcmanager.gdb.service.data.model.Category;
import org.lcmanager.gdb.service.data.model.Developer;
import org.lcmanager.gdb.service.data.model.Game;
import org.lcmanager.gdb.service.data.model.Genre;
import org.lcmanager.gdb.service.data.model.Graphics;
import org.lcmanager.gdb.service.data.model.Processor;
import org.lcmanager.gdb.service.data.model.Publisher;
import org.lcmanager.gdb.service.data.model.Requirement;
import org.lcmanager.gdb.service.data.model.Screenshot;
import org.lcmanager.gdb.service.data.util.OsFamily;
import org.lcmanager.gdb.service.data.util.RequirementType;
import org.lcmanager.gdb.service.game.GameQuery;
import org.lcmanager.gdb.service.game.GameService;
import org.lcmanager.gdb.service.impl.data.mapper.CategoryMapper;
import org.lcmanager.gdb.service.impl.data.mapper.DeveloperMapper;
import org.lcmanager.gdb.service.impl.data.mapper.GameMapper;
import org.lcmanager.gdb.service.impl.data.mapper.GenreMapper;
import org.lcmanager.gdb.service.impl.data.mapper.PublisherMapper;
import org.lcmanager.gdb.service.impl.data.mapper.RequirementMapper;
import org.lcmanager.gdb.service.impl.data.mapper.ScreenshotMapper;
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
     * The {@link PublisherMapper}.
     * 
     */
    @Autowired
    private PublisherMapper publisherMapper;
    /**
     * The {@link DeveloperMapper}.
     * 
     */
    @Autowired
    private DeveloperMapper developerMapper;
    /**
     * The {@link CategoryMapper}.
     * 
     */
    @Autowired
    private CategoryMapper categoryMapper;
    /**
     * The {@link GenreMapper}.
     * 
     */
    @Autowired
    private GenreMapper genreMapper;
    /**
     * The {@link ScreenshotMapper}.
     * 
     */
    @Autowired
    private ScreenshotMapper screenshotMapper;
    /**
     * The {@link RequirementMapper}.
     * 
     */
    @Autowired
    private RequirementMapper requirementMapper;

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.game.GameService#retrieveGames(org.lcmanager.gdb.service.game.GameQuery,
     *      int, boolean)
     */
    @Override
    public Paged<Game> retrieveGames(final GameQuery query, final int page, final boolean loadAll) {
        throw new UnsupportedOperationException("This operation is not supported! Use a branded service instead!");
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.game.GameService#retrieveGame(int)
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable
    public Game retrieveGame(final int gameId) {
        if (!this.gameMapper.exists(gameId)) {
            return null;
        }
        return this.gameMapper.findById(gameId);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.game.GameService#save(org.lcmanager.gdb.service.data.model.Game)
     */
    @Override
    @Transactional
    public void save(final Game game) {
        if (game == null || game.getId() != null && this.gameMapper.exists(game.getId())) {
            return;
        }

        this.gameMapper.insert(game);

        final int gameId = game.getId();

        final Set<OsFamily> platforms = CollectionUtil.orEmpty(game.getPlatforms());
        platforms.stream().forEach(platform -> this.gameMapper.addPlatform(gameId, platform));

        final Set<Publisher> publishers = game.getPublishers();
        publishers.stream() //
                .parallel() //
                .filter(publisher -> !this.publisherMapper.existsName(publisher.getName())) //
                .forEach(this.publisherMapper::insert);
        publishers.stream() //
                .parallel() //
                .filter(publisher -> publisher.getId() == null) //
                .forEach(publisher -> {
                    publisher.setId(this.publisherMapper.findByName(publisher.getName()).getId());
                });
        publishers.stream() //
                .parallel() //
                .mapToInt(Publisher::getId) //
                .forEach(publisherId -> {
                    this.gameMapper.addPublisher(gameId, publisherId);
                });

        final Set<Developer> developers = CollectionUtil.orEmpty(game.getDevelopers());
        developers.stream() //
                .parallel() //
                .filter(developer -> !this.developerMapper.existsName(developer.getName())) //
                .forEach(this.developerMapper::insert);
        developers.stream() //
                .parallel() //
                .filter(developer -> developer.getId() == null) //
                .forEach(developer -> {
                    developer.setId(this.developerMapper.findByName(developer.getName()).getId());
                });
        developers.stream() //
                .parallel() //
                .mapToInt(Developer::getId) //
                .forEach(developerId -> {
                    this.gameMapper.addDeveloper(gameId, developerId);
                });

        final Set<Category> categories = CollectionUtil.orEmpty(game.getCategories());
        categories.stream() //
                .parallel() //
                .filter(category -> !this.categoryMapper.existsDescription(category.getDescription())) //
                .forEach(this.categoryMapper::insert);
        categories.stream() //
                .parallel() //
                .filter(category -> category.getId() == null) //
                .forEach(category -> {
                    category.setId(this.categoryMapper.findByDescription(category.getDescription()).getId());
                });
        categories.stream() //
                .parallel() //
                .mapToInt(Category::getId) //
                .forEach(categoryId -> {
                    this.gameMapper.addCategory(gameId, categoryId);
                });

        final Set<Genre> genres = CollectionUtil.orEmpty(game.getGenres());
        genres.stream() //
                .parallel() //
                .filter(genre -> !this.genreMapper.existsDescription(genre.getDescription())) //
                .forEach(this.genreMapper::insert);
        genres.stream() //
                .parallel() //
                .filter(genre -> genre.getId() == null) //
                .forEach(genre -> {
                    genre.setId(this.genreMapper.findByDescription(genre.getDescription()).getId());
                });
        genres.stream() //
                .parallel() //
                .mapToInt(Genre::getId) //
                .forEach(genreId -> {
                    this.gameMapper.addGenre(gameId, genreId);
                });

        final Set<Screenshot> screenshots = CollectionUtil.orEmpty(game.getScreenshots());
        screenshots.stream() //
                .parallel() //
                .forEach(this.screenshotMapper::insert);
        screenshots.stream() //
                .parallel() //
                .mapToInt(Screenshot::getId) //
                .forEach(screenshotId -> {
                    this.gameMapper.addScreenshot(gameId, screenshotId);
                });

        final Map<OsFamily, Requirement> minimumRequirements = CollectionUtil.orEmpty(game.getMinimumRequirements());
        minimumRequirements.entrySet().stream()//
                .parallel() //
                .forEach(entry -> {
                    this.insertRequirement(entry.getValue());
                    this.gameMapper.addRequirement(gameId, entry.getKey(), entry.getValue().getId(), RequirementType.MINIMUM);
                });

        final Map<OsFamily, Requirement> recommendedRequirements = CollectionUtil.orEmpty(game.getRecommendedRequirements());
        recommendedRequirements.entrySet().stream()//
                .parallel() //
                .forEach(entry -> {
                    this.insertRequirement(entry.getValue());
                    this.gameMapper.addRequirement(gameId, entry.getKey(), entry.getValue().getId(), RequirementType.RECOMMENDED);
                });
    }

    /**
     * Inserts the given requirement into the database.
     *
     * @param requirement
     *            The requirement to insert.
     */
    private void insertRequirement(final Requirement requirement) {
        this.requirementMapper.insert(requirement);

        final int requirementId = requirement.getId();

        // TODO: Insert operating system.

        final Map<Brand, Processor> processors = CollectionUtil.orEmpty(requirement.getProcessors());
        processors.values().stream() //
                .mapToInt(Processor::getId) //
                .forEach(processorId -> {
                    this.requirementMapper.addProcessor(requirementId, processorId);
                });

        final Map<Brand, Graphics> graphics = CollectionUtil.orEmpty(requirement.getGraphics());
        graphics.values().stream()//
                .mapToInt(Graphics::getId)//
                .forEach(graphicsId -> {
                    this.requirementMapper.addGraphics(requirementId, graphicsId);
                });
    }
}
