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
package org.lcmanager.gdb.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Holds the application context.
 * 
 * <p>
 * <b> NOTE: Using this class may be tricky! Therefore, DO NOT use any methods
 * if you are not 150% sure that you know what you are doing! </b>
 * </p>
 *
 */
@Component
public class ApplicationContextUtil {
    /**
     * The application context.
     * 
     */
    private static AbstractApplicationContext applicationContext;

    /**
     * Retrieves the application context.
     *
     * @return The application context.
     */
    public static AbstractApplicationContext getApplicationContext() {
        return ApplicationContextUtil.applicationContext;
    }

    /**
     * Sets the application context.
     *
     * @param applicationContext
     *            The application context to set.
     */
    @Autowired
    private void setApplicationContext(final AbstractApplicationContext applicationContext) {
        ApplicationContextUtil.applicationContext = applicationContext;
    }
}
