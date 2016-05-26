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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.lcmanager.gdb.base.MathUtil;
import org.lcmanager.gdb.service.data.model.Brand;
import org.lcmanager.gdb.service.data.model.Brand.WellKnownBrand;
import org.lcmanager.gdb.service.data.model.ComputerSystem;
import org.lcmanager.gdb.service.data.model.Processor;
import org.lcmanager.gdb.service.data.model.Requirement;
import org.lcmanager.gdb.service.requirement.ProcessorCompareResult.ProcessorCompareResultBuilder;

public class ProcessorComparator implements Comparator<ProcessorCompareResult> {
    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.requirement.Comparator#compare(org.lcmanager.gdb.service.data.model.Requirement,
     *      org.lcmanager.gdb.service.data.model.ComputerSystem)
     */
    @Override
    public ProcessorCompareResult compare(final Requirement requirement, final ComputerSystem computerSystem) {
        final Processor processor = computerSystem.getProcessor();
        final Brand bestMatchingBrand = this.getBestMatchingBrand(processor.getBrand(), requirement.getProcessors().keySet());
        final Processor requiredProcessor = requirement.getProcessors().get(bestMatchingBrand);

        final ProcessorCompareResultBuilder builder = ProcessorCompareResult.builder();

        builder.coreScorePercentage(MathUtil.calulatePercentage(requiredProcessor.getCores(), processor.getCores()));
        builder.frequencyScorePercentage(MathUtil.calulatePercentage(requiredProcessor.getFrequency(), processor.getFrequency()));
        final int instructionSetScorePercentage;
        if (requiredProcessor.getInstructionSet() > processor.getInstructionSet()) {
            instructionSetScorePercentage = 0;
        } else {
            instructionSetScorePercentage = MathUtil.calulatePercentage(requiredProcessor.getInstructionSet(),
                    processor.getInstructionSet());
        }
        builder.instructionSetScorePercentage(instructionSetScorePercentage);
        builder.threadsScorePercentage(MathUtil.calulatePercentage(requiredProcessor.getThreads(), processor.getThreads()));

        return builder.build();
    }

    private Brand getBestMatchingBrand(final Brand brand, final Collection<Brand> brandCollection) {
        final List<Brand> brands = new ArrayList<>(brandCollection);

        if (brands.contains(brands)) {
            return brand;
        }

        if (!WellKnownBrand.isWellKnownBrand(brand)) {
            return brands.get(0);
        }

        final WellKnownBrand wellKnownBrand = WellKnownBrand.getWellKnownBrand(brand);
        final List<WellKnownBrand> wellKnownBrands = brands.stream() //
                .filter(WellKnownBrand::isWellKnownBrand) //
                .map(WellKnownBrand::getWellKnownBrand) //
                .collect(Collectors.toList());

        final Brand result;
        if (wellKnownBrand == WellKnownBrand.AMD && wellKnownBrands.contains(WellKnownBrand.INTEL)) {
            result = WellKnownBrand.INTEL.getBrand();
        } else if (wellKnownBrand == WellKnownBrand.INTEL && wellKnownBrands.contains(WellKnownBrand.AMD)) {
            result = WellKnownBrand.AMD.getBrand();
        } else if (brands.contains(null)) {
            result = null;
        } else {
            result = brands.get(0);
        }
        return result;
    }
}
