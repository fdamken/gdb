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
package org.lcmanager.gdb.service.game;

import org.lcmanager.gdb.base.MathUtil;
import org.lcmanager.gdb.service.compare.CompareResult;
import org.lcmanager.gdb.service.requirement.RequirementCompareResult;

import lombok.Builder;
import lombok.Data;

/**
 * The result of a comparison executed by the {@link GameCompareService}.
 *
 */
@Data
@Builder
public class GameCompareResult implements CompareResult {
    /**
     * The result of the comparison of the minimum requirements.
     * 
     */
    private final RequirementCompareResult minimumRequirementCompareResult;
    /**
     * The result of the comparison of the recommended requirements.
     * 
     */
    private final RequirementCompareResult recommendedRequirementCompareResult;

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.compare.CompareResult#getPercentage()
     */
    @Override
    public int getPercentage() {
        if (this.recommendedRequirementCompareResult == null && this.minimumRequirementCompareResult == null) {
            return 0;
        }
        if (this.recommendedRequirementCompareResult == null) {
            return this.minimumRequirementCompareResult.getPercentage();
        }
        if (this.minimumRequirementCompareResult == null) {
            return this.recommendedRequirementCompareResult.getPercentage();
        }
        return MathUtil.average(CompareResult::getPercentage, this.minimumRequirementCompareResult,
                this.recommendedRequirementCompareResult);
    }

    /**
     *
     * @return Whether the minimum requirements where compared.
     */
    public boolean isMinimumCompared() {
        return this.minimumRequirementCompareResult != null;
    }

    /**
     *
     * @return Whether the recommended requirements where compared.
     */
    public boolean isRecommendedCompared() {
        return this.recommendedRequirementCompareResult != null;
    }

    /**
     *
     * @return Whether either the minimum or the recommended requirements where
     *         compared.
     */
    public boolean isCompared() {
        return this.isMinimumCompared() || this.isRecommendedCompared();
    }
}
