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

import org.lcmanager.gdb.service.annotation.Generic;
import org.lcmanager.gdb.service.data.model.Brand;
import org.lcmanager.gdb.service.data.model.Graphics;
import org.lcmanager.gdb.service.graphics.GraphicsService;
import org.lcmanager.gdb.service.graphics.exception.GraphicsServiceException;
import org.lcmanager.gdb.service.impl.data.mapper.BrandMapper;
import org.lcmanager.gdb.service.impl.data.mapper.GraphicsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A generic implementation of {@link GraphicsService} that accesses the
 * database.
 *
 */
@Service("dbGraphicsService")
@Generic
@CacheConfig(cacheNames = "db-graphics-service")
public class DatabaseGraphicsService implements GraphicsService {
    /**
     * The {@link GraphicsMapper}.
     * 
     */
    @Autowired
    private GraphicsMapper graphicsMapper;
    /**
     * The {@link BrandMapper}.
     * 
     */
    @Autowired
    private BrandMapper brandMapper;

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.graphics.GraphicsService#retrieveGraphics(org.lcmanager.gdb.service.data.model.Brand,
     *      java.lang.String)
     */
    @Override
    public Graphics retrieveGraphics(final Brand brand, final String model) throws GraphicsServiceException {
        if (!this.graphicsMapper.existsBrandModel(brand, model)) {
            return null;
        }
        return this.graphicsMapper.findByBrandAndModel(brand, model);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.graphics.GraphicsService#isResponsible(org.lcmanager.gdb.service.data.model.Brand)
     */
    @Override
    public boolean isResponsible(final Brand brand) {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.graphics.GraphicsService#save(org.lcmanager.gdb.service.data.model.Graphics)
     */
    @Override
    @Transactional
    public void save(final Graphics graphics) {
        if (graphics.getId() != null && this.graphicsMapper.exists(graphics.getId())) {
            return;
        }

        final Brand brand = graphics.getBrand();
        if (this.brandMapper.existsName(brand.getName())) {
            brand.setId(this.brandMapper.findByName(brand.getName()).getId());
        } else {
            this.brandMapper.insert(brand);
        }

        this.graphicsMapper.insert(graphics);
    }
}
