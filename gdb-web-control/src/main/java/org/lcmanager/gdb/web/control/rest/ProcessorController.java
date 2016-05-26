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

import org.lcmanager.gdb.base.exception.GdbException;
import org.lcmanager.gdb.service.data.model.Brand.WellKnownBrand;
import org.lcmanager.gdb.service.data.model.Processor;
import org.lcmanager.gdb.service.processor.ProcessorService;
import org.lcmanager.gdb.web.control.status.GenerateStatus;
import org.lcmanager.gdb.web.control.util.ControllerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles the interaction with the processor REST API.
 *
 */
@RestController
@RequestMapping("/api/processor")
@GenerateStatus
@CacheConfig(cacheNames = "processor-controller")
public class ProcessorController {
    /**
     * The {@link ProcessorService}.
     * 
     */
    @Autowired
    private ProcessorService processorService;

    /**
     * Retrieves the processor with the given model by the given brand.
     *
     * @param brand
     *            The brand to retrieve the processor from.
     * @param model
     *            The model of the processor to retrieve.
     * @return The retrieved processor.
     * @throws GdbException
     *             If any error occurs.
     */
    @RequestMapping("/{brand}/{model}")
    @Cacheable
    public ResponseEntity<ResourceSupport> handleFind(@PathVariable final WellKnownBrand brand, @PathVariable final String model)
            throws GdbException {
        final Resource<Processor> resource = ControllerUtil
                .createResource(this.processorService.retrieveProcessor(brand.getBrand(), model));

        resource.add(ControllerLinkBuilder
                .linkTo(ControllerLinkBuilder.methodOn(ProcessorController.class).handleFind(brand, model)).withSelfRel());

        return ControllerUtil.createResponse(resource);
    }
}
