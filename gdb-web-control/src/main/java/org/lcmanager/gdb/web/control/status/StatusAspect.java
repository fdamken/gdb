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
package org.lcmanager.gdb.web.control.status;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.lcmanager.gdb.base.ApplicationContextUtil;
import org.lcmanager.gdb.base.BaseAspect;
import org.lcmanager.gdb.web.control.util.exception.NullContentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This aspect intercepts all controller methods that are capable for status
 * generation as described in {@link GenerateStatus}.
 *
 * @see org.lcmanager.gdb.web.control.status.GenerateStatus
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Component
@Aspect
public class StatusAspect extends BaseAspect {
    /**
     * The {@link ApplicationContext} used to autowire the status generator.
     * 
     */
    @Autowired
    private AbstractApplicationContext applicationContext;

    /**
     * The actual pointcut for intercepting the methods with status generation.
     * 
     * <p>
     * This wraps all all public controller methods within the GDB that are
     * within a generate-status class or are generate-status methods.
     * </p>
     *
     * @param joinPoint
     *            The join point.
     * @return The result object with the appropriate status.
     * @throws Throwable
     *             If any error occurs during the join point invocation.
     */
    @Around("gdb() && publicMethod() && requestMethod() && ( generateStatusClass() || generateStatusMethod() )")
    public ResponseEntity around(final ProceedingJoinPoint joinPoint) throws Throwable {
        ResponseEntity responseEntity;
        Object body = null;
        try {
            responseEntity = (ResponseEntity) joinPoint.proceed();
            body = responseEntity.getBody();
        } catch (final NullContentException dummy) {
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        final Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        final StatusGenerator statusGenerator = this.extractStatusGenerator(method);
        final HttpStatus generatedStatus = statusGenerator.generateStatus(method, body);

        final HttpStatus status = generatedStatus == null ? responseEntity.getStatusCode() : generatedStatus;

        return ResponseEntity.status(status).body(body);
    }

    /**
     * Extracts and instantiates the status generator that is responsible for
     * the given method.
     *
     * @param method
     *            The method to extract the status generator instance from.
     * @return The created status generator.
     */
    private StatusGenerator extractStatusGenerator(final Method method) {
        final GenerateStatus generateStatusAnnotation;
        if (method.isAnnotationPresent(GenerateStatus.class)) {
            generateStatusAnnotation = method.getAnnotation(GenerateStatus.class);
        } else {
            generateStatusAnnotation = method.getDeclaringClass().getAnnotation(GenerateStatus.class);
        }

        final Class<? extends StatusGenerator<?>> clazz = generateStatusAnnotation.value();
        final Object statusGenerator = ApplicationContextUtil.getApplicationContext().getBeanFactory().autowire(clazz,
                AutowireCapableBeanFactory.AUTOWIRE_NO, true);
        return (StatusGenerator) statusGenerator;
    }

    /**
     * Pointcut for every public method.
     *
     */
    @Pointcut("execution(public * *(..))")
    public void publicMethod() {
        // Nothing to do.
    }

    /**
     * Pointcut for every method that is annotated with {@link RequestMapping
     * &#064;RequestMapping} and returns a {@link ResponseEntity}.
     *
     */
    @Pointcut("execution(@org.springframework.web.bind.annotation.RequestMapping org.springframework.http.ResponseEntity *(..))")
    public void requestMethod() {
        // Nothing to do.
    }

    /**
     * Pointcut for every method within a class that is tagged with
     * {@link GenerateStatus &#064;GenerateStatus}.
     * 
     */
    @Pointcut("within(@org.lcmanager.gdb.web.control.status.GenerateStatus *)")
    public void generateStatusClass() {
        // Nothing to do.
    }

    /**
     * Pointcut for every method that is tagged with {@link GenerateStatus
     * &#064;GenerateStatus}.
     *
     */
    @Pointcut("execution(@org.lcmanager.gdb.web.control.status.GenerateStatus * *(..))")
    public void generateStatusMethod() {
        // Nothing to do.
    }
}
