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

import lombok.experimental.UtilityClass;

import org.lcmanager.gdb.web.control.util.exception.NullContentException;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Provides utility methods for controllers and REST controllers.
 *
 */
@UtilityClass
public class ControllerUtil {
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
     * Creates a {@link ResponseEntity} by wrapping the given {@link Resource
     * resource}.
     *
     * @param resource
     *            The {@link Resource} to wrap.
     * @return The wrapped resource with the HTTP status {@link HttpStatus#OK
     *         200: OK}.
     */
    public <T> ResponseEntity<Resource<T>> createResponse(final Resource<T> resource) {
        return ResponseEntity.ok(resource);
    }
}
