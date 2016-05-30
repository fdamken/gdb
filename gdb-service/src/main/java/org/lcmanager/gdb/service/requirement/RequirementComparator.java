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
package org.lcmanager.gdb.service.requirement;

import org.lcmanager.gdb.service.compare.Comparator;
import org.lcmanager.gdb.service.data.model.ComputerSystem;
import org.lcmanager.gdb.service.data.model.Requirement;

/**
 * Compares a {@link Requirement} with a {@link ComputerSystem}.
 *
 */
public class RequirementComparator implements Comparator<RequirementCompareResult> {
    /**
     * The {@link BasicComparator} used for comparing the basic properties of a
     * requirement.
     * 
     */
    private final BasicComparator basicComparator = new BasicComparator();
    /**
     * The {@link ProcessorComparator} used for comparing processors.
     * 
     */
    private final ProcessorComparator processorComparator = new ProcessorComparator();
    /**
     * The {@link GraphicsComparator} used for comparing graphics cards.
     * 
     */
    private final GraphicsComparator graphicsComparator = new GraphicsComparator();

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.compare.Comparator#compare(org.lcmanager.gdb.service.data.model.Requirement,
     *      org.lcmanager.gdb.service.data.model.ComputerSystem)
     */
    @Override
    public RequirementCompareResult compare(final Requirement requirement, final ComputerSystem computerSystem) {
        final RequirementCompareResult.RequirementCompareResultBuilder builder = RequirementCompareResult.builder();

        builder.basicCompareResult(this.basicComparator.compare(requirement, computerSystem));

        builder.processorCompareResult(this.processorComparator.compare(requirement, computerSystem));

        builder.graphicsCompareResult(this.graphicsComparator.compare(requirement, computerSystem));

        return builder.build();
    }
}
