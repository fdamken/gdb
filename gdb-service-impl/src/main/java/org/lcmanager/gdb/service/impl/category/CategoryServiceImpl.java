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
package org.lcmanager.gdb.service.impl.category;

import java.util.HashSet;
import java.util.Set;

import org.lcmanager.gdb.service.category.CategoryService;
import org.lcmanager.gdb.service.data.model.Category;
import org.lcmanager.gdb.service.impl.data.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link CategoryService}.
 *
 */
@Service
@CacheConfig(cacheNames = "category-service")
public class CategoryServiceImpl implements CategoryService {
    /**
     * The {@link CategoryMapper}.
     * 
     */
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.category.CategoryService#retrieveCategories()
     */
    @Override
    @Cacheable
    public Set<Category> retrieveCategories() {
        return new HashSet<>(this.categoryMapper.find());
    }
}
