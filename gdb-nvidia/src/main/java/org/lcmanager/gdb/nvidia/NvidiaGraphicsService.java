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
package org.lcmanager.gdb.nvidia;

import org.lcmanager.gdb.service.annotation.Branded;
import org.lcmanager.gdb.service.data.model.Brand;
import org.lcmanager.gdb.service.data.model.Brand.WellKnownBrand;
import org.lcmanager.gdb.service.data.model.Graphics;
import org.lcmanager.gdb.service.graphics.GraphicsService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * The Nvidia implementation of {@link GraphicsService}.
 * 
 */
@Service
@Branded
@CacheConfig(cacheNames = "nvidia-graphics-service")
public class NvidiaGraphicsService implements GraphicsService {
    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.graphics.GraphicsService#retrieveGraphics(org.lcmanager.gdb.service.data.model.Brand,
     *      java.lang.String)
     */
    @Override
    @Cacheable
    public Graphics retrieveGraphics(final Brand brand, final String model) {
        // TODO Auto-generated method body.
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.graphics.GraphicsService#isResponsible(org.lcmanager.gdb.service.data.model.Brand)
     */
    @Override
    public boolean isResponsible(final Brand brand) {
        return WellKnownBrand.NVIDIA.getBrand().equals(brand);
    }
}
