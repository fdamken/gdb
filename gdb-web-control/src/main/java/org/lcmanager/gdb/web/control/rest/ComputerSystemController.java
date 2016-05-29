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

import java.util.Map;

import org.lcmanager.gdb.base.exception.GdbException;
import org.lcmanager.gdb.service.cs.ComputerSystemService;
import org.lcmanager.gdb.service.data.model.Brand;
import org.lcmanager.gdb.service.data.model.Brand.WellKnownBrand;
import org.lcmanager.gdb.service.data.model.ComputerSystem;
import org.lcmanager.gdb.service.data.util.OsFamily;
import org.lcmanager.gdb.service.exception.ServiceException;
import org.lcmanager.gdb.service.graphics.GraphicsService;
import org.lcmanager.gdb.service.processor.ProcessorService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles the interaction with the computer system REST API.
 *
 */
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
    /**
     * The {@link ProcessorService}.
     * 
     */
    @Autowired
    private ProcessorService processorService;
    /**
     * The {@link GraphicsService}.
     * 
     */
    @Autowired
    private GraphicsService graphicsService;

    /**
     * Finds all computer systems of the current user.
     *
     * @return All computer systems of the current user.
     */
    @RequestMapping
    public ResponseEntity<ResourceSupport> handleFind() {
        final Resources<ComputerSystem> resource = ControllerUtil
                .createResources(this.computerSystemService.retrieveComputerSystems(this.userService.retrieveUser()));

        resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(ComputerSystemController.class).handleFind())
                .withSelfRel());

        return ControllerUtil.createResponse(resource);
    }

    /**
     * Creates the computer system represented by the request body for the
     * current user.
     *
     * @param requestBody
     *            The data that represents the computer system to create.
     * @throws GdbException
     *             If any error occurs.
     */
    @RequestMapping(method = RequestMethod.PUT)
    public void createComputerSystem(@RequestBody final Map<String, Object> requestBody) throws GdbException {
        this.computerSystemService.addComputerSystem(this.userService.retrieveUser(), this.makeComputerSystem(requestBody));
    }

    /**
     * Patches the computer system with the given ID of the current user by
     * copying the data from the request body by using
     * {@link #makeComputerSystem(int, Map)}.
     *
     * @param id
     *            The ID of the computer to patch.
     * @param requestBody
     *            The data to patch.
     * @throws GdbException
     *             If any error occurs.
     */
    @RequestMapping(path = "/{id}",
                    method = RequestMethod.PATCH)
    public void modifyComputerSystem(@PathVariable final int id, @RequestBody final Map<String, Object> requestBody)
            throws GdbException {
        this.computerSystemService.updateComputerSystem(this.userService.retrieveUser(),
                this.makeComputerSystem(id, requestBody));
    }

    /**
     * Deletes the computer system with the given ID of the current user.
     *
     * @param id
     *            The ID of the computer system to delete.
     */
    @RequestMapping(path = "/{id}",
                    method = RequestMethod.DELETE)
    public void deleteComputerSystem(@PathVariable final int id) {
        this.computerSystemService.deleteComputerSystem(this.userService.retrieveUser(), id);
    }

    /**
     * Finds the primary computer system of the current user.
     *
     * @return The primary computer system of the current user.
     */
    @RequestMapping("/primary")
    public ResponseEntity<ResourceSupport> handleFindPrimary() {
        final Resource<ComputerSystem> resource = ControllerUtil
                .createResource(this.computerSystemService.retrievePrimaryComputerSystem(this.userService.retrieveUser()));

        resource.add(ControllerLinkBuilder
                .linkTo(ControllerLinkBuilder.methodOn(ComputerSystemController.class).handleFindPrimary()).withSelfRel());

        return ControllerUtil.createResponse(resource);
    }

    /**
     * Creates a {@link ComputerSystem} by using the given data.
     * 
     * <p>
     * The following data must be present in the given map:
     * <ul>
     * <li>description: {@link String}</li>
     * <li>memory: <code>int</code></li>
     * <li>primary: <code>boolean</code></li>
     * <li>osFamily: {@link String}</li>
     * <li>processorBrand: {@link String}</li>
     * <li>processorModel: {@link String}</li>
     * <li>graphicsBrand: {@link String}</li>
     * <li>graphicsModel: {@link String}</li>
     * </ul>
     * </p>
     *
     * @param data
     *            The data to create a computer system of.
     * @return The created computer system.
     * @throws ServiceException
     *             If any error occurs.
     */
    private ComputerSystem makeComputerSystem(final Map<String, Object> data) throws ServiceException {
        final String description = (String) data.get("description");
        final int memory = (int) data.get("memory");
        final boolean primary = (boolean) data.get("primary");
        final OsFamily osFamily = OsFamily.valueOf((String) data.get("osFamily"));
        final Brand processorBrand = WellKnownBrand.valueOf((String) data.get("processorBrand")).getBrand();
        final String processorModel = (String) data.get("processorModel");
        final Brand graphicsBrand = WellKnownBrand.valueOf((String) data.get("graphicsBrand")).getBrand();
        final String graphicsModel = (String) data.get("graphicsModel");

        final ComputerSystem computerSystem = new ComputerSystem();
        computerSystem.setDescription(description);
        computerSystem.setMemory(memory);
        computerSystem.setPrimary(primary);
        computerSystem.setOsFamily(osFamily);
        computerSystem.setProcessor(this.processorService.retrieveProcessor(processorBrand, processorModel));
        computerSystem.setGraphics(this.graphicsService.retrieveGraphics(graphicsBrand, graphicsModel));
        return computerSystem;
    }

    /**
     * Invokes {@link #makeComputerSystem(Map)} and sets the ID of the created
     * computer system.
     *
     * @param id
     *            The ID to set.
     * @param data
     *            The data to be passed to {@link #makeComputerSystem(Map)}.
     * @return The created {@link ComputerSystem} with the set ID.
     * @throws ServiceException
     *             If any error occurs.
     */
    private ComputerSystem makeComputerSystem(final int id, final Map<String, Object> data) throws ServiceException {
        return this.makeComputerSystem(data).setId(id);
    }
}
