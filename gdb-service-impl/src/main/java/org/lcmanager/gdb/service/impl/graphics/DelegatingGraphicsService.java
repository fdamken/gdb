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
package org.lcmanager.gdb.service.impl.graphics;

import java.util.List;
import java.util.stream.Stream;

import org.lcmanager.gdb.base.StreamUtil;
import org.lcmanager.gdb.base.health.NoHealthTrace;
import org.lcmanager.gdb.service.annotation.Branded;
import org.lcmanager.gdb.service.annotation.Generic;
import org.lcmanager.gdb.service.data.model.Brand;
import org.lcmanager.gdb.service.data.model.Graphics;
import org.lcmanager.gdb.service.graphics.GraphicsService;
import org.lcmanager.gdb.service.graphics.exception.GraphicsServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * An implementation of {@link GraphicsService} that delegates to the branded
 * graphics services.
 *
 */
@Service
@Generic
@Primary
@CacheConfig(cacheNames = "delegating-graphics-service")
public class DelegatingGraphicsService implements GraphicsService {
    /**
     * All branded {@link GraphicsService}.
     * 
     */
    @Autowired(required = false)
    @Branded
    private List<GraphicsService> processorServices;

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.graphics.GraphicsService#retrieveGraphics(org.lcmanager.gdb.service.data.model.Brand,
     *      java.lang.String)
     */
    @Override
    @Cacheable
    public Graphics retrieveGraphics(final Brand brand, final String model) throws GraphicsServiceException {
        if (!this.isResponsible(brand)) {
            throw new UnsupportedOperationException("Brand " + brand + " is not supported!");
        }

        return this.filterResponsibleServices(brand).findAny().get().retrieveGraphics(brand, model);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.graphics.GraphicsService#isResponsible(org.lcmanager.gdb.service.data.model.Brand)
     */
    @Override
    @NoHealthTrace
    public boolean isResponsible(final Brand brand) {
        return !StreamUtil.isEmpty(this.filterResponsibleServices(brand));
    }

    /**
     * Finds all services that are responsible for the given brand and returns
     * them as a {@link Stream}.
     *
     * @param brand
     *            The brand to find all responsible services for.
     * @return A stream of responsible services.
     */
    private Stream<GraphicsService> filterResponsibleServices(final Brand brand) {
        return this.processorServices.stream().filter(processorService -> processorService.isResponsible(brand));
    }
}
