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
import org.lcmanager.gdb.base.Sorting.Direction;
import org.lcmanager.gdb.service.data.model.Category;
import org.lcmanager.gdb.service.data.util.OsFamily;

import lombok.Value;

/**
 * A game query is used to query for games and restrict the search results.
 *
 * <p>
 * All search properties are linked with ANDs.
 * </p>
 *
 */
@Value
public class GameQuery {
    /**
     * The term to search for.
     * 
     */
    String term;
    /**
     * The first categories to search for. All categories in this list are
     * linked with ORs.
     * 
     */
    List<Category> categories1;
    /**
     * The second categories to search for. All categories in this list are
     * linked with ORs.
     * 
     */
    List<Category> categories2;
    /**
     * The platforms to search for. All platforms in this list are linked with
     * ORs.
     * 
     */
    List<OsFamily> platforms;

    /**
     * The {@link Sorting} that should be applied to the result.
     * 
     */
    Sorting sorting;

    /**
     * Constructor of GameQuery.
     *
     * @param term
     *            The {@link #term} to set.
     * @param categories1
     *            The {@link #categories1} to set.
     * @param categories2
     *            The {@link #categories2} to set.
     * @param platforms
     *            The {@link #platforms} to set.
     * @param sorting
     *            The {@link #sorting} to set.
     */
    public GameQuery(final String term, final List<Category> categories1, final List<Category> categories2,
            final List<OsFamily> platforms, final Sorting sorting) {
        this.term = term;
        this.categories1 = Collections.unmodifiableList(categories1);
        this.categories2 = Collections.unmodifiableList(categories2);
        this.platforms = Collections.unmodifiableList(platforms);
        this.sorting = sorting;
    }

    /**
     * Creates a new {@link GameQueryBuilder game query builder}.
     *
     * @return The newly created {@link GameQueryBuilder game query builder}.
     */
    public static GameQueryBuilder builder() {
        return new GameQueryBuilder();
    }

    /**
     * This builder is used for building a {@link GameQuery} in a declarative
     * way.
     *
     */
    public static class GameQueryBuilder {
        /**
         * The term to search for.
         * 
         */
        private String term = null;
        /**
         * The first categories to search for.
         * 
         */
        private final List<Category> categories1 = new ArrayList<>();
        /**
         * The second categories to search for.
         * 
         */
        private final List<Category> categories2 = new ArrayList<>();
        /**
         * The platforms to search for.
         * 
         */
        private final List<OsFamily> platforms = new ArrayList<>();

        /**
         * The property to sort by.
         * 
         */
        private String sortingTerm = null;
        /**
         * The direction to sort.
         * 
         */
        private Direction sortingDirection = null;

        /**
         * Constructor of GameQueryBuilder.
         *
         */
        private GameQueryBuilder() {
            // Nothing to do.
        }

        /**
         * Sets the term to search for.
         * 
         * <p>
         * This may only be invoked once.
         * </p>
         *
         * @param term
         *            The term to search for.
         * @return Continue with building.
         */
        public AndBuilder term(final String term) {
            if (this.term == null) {
                this.term = term;
            } else {
                throw new IllegalStateException("Term has already been initialized!");
            }

            return new AndBuilder();
        }

        /**
         * Initializes the category builder.
         * 
         * <p>
         * This may only be invoked twice.
         * </p>
         *
         * @return The category builder.
         */
        public CategoryBuilder category() {
            if (this.categories1.size() <= 0) {
                return new CategoryBuilder(this.categories1);
            } else if (this.categories2.size() <= 0) {
                return new CategoryBuilder(this.categories2);
            } else {
                throw new IllegalStateException("Both categories1 and categories2 have already been initialized!");
            }
        }

        /**
         * Initializes the platforms builder.
         * 
         * <p>
         * This may only be invoked once.
         * </p>
         *
         * @return The platforms builder.
         */
        public PlatformsBuilder platforms() {
            if (this.platforms.size() <= 0) {
                return new PlatformsBuilder(this.platforms);
            }
            throw new IllegalStateException("Platforms has already been initialized!");
        }

        /**
         * Initializes the sorting builder.
         * 
         * <p>
         * This may only be invoked once.
         * </p>
         *
         * @return The sorting builder.
         */
        public SortingTermBuilder sort() {
            if (this.sortingTerm == null && this.sortingDirection == null) {
                return new SortingTermBuilder();
            }
            throw new IllegalStateException("Sorting has already been initialized!");
        }

        /**
         * Builds the {@link GameQuery}.
         *
         * @return The built {@link GameQuery}.
         */
        public GameQuery build() {
            return new GameQuery(this.term, this.categories1, this.categories2, this.platforms,
                    new Sorting(this.sortingDirection, this.sortingTerm));
        }

        /**
         * Used to build the categories.
         *
         */
        public class CategoryBuilder {
            /**
             * The built categories.
             * 
             */
            private final List<Category> categories;

            /**
             * Constructor of CategoryBuilder.
             *
             * @param categories
             *            The list that all categories will be added to.
             */
            private CategoryBuilder(final List<Category> categories) {
                this.categories = categories;
            }

            /**
             * Adds the given category to the game query.
             *
             * @param category
             *            The category to add.
             * @return Continue with building.
             */
            public CategoryBuilderOr contains(final Category category) {
                this.categories.add(category);

                return new CategoryBuilderOr();
            }

            /**
             * Continues with building.
             *
             */
            public class CategoryBuilderOr extends AndBuilder {
                /**
                 * Constructor of CategoryBuilderOr.
                 *
                 */
                private CategoryBuilderOr() {
                    // Nothing to do.
                }

                /**
                 *
                 * @return The initial category builder.
                 */
                public CategoryBuilder or() {
                    return CategoryBuilder.this;
                }
            }
        }

        /**
         * Builds the platforms.
         *
         */
        public class PlatformsBuilder {
            /**
             * The built platforms.
             * 
             */
            private final List<OsFamily> platforms;

            /**
             * Constructor of PlatformsBuilder.
             *
             * @param platforms
             *            The list to add the built platforms to.
             */
            private PlatformsBuilder(final List<OsFamily> platforms) {
                this.platforms = platforms;
            }

            /**
             * Adds the given platform to the game query.
             *
             * @param platform
             *            The platform to add.
             * @return Continue with building.
             */
            public PlatformsBuilderOr contains(final OsFamily platform) {
                this.platforms.add(platform);

                return new PlatformsBuilderOr();
            }

            /**
             * Continues with building.
             *
             */
            public class PlatformsBuilderOr extends AndBuilder {
                /**
                 * Constructor of PlatformsBuilderOr.
                 *
                 */
                private PlatformsBuilderOr() {
                    // Nothing to do.
                }

                /**
                 *
                 * @return The initial platforms builder.
                 */
                public PlatformsBuilder or() {
                    return PlatformsBuilder.this;
                }
            }
        }

        /**
         * Builds the sorting term and the sorting direction.
         *
         */
        public class SortingTermBuilder {
            /**
             * Constructor of SortingTermBuilder.
             *
             */
            private SortingTermBuilder() {
                // Nothing to do.
            }

            /**
             * Sets the property to sort by.
             *
             * @param term
             *            The property to sort by.
             * @return Continue with building.
             */
            public SortingDirectionBuilder by(final String term) {
                GameQueryBuilder.this.sortingTerm = term;

                return new SortingDirectionBuilder();
            }

            /**
             * Builds the sorting direction.
             *
             */
            public class SortingDirectionBuilder {
                /**
                 * Constructor of SortingDirectionBuilder.
                 *
                 */
                private SortingDirectionBuilder() {
                    // Nothing to do.
                }

                /**
                 * Sets the sorting direction to ascending.
                 *
                 * @return Continue with building.
                 */
                public AndBuilder ascending() {
                    GameQueryBuilder.this.sortingDirection = Direction.ASCENDING;

                    return new AndBuilder();
                }

                /**
                 * Sets the sorting direction to ascending.
                 *
                 * @return Continue with building.
                 */
                public AndBuilder asc() {
                    return this.ascending();
                }

                /**
                 * Sets the sorting direction to descending.
                 *
                 * @return Continue with building.
                 */
                public AndBuilder descending() {
                    GameQueryBuilder.this.sortingDirection = Direction.DESCENDING;

                    return new AndBuilder();
                }

                /**
                 * Sets the sorting direction to descending.
                 *
                 * @return Continue with building.
                 */
                public AndBuilder desc() {
                    return this.descending();
                }
            }
        }

        /**
         * Links all builds.
         *
         */
        public class AndBuilder {
            /**
             * Constructor of AndBuilder.
             *
             */
            private AndBuilder() {
                // Nothing to do,
            }

            /**
             * Continues with building.
             *
             * @return The initial game query builder.
             */
            public GameQueryBuilder and() {
                return GameQueryBuilder.this;
            }

            /**
             * Initializes the sorting builder.
             *
             * @return The sorting builder.
             */
            public SortingTermBuilder sort() {
                return GameQueryBuilder.this.sort();
            }

            /**
             * Builds the {@link GameQuery}.
             *
             * @return The built {@link GameQuery}.
             */
            public GameQuery build() {
                return GameQueryBuilder.this.build();
            }
        }
    }
}
