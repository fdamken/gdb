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
package org.lcmanager.gdb.base.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

/**
 * Marks a class to be relevant for the basic health indicator by Spring. The
 * actual health detection is implemented via AspectJ by {@link HealthAspect}.
 * When a class implements this interface it is automatically registered in
 * Spring and all public methods are treated ass health-trace methods when not
 * marked with {@link NoHealthTrace}. That is all invocations are counted and
 * compared to all failed invocations to calculate the health of the class.
 *
 */
public interface HealthRelevant extends HealthIndicator {
    /**
     * {@inheritDoc}
     *
     * @see org.springframework.boot.actuate.health.HealthIndicator#health()
     */
    @Override
    default Health health() {
        throw new UnsupportedOperationException(getClass().getCanonicalName() + " must be woven by AspectJ to get this work!");
    }
}
