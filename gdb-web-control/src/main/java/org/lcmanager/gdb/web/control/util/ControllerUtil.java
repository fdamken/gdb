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
package org.lcmanager.gdb.web.control.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.lcmanager.gdb.base.PaginationMetadata;
import org.lcmanager.gdb.web.control.util.exception.NullContentException;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.experimental.UtilityClass;

/**
 * Provides utility methods for controllers and REST controllers.
 *
 */
@UtilityClass
public class ControllerUtil {
    /**
     * Creates a new map and passes it to each of the given consumers to enable
     * them to fill the map. This allows inline-map-creation.
     *
     * @param <K>
     *            The key type.
     * @param <V>
     *            The value type.
     * @param consumer
     *            An array of consumers that are filling the map.
     * @return The created map.
     */
    @SafeVarargs
    public <K, V> Map<K, V> createMap(final Consumer<Map<K, V>>... consumer) {
        final Map<K, V> map = new HashMap<>();
        Arrays.stream(consumer).forEach(c -> c.accept(map));
        return map;
    }

    /**
     * Creates a simple resource for the given content.
     *
     * @param content
     *            The content to set.
     * @return The created resource.
     * @throws NullContentException
     *             If the given content was <code>null</code>.
     */
    public <T> Resource<T> createResource(final T content) {
        if (content == null) {
            throw new NullContentException();
        }

        return new Resource<>(content);
    }

    /**
     * Creates a resource list ({@link Resources}) for the given content.
     *
     * @param content
     *            The content to set.
     * @return The creates resources.
     * @throws NullContentException
     *             If the given content was <code>null</code>.
     */
    public <T> Resources<T> createResources(final Collection<T> content) {
        if (content == null) {
            throw new NullContentException();
        }

        return new Resources<>(content);
    }

    /**
     * Creates a resource list ({@link PagedResources}) for the given content.
     *
     * @param content
     *            The content to set.
     * @param paginationMetadata
     *            The pagination meta-data to apply.
     * @return The creates resources.
     * @throws NullContentException
     *             If the given content was <code>null</code>.
     */
    public <T> PagedResources<T> createResources(final Collection<T> content, final PaginationMetadata paginationMetadata) {
        if (content == null) {
            throw new NullContentException();
        }

        final PageMetadata metadata = new PageMetadata(paginationMetadata.getPageSize(), paginationMetadata.getPage(),
                paginationMetadata.getTotalItems(), paginationMetadata.getTotalPages());

        return new PagedResources<>(content, metadata);
    }

    /**
     * Creates a {@link ResponseEntity} by wrapping the given
     * {@link ResourceSupport resource}.
     *
     * @param resource
     *            The {@link ResourceSupport resource} to wrap.
     * @return The wrapped resource with the HTTP status {@link HttpStatus#OK
     *         200: OK}.
     */
    public ResponseEntity<ResourceSupport> createResponse(final ResourceSupport resource) {
        return ResponseEntity.ok(resource);
    }
}
