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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.HttpStatus;

/**
 * Represents a basic configuration for the {@link DefaultStatusGenerator}.
 * 
 * <p>
 * If any {@link HttpStatus} is set to {@link HttpStatus#I_AM_A_TEAPOT} it is
 * interpreted as the initial HTTP status.
 * </p>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface StatusConfig {
    /**
     *
     * @return The HTTP status to set if the result is <code>null</code>.
     *         Defaults to {@link HttpStatus#NOT_FOUND Error 404: Not Found}.
     */
    HttpStatus nullStatus() default HttpStatus.NOT_FOUND;

    /**
     *
     * @return The HTTP status to set if the result is valid. Defaults to
     *         {@link HttpStatus#I_AM_A_TEAPOT initial}.
     */
    HttpStatus defaultStatus() default HttpStatus.I_AM_A_TEAPOT;
}
