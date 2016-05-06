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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lcmanager.gdb.service.data.model.Category;
import org.lcmanager.gdb.service.data.model.Developer;
import org.lcmanager.gdb.service.data.model.Genre;
import org.lcmanager.gdb.service.data.model.Publisher;
import org.lcmanager.gdb.service.data.util.OsFamily;

import lombok.Value;

/**
 * A game query is used to query for games and restict the search results.
 *
 */
@Value
public class GameQuery {
    /**
     * The term to search for.
     * 
     */
    private String term;
    /**
     * A list of categories that must be present on a game to make it eligable
     * for the result.
     * 
     */
    private List<Category> categories;
    /**
     * A list of developers that must be present on a game to make it eligable
     * for the result.
     * 
     */
    private List<Developer> developers;
    /**
     * A list of genres that must be present on a game to make it eligable for
     * the result.
     * 
     */
    private List<Genre> genres;
    /**
     * A list of platforms that must be present on a game to make it eligable
     * for the result.
     * 
     */
    private List<OsFamily> platforms;
    /**
     * A list of publishers that must be present on a game to make it eligable
     * for the result.
     * 
     */
    private List<Publisher> publishers;

    /**
     * Constructor of GameQuery.
     *
     * @param term
     *            The {@link #term} to set.
     * @param categories
     *            The {@link #categories} to set.
     * @param developers
     *            The {@link #developers} to set.
     * @param genres
     *            The {@link #genres} to set.
     * @param platforms
     *            The {@link #platforms} to set.
     * @param publishers
     *            The {@link #publishers} to set.
     */
    public GameQuery(final String term, final List<Category> categories, final List<Developer> developers,
            final List<Genre> genres, final List<OsFamily> platforms, final List<Publisher> publishers) {
        this.term = term;
        this.categories = Collections.unmodifiableList(categories);
        this.developers = Collections.unmodifiableList(developers);
        this.genres = Collections.unmodifiableList(genres);
        this.platforms = Collections.unmodifiableList(platforms);
        this.publishers = Collections.unmodifiableList(publishers);
    }

    public static GameQuery.Builder.Property builder() {
        return new Builder().new Property();
    }

    public static class Builder {
        /**
         * The term to search for.
         * 
         */
        private String term;
        /**
         * A list of categories that must be present on a game to make it
         * eligable for the result.
         * 
         */
        private final List<Category> categories = new ArrayList<>();
        /**
         * A list of developers that must be present on a game to make it
         * eligable for the result.
         * 
         */
        private final List<Developer> developers = new ArrayList<>();
        /**
         * A list of genres that must be present on a game to make it eligable
         * for the result.
         * 
         */
        private final List<Genre> genres = new ArrayList<>();
        /**
         * A list of platforms that must be present on a game to make it
         * eligable for the result.
         * 
         */
        private final List<OsFamily> platforms = new ArrayList<>();
        /**
         * A list of publishers that must be present on a game to make it
         * eligable for the result.
         * 
         */
        private final List<Publisher> publishers = new ArrayList<>();

        public Property and() {
            return new Property();
        }

        public GameQuery build() {
            return new GameQuery(this.term, this.categories, this.developers, this.genres, this.platforms, this.publishers);
        }

        public class Property {
            public Builder term(final String term) {
                Builder.this.term = term;

                return Builder.this;
            }

            public Builder category(final Category category) {
                Builder.this.categories.add(category);

                return Builder.this;
            }

            public Builder developer(final Developer developer) {
                Builder.this.developers.add(developer);

                return Builder.this;
            }

            public Builder genre(final Genre genre) {
                Builder.this.genres.add(genre);

                return Builder.this;
            }

            public Builder platform(final OsFamily platform) {
                Builder.this.platforms.add(platform);

                return Builder.this;
            }

            public Builder publisher(final Publisher publisher) {
                Builder.this.publishers.add(publisher);

                return Builder.this;
            }

            public GameQuery build() {
                return Builder.this.build();
            }
        }
    }
}
