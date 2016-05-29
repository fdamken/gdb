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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.lcmanager.gdb.base.BaseAspect;
import org.lcmanager.gdb.base.MathUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

/**
 * The aspect for {@link HealthRelevant}.
 *
 * @see org.lcmanager.gdb.base.health.HealthRelevant
 */
@Component
@Aspect
public class HealthAspect extends BaseAspect {
    /**
     * Represents the an <code>UNSTABLE</code> health status.
     * 
     */
    private static final Status UNSTABLE = new Status("UNSTABLE");

    /**
     * The threshold, in percentages, when to switch from <code>UP</code> to
     * <code>UNSTABLE</code>.
     * 
     */
    @Value("${health.unstable-threshold}")
    private int unstableThreshold;

    /**
     * Contains the count of the total invocations.
     * 
     */
    private final Map<Integer, Integer> total = new ConcurrentHashMap<>();
    /**
     * Contains the count of the failed invocations.
     * 
     */
    private final Map<Integer, Integer> error = new ConcurrentHashMap<>();

    /**
     * Wraps around all public methods of {@link HealthRelevant health-relevant}
     * classes that are not marked with {@link NoHealthTrace}.
     * 
     * <p>
     * This increments {@link #total} and {@link #error}.
     * </p>
     *
     * @param joinPoint
     *            The proceeding join point.
     * @return The result object of the join point.
     * @throws Throwable
     *             If the join point throws an error.
     */
    @Around("within(org.lcmanager.gdb.base.health.HealthRelevant+) " //
            + "&& execution(public * *(..)) " //
            + "&& !execution(@org.lcmanager.gdb.base.health.NoHealthTrace * *(..))")
    public Object aroundHealthRelevant(final ProceedingJoinPoint joinPoint) throws Throwable {
        final Class<?> clazz = joinPoint.getTarget().getClass();

        this.incrementTotal(clazz);

        try {
            return joinPoint.proceed();
        } catch (final Throwable error) {
            this.incrementError(clazz);

            throw error;
        }
    }

    /**
     * Wraps around the {@link HealthRelevant#health()} method and provides its
     * implementation.
     *
     * @param joinPoint
     *            The join point that is only used to retrieve the implementing
     *            class.
     * @return The resulting health depending on the total invocations and the
     *         failed invocations.
     */
    @Around("execution(* org.lcmanager.gdb.base.health.HealthRelevant+ .health())")
    public Health aroundHealth(final ProceedingJoinPoint joinPoint) {
        final Class<?> clazz = joinPoint.getTarget().getClass();
        final int totalCount = this.nullOrZero(this.total.get(this.makeCountKey(clazz)));
        final int errorCount = this.nullOrZero(this.error.get(this.makeCountKey(clazz)));
        final Health.Builder healthBuilder;
        if (MathUtil.calulatePercentage(totalCount, errorCount) < this.unstableThreshold) {
            healthBuilder = Health.up();
        } else {
            healthBuilder = Health.status(HealthAspect.UNSTABLE);
        }
        healthBuilder.withDetail("total", totalCount);
        healthBuilder.withDetail("error", errorCount);
        return healthBuilder.build();
    }

    /**
     * Increments {@link #total}.
     *
     * @param clazz
     *            The key of {@link #total}.
     */
    private void incrementTotal(final Class<?> clazz) {
        this.total.put(this.makeCountKey(clazz), this.nullOrZero(this.total.get(this.makeCountKey(clazz))) + 1);
    }

    /**
     * Increments {@link #error}.
     *
     * @param clazz
     *            The key of {@link #error}.
     */
    private void incrementError(final Class<?> clazz) {
        this.error.put(this.makeCountKey(clazz), this.nullOrZero(this.error.get(this.makeCountKey(clazz))) + 1);
    }

    /**
     * Creates the key for both {@link #total} and {@link #error} from the given
     * class.
     *
     * @param clazz
     *            The class to create the key for.
     * @return The created key.
     */
    private int makeCountKey(final Class<?> clazz) {
        return clazz.hashCode();
    }

    /**
     * Converts the given {@link Integer} to and <code>int</code> by converting
     * <code>null</code> values to <code>0</code>.
     *
     * @param x
     *            The number.
     * @return <code>0</code> if the given number is <code>null</code>.
     *         Otherwise the number.
     */
    private int nullOrZero(final Integer x) {
        return x == null ? 0 : x;
    }
}
