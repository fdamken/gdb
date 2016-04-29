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
package org.lcmanager.gdb.web.control.config;

import java.util.Properties;

import lombok.val;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.cache.NullCacheStorage;

/**
 * Configures some things that only make sense during development (e.g. disable
 * caches).
 *
 */
@Configuration
@ConditionalOnProperty(name = "dev",
                       havingValue = "true")
public class DevConfiguration {
    /**
     * Configures Freemarker not to cache the templates.
     *
     * @return The Freemarker configurer.
     */
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        val freeMarkerConfigurer = new FreeMarkerConfigurer();
        final Properties settings = new Properties();
        settings.setProperty("cache_storage", NullCacheStorage.class.getName());
        freeMarkerConfigurer.setFreemarkerSettings(settings);
        freeMarkerConfigurer.setTemplateLoaderPaths("classpath:/templates");
        return freeMarkerConfigurer;
    }
}
