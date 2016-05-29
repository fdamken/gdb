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

import org.lcmanager.gdb.service.annotation.Generic;
import org.lcmanager.gdb.service.data.model.Brand;
import org.lcmanager.gdb.service.data.model.Processor;
import org.lcmanager.gdb.service.impl.data.mapper.BrandMapper;
import org.lcmanager.gdb.service.impl.data.mapper.ProcessorMapper;
import org.lcmanager.gdb.service.processor.ProcessorService;
import org.lcmanager.gdb.service.processor.exception.ProcessorServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A generic implementation of {@link ProcessorService} that accesses the
 * database.
 *
 */
@Service("dbProcessorService")
@Generic
@CacheConfig(cacheNames = "db-processor-service")
public class DatabaseProcessorService implements ProcessorService {
    /**
     * The {@link ProcessorMapper}.
     * 
     */
    @Autowired
    private ProcessorMapper processorMapper;
    /**
     * The {@link BrandMapper}.
     * 
     */
    @Autowired
    private BrandMapper brandMapper;

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.processor.ProcessorService#retrieveProcessor(org.lcmanager.gdb.service.data.model.Brand,
     *      java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable
    public Processor retrieveProcessor(final Brand brand, final String model) throws ProcessorServiceException {
        if (!this.processorMapper.existsBrandModel(brand, model)) {
            return null;
        }
        return this.processorMapper.findByBrandAndModel(brand, model);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.processor.ProcessorService#isResponsible(org.lcmanager.gdb.service.data.model.Brand)
     */
    @Override
    public boolean isResponsible(final Brand brand) {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.processor.ProcessorService#save(org.lcmanager.gdb.service.data.model.Processor)
     */
    @Override
    @Transactional
    public void save(final Processor processor) {
        if (processor == null || processor.getId() != null && this.processorMapper.exists(processor.getId())) {
            return;
        }

        processor.setModel(processor.getModel().toLowerCase());

        final Brand brand = processor.getBrand();
        if (this.brandMapper.existsName(brand.getName())) {
            brand.setId(this.brandMapper.findByName(brand.getName()).getId());
        } else {
            this.brandMapper.insert(brand);
        }

        this.processorMapper.insert(processor);
    }
}
