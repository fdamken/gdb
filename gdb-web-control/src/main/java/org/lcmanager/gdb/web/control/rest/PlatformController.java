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

import org.lcmanager.gdb.service.data.util.OsFamily;
import org.lcmanager.gdb.service.platform.PlatformService;
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
 * Handles the interaction with the platform REST API.
 *
 */
@RestController
@RequestMapping("/api/platform")
@GenerateStatus
@CacheConfig(cacheNames = "platform-controller")
public class PlatformController {
    /**
     * The {@link PlatformService}.
     * 
     */
    @Autowired
    private PlatformService platformService;

    /**
     * Finds all available platforms.
     *
     * @return All available platforms.
     */
    @RequestMapping
    @Cacheable
    public ResponseEntity<ResourceSupport> handleFind() {
        final Resources<OsFamily> resource = ControllerUtil.createResources(this.platformService.retrievePlatforms());

        resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(PlatformController.class).handleFind())
                .withSelfRel());

        return ControllerUtil.createResponse(resource);
    }
}
