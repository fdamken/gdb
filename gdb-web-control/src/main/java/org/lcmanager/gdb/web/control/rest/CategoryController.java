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
package org.lcmanager.gdb.web.control.rest;

import org.lcmanager.gdb.service.category.CategoryService;
import org.lcmanager.gdb.service.data.model.Category;
import org.lcmanager.gdb.web.control.status.GenerateStatus;
import org.lcmanager.gdb.web.control.util.ControllerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles the interaction with the category REST API.
 *
 */
@RestController
@RequestMapping("/api/category")
@GenerateStatus
@CacheConfig(cacheNames = "category-controller")
public class CategoryController {
    /**
     * The {@link CategoryService}.
     * 
     */
    @Autowired
    private CategoryService categoryService;

    /**
     * Finds all available categories.
     *
     * @return All available categories.
     */
    @RequestMapping
    @Cacheable
    public ResponseEntity<ResourceSupport> handleFind() {
        final Resources<Category> resource = ControllerUtil.createResources(this.categoryService.retrieveCategories());

        resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CategoryController.class).handleFind())
                .withSelfRel());

        return ControllerUtil.createResponse(resource);
    }
}
