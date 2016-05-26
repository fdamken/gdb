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

import java.util.ArrayList;
import java.util.Arrays;

import org.lcmanager.gdb.base.StreamUtil;
import org.lcmanager.gdb.base.exception.GdbException;
import org.lcmanager.gdb.service.data.model.Brand;
import org.lcmanager.gdb.service.data.model.ComputerSystem;
import org.lcmanager.gdb.service.data.model.Processor;
import org.lcmanager.gdb.service.data.model.Requirement;
import org.lcmanager.gdb.service.processor.ProcessorService;
import org.lcmanager.gdb.service.requirement.RequirementCompareResult;
import org.lcmanager.gdb.service.requirement.RequirementCompareService;
import org.lcmanager.gdb.web.control.status.GenerateStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/requirement")
@GenerateStatus
public class TestController {
    @Autowired
    private RequirementCompareService requirementCompareService;
    @Autowired
    private ProcessorService processorService;

    @RequestMapping
    public RequirementCompareResult handle(@RequestParam final String model1, @RequestParam final String model2)
            throws GdbException {
        return this.requirementCompareService
                .compare(
                        new Requirement().setProcessors(new ArrayList<>(Arrays
                                .asList(this.processorService.retrieveProcessor(Brand.WellKnownBrand.INTEL.getBrand(), model1)))
                                        .stream().collect(StreamUtil.collectMap(Processor::getBrand))),
                        new ComputerSystem().setProcessor(
                                this.processorService.retrieveProcessor(Brand.WellKnownBrand.INTEL.getBrand(), model2)));
    }
}
