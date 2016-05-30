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
package org.lcmanager.gdb.service.impl.game;

import org.lcmanager.gdb.service.data.model.ComputerSystem;
import org.lcmanager.gdb.service.data.model.Game;
import org.lcmanager.gdb.service.data.model.Requirement;
import org.lcmanager.gdb.service.data.util.OsFamily;
import org.lcmanager.gdb.service.game.GameCompareResult;
import org.lcmanager.gdb.service.game.GameCompareService;
import org.lcmanager.gdb.service.requirement.RequirementCompareResult;
import org.lcmanager.gdb.service.requirement.RequirementCompareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link GameCompareService}.
 * 
 */
@Service
public class GameCompareServiceImpl implements GameCompareService {
    /**
     * The {@link RequirementCompareService}.
     * 
     */
    @Autowired
    private RequirementCompareService requirementCompareService;

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.service.game.GameCompareService#compare(org.lcmanager.gdb.service.data.model.Game,
     *      org.lcmanager.gdb.service.data.model.ComputerSystem)
     */
    @Override
    public GameCompareResult compare(final Game game, final ComputerSystem computerSystem) {
        final OsFamily platform = computerSystem.getOsFamily();
        final Requirement minimumRequirement = game.getMinimumRequirements().get(platform);
        final Requirement recommendedRequirement = game.getRecommendedRequirements().get(platform);

        final RequirementCompareResult minimumRequirementCompareResult;
        if (minimumRequirement == null) {
            minimumRequirementCompareResult = null;
        } else {
            minimumRequirementCompareResult = this.requirementCompareService.compare(minimumRequirement, computerSystem);
        }
        final RequirementCompareResult recommendedRequirementCompareResult;
        if (recommendedRequirement == null) {
            recommendedRequirementCompareResult = null;
        } else {
            recommendedRequirementCompareResult = this.requirementCompareService.compare(recommendedRequirement, computerSystem);
        }

        final GameCompareResult.GameCompareResultBuilder builder = GameCompareResult.builder();
        builder.minimumRequirementCompareResult(minimumRequirementCompareResult);
        builder.recommendedRequirementCompareResult(recommendedRequirementCompareResult);
        return builder.build();
    }
}
