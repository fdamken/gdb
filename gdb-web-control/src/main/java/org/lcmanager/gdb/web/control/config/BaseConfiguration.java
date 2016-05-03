/*
 * #%L
 * Game Database
 * %%
 * Copyright (C) 2016 LCManager Group
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
package org.lcmanager.gdb.web.control.config;

import org.lcmanager.gdb.BasePackageMarker;
import org.lcmanager.gdb.base.CommonConstants;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Configures the application and sets the component scan to the root of the
 * package hierarchy.
 *
 */
@Configuration
@EnableAutoConfiguration(exclude = DataSourceTransactionManagerAutoConfiguration.class)
@EnableLoadTimeWeaving
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@EnableCaching
@ComponentScan(basePackageClasses = BasePackageMarker.class)
@ControllerAdvice
public class BaseConfiguration {
    /**
     * Sets the attribute <code>dev</code> in every model to the value of
     * {@link CommonConstants#DEV}.
     *
     * @return The value of {@link CommonConstants#DEV}.
     */
    @ModelAttribute("dev")
    public boolean modelAttributeDev() {
        return CommonConstants.DEV;
    }
}
