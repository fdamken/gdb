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

import org.lcmanager.gdb.service.data.model.ComputerSystem;
import org.lcmanager.gdb.service.data.model.Requirement;

public class RequirementComparator implements Comparator<RequirementCompareResult> {
    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.requirement.Comparator#compare(org.lcmanager.gdb.service.data.model.Requirement,
     *      org.lcmanager.gdb.service.data.model.ComputerSystem)
     */
    @Override
    public RequirementCompareResult compare(final Requirement requirement, final ComputerSystem computerSystem) {
        final RequirementCompareResult.RequirementCompareResultBuilder builder = RequirementCompareResult.builder();

        builder.processorCompareResult(new ProcessorComparator().compare(requirement, computerSystem));

        return builder.build();
    }
}
