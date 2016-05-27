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

import lombok.Builder;
import lombok.Data;

/**
 * Represents the result of a comparison executed by the
 * {@link RequirementComparator}.
 *
 */
@Data
@Builder
public class RequirementCompareResult implements CompareResult {
    /**
     * The compare result of the basic criteria of a system.
     * 
     * <p>
     * NOTE: This is a KO-criteria!
     * </p>
     * 
     */
    private final BasicCompareResult basicCompareResult;
    /**
     * The compare result of the processor.
     * 
     * <p>
     * NOTE: This is a KO-criteria!
     * </p>
     * 
     */
    private final ProcessorCompareResult processorCompareResult;
    /**
     * The compare result of the graphics card.
     * 
     * <p>
     * NOTE: This is a KO-criteria!
     * </p>
     * 
     */
    private final GraphicsCompareResult graphicsCompareResult;

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.requirement.CompareResult#getPercentage()
     */
    @Override
    public int getPercentage() {
        if (this.basicCompareResult.getPercentage() == 0 || this.processorCompareResult.getPercentage() == 0
                || this.graphicsCompareResult.getPercentage() == 0) {
            return 0;
        }

        return MathUtil.average(CompareResult::getPercentage, this.basicCompareResult, this.processorCompareResult,
                this.graphicsCompareResult);
    }
}
