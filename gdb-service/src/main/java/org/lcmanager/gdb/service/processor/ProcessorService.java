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
package org.lcmanager.gdb.service.processor;

import org.lcmanager.gdb.service.data.model.Brand;
import org.lcmanager.gdb.service.data.model.Processor;
import org.lcmanager.gdb.service.processor.exception.ProcessorServiceException;

/**
 * The processor service is used to interaction with processors (e.g. load them
 * from 3rd party sources).
 *
 */
public interface ProcessorService {
    /**
     * Retrieves the processor with the given model identifier of the given
     * {@link Brand}.
     *
     * @param brand
     *            The {@link Brand} of the processor.
     * @param model
     *            The model identifier of the processor.
     * @return The retrieved processor.
     * @throws ProcessorServiceException
     *             If any error occurs whilst accessing the processor service.
     */
    Processor retrieveProcessor(Brand brand, String model) throws ProcessorServiceException;

    /**
     * Checks whether the implementing class is responsible for the given brand.
     *
     * @param brand
     *            The brand to check the responsibility for.
     * @return Whether this service is responsible for the given brand.
     */
    boolean isResponsible(Brand brand);
}
