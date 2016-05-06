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
package org.lcmanager.gdb.service.impl.processor;

import java.util.List;
import java.util.stream.Stream;

import org.lcmanager.gdb.base.StreamUtil;
import org.lcmanager.gdb.service.annotation.Branded;
import org.lcmanager.gdb.service.annotation.Generic;
import org.lcmanager.gdb.service.data.model.Brand;
import org.lcmanager.gdb.service.data.model.Processor;
import org.lcmanager.gdb.service.processor.ProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * An implementation of {@link ProcessorService} that delegates to the branded
 * processor services.
 *
 */
@Service
@Generic
@Primary
@CacheConfig(cacheNames = "delegating-processor-service")
public class DelegatingProcessorService implements ProcessorService {
    /**
     * All branded {@link ProcessorService}.
     * 
     */
    @Autowired(required = false)
    @Branded
    private List<ProcessorService> processorServices;

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.processor.ProcessorService#retrieveProcessor(org.lcmanager.gdb.service.data.model.Brand,
     *      java.lang.String)
     */
    @Override
    @Cacheable
    public Processor retrieveProcessor(final Brand brand, final String model) {
        if (!this.isResponsible(brand)) {
            throw new UnsupportedOperationException("Brand " + brand + " is not supported!");
        }

        return this.filterResponsibleServices(brand).findAny().get().retrieveProcessor(brand, model);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.processor.ProcessorService#isResponsible(org.lcmanager.gdb.service.data.model.Brand)
     */
    @Override
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
    private Stream<ProcessorService> filterResponsibleServices(final Brand brand) {
        return this.processorServices.stream().filter(processorService -> processorService.isResponsible(brand));
    }
}
