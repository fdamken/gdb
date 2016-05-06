/*
 * #%L
 * Bank Account Manager
 * %%
 * Copyright (C) 2016 Fabian Damken
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
package org.lcmanager.gdb.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a pagination and manages the item splitting, counting and so on.
 *
 * @param <T>
 *            The item type.
 */
@Getter
public class Pagination<T> {
    /**
     * All, unpaginated, items.
     * 
     */
    private final List<T> allItems;
    /**
     * The size of every page (except for the last, of course).
     * 
     */
    private final int pageSize;

    /**
     * The index of the last item.
     * 
     */
    private final int lastItem;
    /**
     * The index of the last page.
     * 
     */
    private final int lastPage;

    /**
     * The strategy that should be applied if any error occurs with the
     * arguments (e.g. the page index is out of range).
     * 
     * <p>
     * Defaults to {@link ArgumentErrorStrategy#IGNORE}.
     * </p>
     * 
     */
    @Setter
    private ArgumentErrorStrategy argumentErrorStrategy = ArgumentErrorStrategy.IGNORE;

    /**
     * The items that are present on the current page.
     * 
     */
    private List<T> items;
    /**
     * The current page.
     * 
     */
    private int page;

    /**
     * Whether the {@link #items} are dirty and must be refreshed when another
     * object accesses them via {@link #getItems()}.
     * 
     */
    private boolean dirty;

    /**
     * Constructor of Pagination.
     *
     * @param pageSize
     *            The size of any page. If the size is smaller than one, the
     *            page size is set to infinite.
     * @param allItems
     *            All items that should be paginated. If <code>null</code>, an
     *            empty list is applied.
     */
    public Pagination(final int pageSize, final List<T> allItems) {
        if (pageSize <= 0) {
            this.pageSize = -1;
        } else {
            this.pageSize = pageSize;
        }
        if (allItems == null) {
            this.allItems = Collections.emptyList();
        } else {
            this.allItems = Collections.unmodifiableList(allItems);
        }

        this.lastItem = this.allItems.size() - 1;
        if (this.pageSize == -1) {
            this.lastPage = -1;
        } else {
            this.lastPage = this.allItems.size() / this.pageSize + (this.allItems.size() % this.pageSize == 0 ? 0 : 1) - 1;
        }
    }

    /**
     * Constructor of Pagination.
     *
     * @param pageSize
     *            The size of any page. Passed to {@link #Pagination(int, List)}
     *            .
     * @param allItems
     *            All items that should be paginated. Passed to
     *            {@link #Pagination(int, List)}.
     */
    @SafeVarargs
    public Pagination(final int pageSize, final T... allItems) {
        this(pageSize, allItems == null ? null : Arrays.asList(allItems));
    }

    /**
     * Fetches the current items and instaniates {@link #items}.
     *
     * @return All items of the current page.
     */
    public List<T> getItems() {
        if (this.dirty) {
            if (this.pageSize == -1) {
                this.items = this.allItems;
            } else if (this.page == -1) {
                this.items = Collections.emptyList();
            } else {
                final int start = this.page * this.pageSize;
                final int end = Math.min((this.page + 1) * this.pageSize - 1, this.lastItem) + 1;

                final List<T> newItems = new ArrayList<>();
                for (int i = start; i < end; i++) {
                    newItems.add(this.allItems.get(i));
                }

                this.items = Collections.unmodifiableList(newItems);
            }
        }

        return this.items;
    }

    /**
     *
     * @return The size of the current page.
     */
    public int getSize() {
        return this.getItems().size();
    }

    /**
     *
     * @return The total size of this pagination.
     */
    public int getTotalSize() {
        return this.allItems.size();
    }

    /**
     *
     * @return The total count of pages.
     */
    public int getTotalPages() {
        return this.lastPage + 1;
    }

    /**
     * Jumps to the given page. If the given page is out of bounds or smaller
     * than zero, the {@link #argumentErrorStrategy} is applied. If the strategy
     * is set to {@link ArgumentErrorStrategy#IGNORE}, the page jumps to
     * <code>-1</code> and therefor no items will be returned on a
     * {@link #getItems()} invocation.
     *
     * @param page
     *            The page to jump to.
     */
    public void jumpToPage(final int page) {
        boolean pageInvalid = false;
        if (page < 0) {
            this.argumentErrorStrategy.apply("Page must be at least one!");

            pageInvalid = true;
        }
        if (page > this.lastPage) {
            this.argumentErrorStrategy.apply("Page does not exist! The last page is page " + this.lastPage + ".");

            pageInvalid = true;
        }

        this.page = pageInvalid ? -1 : page;
        this.dirty = true;
    }

    /**
     * Jumps to the next page by invoking {@link #jumpToPage(int)}.
     *
     */
    public void jumpToNextPage() {
        this.jumpToPage(this.page + 1);
    }

    /**
     * Jumps to the previous page by invoking {@link #jumpToPage(int)}.
     *
     */
    public void jumpToPreviousPage() {
        this.jumpToPage(this.page - 1);
    }

    /**
     * The argument error strategy can be set to handle argument errors
     * differently.
     *
     */
    public static enum ArgumentErrorStrategy {
        /**
         * Ignore all argument errors and continou with the default value.
         * 
         */
        IGNORE {
            /**
             * {@inheritDoc}
             *
             * @see org.lcmanager.gdb.base.Pagination.ArgumentErrorStrategy#apply(java.lang.String)
             */
            @Override
            public void apply(final String msg) {
                // Nothing to do.
            }
        },
        /**
         * Throw an exception on every argument error.
         * 
         */
        EXCEPTION {
            /**
             * {@inheritDoc}
             *
             * @see org.lcmanager.gdb.base.Pagination.ArgumentErrorStrategy#apply(java.lang.String)
             */
            @Override
            public void apply(final String msg) {
                throw new IllegalArgumentException(msg);
            }
        };

        /**
         * Executes this strategy.
         *
         * @param msg
         *            The message that is used when throwing exceptions.
         */
        public abstract void apply(final String msg);
    }
}
