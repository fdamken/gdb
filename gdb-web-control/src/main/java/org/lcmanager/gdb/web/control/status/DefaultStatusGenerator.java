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

import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;

/**
 * The default {@link StatusGenerator} is the default value for
 * {@link GenerateStatus#value()} and generates a status based on the result of
 * the controller method.
 * 
 * <p>
 * This generator may be configured by using {@link StatusConfig @StatusConfig}
 * to set some properties. The configuration can either be applied on the class
 * level which represents the default for the class or on the method level which
 * overrides both the default settings and the class settings. <br>
 * That is, starting from the method to the default value, a configuration
 * hierarchy defined by <code>method &gt; class &gt; default</code>.
 * <p>
 * <br>
 * NOTE: If any non-default value is present in the hierarchy, the it is used as
 * the setting. It is not possible to force the default setting. </b>
 * </p>
 * </p>
 *
 */
public class DefaultStatusGenerator implements StatusGenerator<Object> {
    /**
     * The default {@link StatusConfig} annotation. This contains the default
     * value only.
     * 
     */
    private static final StatusConfig STATUS_CONFIG_DEFAULT;

    static {
        @StatusConfig
        class Foo {
            // Nothing to do.
        }

        STATUS_CONFIG_DEFAULT = Foo.class.getAnnotation(StatusConfig.class);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.lcmanager.gdb.web.control.status.StatusGenerator#generateStatus(java.lang.reflect.Method,
     *      java.lang.Object)
     */
    @Override
    public HttpStatus generateStatus(final Method method, final Object pBody) {
        final StatusConfig classConfig = this.extractClassConfig(method);
        final StatusConfig methodConfig = this.extractMethodConfig(method);

        final HttpStatus nullStatus;
        if (methodConfig.nullStatus() == DefaultStatusGenerator.STATUS_CONFIG_DEFAULT.nullStatus()) {
            nullStatus = classConfig.nullStatus();
        } else {
            nullStatus = methodConfig.nullStatus();
        }

        final HttpStatus defaultStatus;
        if (methodConfig.defaultStatus() == DefaultStatusGenerator.STATUS_CONFIG_DEFAULT.defaultStatus()) {
            defaultStatus = classConfig.defaultStatus();
        } else {
            defaultStatus = methodConfig.nullStatus();
        }

        final Object body;
        if (pBody instanceof Resource) {
            body = ((Resource<?>) pBody).getContent();
        } else {
            body = pBody;
        }

        return body == null ? nullStatus : defaultStatus == HttpStatus.I_AM_A_TEAPOT ? null : defaultStatus;
    }

    /**
     * Extracts the status configuration on the method level of the given
     * method. If no status configuration annotation is present this falls back
     * to {@link #STATUS_CONFIG_DEFAULT}.
     *
     * @param method
     *            The method to extract the status configuration annotation on
     *            method level from.
     * @return The status configuration annotation.
     */
    private StatusConfig extractMethodConfig(final Method method) {
        return method.isAnnotationPresent(StatusConfig.class) ? method.getAnnotation(StatusConfig.class)
                : DefaultStatusGenerator.STATUS_CONFIG_DEFAULT;
    }

    /**
     * Extracts the status configuration on the class level of the given method.
     * If no status configuration annotation is present this falls back to
     * {@link #STATUS_CONFIG_DEFAULT}.
     *
     * @param method
     *            The method to extract the status configuration annotation on
     *            class level from.
     * @return The status configuration annotation.
     */
    private StatusConfig extractClassConfig(final Method method) {
        return method.getDeclaringClass().isAnnotationPresent(StatusConfig.class) ? method.getDeclaringClass().getAnnotation(
                StatusConfig.class) : DefaultStatusGenerator.STATUS_CONFIG_DEFAULT;
    }
}
