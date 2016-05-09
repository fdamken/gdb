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
package org.lcmanager.gdb.appl.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.lcmanager.gdb.base.BaseAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * This aspect is used for trace logging only.
 *
 */
@Component
@Aspect
public class LoggingAspect extends BaseAspect {
    /**
     * The log threshold (in milliseconds).
     * 
     */
    @Value("${log.threshold}")
    private int logThreshold;

    /**
     * Logs the execution of the given {@link ProceedingJoinPoint}.
     *
     * <p>
     * This method intercepts all classes in the GDB but {@link LoggingAspect}.
     * </p>
     *
     * @param joinPoint
     *            The {@link ProceedingJoinPoint} to log.
     * @return The return of the {@link ProceedingJoinPoint}.
     * @throws Throwable
     *             If the {@link ProceedingJoinPoint} throws it.
     */
    @Around("gdb() && !me()")
    public Object log(final ProceedingJoinPoint joinPoint) throws Throwable {
        assert joinPoint != null;

        final Signature signature = joinPoint.getSignature();
        final Logger logger = LoggerFactory.getLogger(signature.getDeclaringType());
        Object result = null;
        if (logger.isTraceEnabled()) {
            final StopWatch stopWatch = new StopWatch();

            Throwable throwable = null;

            stopWatch.start();
            try {
                result = joinPoint.proceed();
            } catch (final Throwable t) {
                throwable = t;
            }
            stopWatch.stop();

            this.log(signature, stopWatch, throwable);

            if (throwable != null) {
                throw throwable;
            }
        } else {
            result = joinPoint.proceed();
        }
        return result;
    }

    /**
     * Logs the given, executed, method call.
     *
     * @param signature
     *            The {@link Signature} of the executed method.
     * @param stopWatch
     *            The {@link StopWatch} that contains the execution time.
     * @param throwable
     *            The thrown exception, if any. Otherwise, <code>null</code>.
     */
    private void log(final Signature signature, final StopWatch stopWatch, final Throwable throwable) {
        assert signature != null;
        assert stopWatch != null;

        final Logger logger = LoggerFactory.getLogger(signature.getDeclaringType());

        final long time = stopWatch.getTotalTimeMillis();
        if (time >= this.logThreshold) {
            String msg = "Executed " + signature + " in " + time + "ms!";
            if (throwable != null) {
                msg += " Threw an exception: " + throwable.getClass() + " (" + throwable.getMessage() + ")!";
            }
            logger.trace(msg);
        }
    }

    /**
     * Pointcut for {@link LoggingAspect}.
     *
     */
    @Pointcut("within(org.lcmanager.gdb.appl.aspect.LoggingAspect)")
    private void me() {
        // Nothing to do.
    }
}
