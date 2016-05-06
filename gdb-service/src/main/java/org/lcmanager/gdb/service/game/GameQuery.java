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

import org.lcmanager.gdb.base.Sorting;
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
     * The sorting;
     * 
     */
    private Sorting sorting;
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
     * @param sorting
     *            The {@link #sorting} to set.
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
    public GameQuery(final String term, final Sorting sorting, final List<Category> categories, final List<Developer> developers,
            final List<Genre> genres, final List<OsFamily> platforms, final List<Publisher> publishers) {
        this.term = term;
        this.sorting = sorting;
        this.categories = Collections.unmodifiableList(categories);
        this.developers = Collections.unmodifiableList(developers);
        this.genres = Collections.unmodifiableList(genres);
        this.platforms = Collections.unmodifiableList(platforms);
        this.publishers = Collections.unmodifiableList(publishers);
    }

    /**
     * Creates a new {@link Builder}.
     *
     * @return The new {@link Builder}.
     */
    public static GameQuery.Builder.Property builder() {
        return new Builder().new Property();
    }

    /**
     * This class is used to instantiate a {@link GameQuery} in a user-friendly
     * way.
     *
     */
    public static class Builder {
        /**
         * The term to search for.
         * 
         */
        private String term;
        /**
         * The sorting.
         * 
         */
        private Sorting sorting;
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

        /**
         * Constructor of Builder.
         *
         */
        private Builder() {
            // Nothing to do.
        }

        /**
         * Adds a new property to query for.
         *
         * @return The new property.
         */
        public Property and() {
            return new Property();
        }

        /**
         * Sets the query sorting.
         *
         * @return A sort builder.
         */
        public SortBuilder sort() {
            return new SortBuilder();
        }

        /**
         * Builds the actual {@link GameQuery}.
         *
         * @return The {@link GameQuery}.
         */
        public GameQuery build() {
            return new GameQuery(this.term, this.sorting, this.categories, this.developers, this.genres, this.platforms,
                    this.publishers);
        }

        /**
         * Represents a property to search for.
         *
         */
        public class Property {
            /**
             * Sets the query term.
             *
             * @param term
             *            The term to set.
             * @return The {@link Builder}.
             */
            public Builder term(final String term) {
                Builder.this.term = term;

                return Builder.this;
            }

            /**
             * Adds the given category to the query.
             * 
             * @param category
             *            The category to add.
             * @return The {@link Builder}.
             */
            public Builder category(final Category category) {
                Builder.this.categories.add(category);

                return Builder.this;
            }

            /**
             * Adds the given developer to the query.
             * 
             * @param developer
             *            The developer to add.
             * @return The {@link Builder}.
             */
            public Builder developer(final Developer developer) {
                Builder.this.developers.add(developer);

                return Builder.this;
            }

            /**
             * Adds the given genre to the query.
             * 
             * @param genre
             *            The genre to add.
             * @return The {@link Builder}.
             */
            public Builder genre(final Genre genre) {
                Builder.this.genres.add(genre);

                return Builder.this;
            }

            /**
             * Adds the given platform to the query.
             * 
             * @param platform
             *            The platform to add.
             * @return The {@link Builder}.
             */
            public Builder platform(final OsFamily platform) {
                Builder.this.platforms.add(platform);

                return Builder.this;
            }

            /**
             * Adds the given publisher to the query.
             * 
             * @param publisher
             *            The publisher to add.
             * @return The {@link Builder}.
             */
            public Builder publisher(final Publisher publisher) {
                Builder.this.publishers.add(publisher);

                return Builder.this;
            }

            /**
             * Delegates to {@link Builder#build()}.
             *
             * @return The result of {@link Builder#build()}
             * @see org.lcmanager.gdb.service.game.GameQuery.Builder#build()
             */
            public GameQuery build() {
                return Builder.this.build();
            }
        }

        /**
         * Builds the sorting for a game query.
         *
         */
        public class SortBuilder {
            /**
             * The sorting term (e.g. name).
             * 
             */
            private String term;
            /**
             * The sorting direction.
             * 
             */
            private Sorting.Direction direction;

            /**
             * Sets the term.
             *
             * @param term
             *            The {@link #term} to set.
             * @return The sorting direction builder.
             */
            public SortDirectionBuilder by(final String term) {
                this.term = term;

                return new SortDirectionBuilder();
            }

            /**
             * Builds the sorting direction and returns to the initial builder.
             *
             */
            public class SortDirectionBuilder {
                /**
                 * Sets the sorting direction to ascending.
                 *
                 * @return The initial builder.
                 */
                public Builder ascending() {
                    SortBuilder.this.direction = Sorting.Direction.ASCENDING;

                    return this.build();
                }

                /**
                 * Sets the sorting direction to ascending.
                 *
                 * @return The initial builder.
                 */
                public Builder asc() {
                    return this.ascending();
                }

                /**
                 * Sets the sorting direction to descending.
                 *
                 * @return The initial builder.
                 */
                public Builder descending() {
                    SortBuilder.this.direction = Sorting.Direction.DESCENDING;

                    return this.build();
                }

                /**
                 * Sets the sorting direction to descending.
                 *
                 * @return The initial builder.
                 */
                public Builder desc() {
                    return this.descending();
                }

                /**
                 * Sets the sorting property on the initial builder.
                 *
                 * @return The initial builder.
                 */
                private Builder build() {
                    Builder.this.sorting = new Sorting(SortBuilder.this.direction, SortBuilder.this.term);

                    return Builder.this;
                }
            }
        }
    }
}
