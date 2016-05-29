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

import org.lcmanager.gdb.base.MathUtil;
import org.lcmanager.gdb.service.data.model.ComputerSystem;
import org.lcmanager.gdb.service.data.model.Requirement;

/**
 * Compares the basic properties of a {@link Requirement} and a
 * {@link ComputerSystem}.
 *
 */
public class BasicComparator implements Comparator<BasicCompareResult> {
    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.requirement.Comparator#compare(org.lcmanager.gdb.service.data.model.Requirement,
     *      org.lcmanager.gdb.service.data.model.ComputerSystem)
     */
    @Override
    public BasicCompareResult compare(final Requirement requirement, final ComputerSystem computerSystem) {
        final BasicCompareResult.BasicCompareResultBuilder builder = BasicCompareResult.builder();

        final boolean osFamilyMatch = requirement.getOsFamily().equals(computerSystem.getOsFamily());
        builder.osFamilyScorePercentage(osFamilyMatch ? 100 : 0);

        builder.memoryScorePercentage(MathUtil.calulatePercentage(requirement.getMemory(), computerSystem.getMemory()));

        return builder.build();
    }
}
