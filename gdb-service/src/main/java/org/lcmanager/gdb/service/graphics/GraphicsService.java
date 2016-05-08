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
package org.lcmanager.gdb.service.graphics;

import org.lcmanager.gdb.service.data.model.Brand;
import org.lcmanager.gdb.service.data.model.Graphics;
import org.lcmanager.gdb.service.graphics.exception.GraphicsServiceException;

/**
 * The graphics service is used to interaction with graphics cards (e.g. load
 * them from 3rd party sources).
 *
 */
public interface GraphicsService {
    /**
     * Retrieves the graphics card with the given model identifier of the given
     * {@link Brand}.
     *
     * @param brand
     *            The {@link Brand} of the graphics card.
     * @param model
     *            The model identifier of the graphics card.
     * @return The retrieved graphics card.
     * @throws GraphicsServiceException
     *             If an error occurs whilst retrieving the graphics card.
     */
    Graphics retrieveGraphics(Brand brand, String model) throws GraphicsServiceException;

    /**
     * Checks whether the implementing class is responsible for the given brand.
     *
     * @param brand
     *            The brand to check the responsibility for.
     * @return Whether this service is responsible for the given brand.
     */
    boolean isResponsible(Brand brand);
}
