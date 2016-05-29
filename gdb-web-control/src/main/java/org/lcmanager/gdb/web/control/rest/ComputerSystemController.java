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

import org.lcmanager.gdb.service.cs.ComputerSystemService;
import org.lcmanager.gdb.service.data.model.ComputerSystem;
import org.lcmanager.gdb.service.user.UserService;
import org.lcmanager.gdb.web.control.status.GenerateStatus;
import org.lcmanager.gdb.web.control.util.ControllerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/computer-system")
@CacheConfig(cacheNames = "computer-system-controller")
@GenerateStatus
public class ComputerSystemController {
    /**
     * The {@link UserService}.
     * 
     */
    @Autowired
    private UserService userService;
    /**
     * The {@link ComputerSystemService}.
     * 
     */
    @Autowired
    private ComputerSystemService computerSystemService;

    @RequestMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResourceSupport> handleFind() {
        final Resources<ComputerSystem> resource = ControllerUtil
                .createResources(this.computerSystemService.retrieveComputerSystems(this.userService.retrieveUser()));

        resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(ComputerSystemController.class).handleFind())
                .withSelfRel());

        return ControllerUtil.createResponse(resource);
    }

    @RequestMapping("/primary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResourceSupport> handleFindPrimary() {
        final Resource<ComputerSystem> resource = ControllerUtil
                .createResource(this.computerSystemService.retrievePrimaryComputerSystem(this.userService.retrieveUser()));

        resource.add(ControllerLinkBuilder
                .linkTo(ControllerLinkBuilder.methodOn(ComputerSystemController.class).handleFindPrimary()).withSelfRel());

        return ControllerUtil.createResponse(resource);
    }
}
