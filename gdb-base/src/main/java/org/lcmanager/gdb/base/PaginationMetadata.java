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
package org.lcmanager.gdb.base;

import lombok.Value;

/**
 * Represents the meta-data about any pagination.
 *
 */
@Value
public class PaginationMetadata {
    /**
     * Represents an empty pagination meta-data. That is on page <code>1</code>
     * with with a total items of <code>0</code>.
     * 
     */
    public static final PaginationMetadata EMPTY = new PaginationMetadata(1, 0, 0, 1);

    /**
     * The number of the current page.
     * 
     */
    int page;
    /**
     * The size of all pages.
     * 
     */
    int pageSize;
    /**
     * The total count of the items.
     * 
     * <p>
     * All values below zero represent that no value was set.
     * </p>
     * 
     */
    int totalItems;
    /**
     * The total count of the pages.
     * 
     * <p>
     * All values below zero represent that no value was set.
     * </p>
     * 
     */
    int totalPages;

    /**
     * Constructor of PaginationMetadata.
     *
     * @param page
     *            The {@link #page} to set.
     * @param pageSize
     *            The {@link #pageSize} to set.
     * @param totalItems
     *            The {@link #totalItems} to set.
     * @param totalPages
     *            The {@link #totalPages} to set.
     */
    public PaginationMetadata(final int page, final int pageSize, final int totalItems, final int totalPages) {
        if (page < 1) {
            throw new IllegalArgumentException("Page must not be lower than one!");
        }
        if (pageSize < 0) {
            throw new IllegalArgumentException("Page size must not be lower than zero!");
        }

        this.page = page;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }

    /**
     * Constructor of PaginationMetadata.
     *
     * @param page
     *            The {@link #page} to set.
     * @param pageSize
     *            The {@link #pageSize} to set
     */
    public PaginationMetadata(final int page, final int pageSize) {
        this(page, pageSize, -1, -1);
    }
}
