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
package org.lcmanager.gdb.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;

/**
 * Configures Spring Web (MVC).
 *
 */
@Configuration
public class WebConfiguration extends WebMvcAutoConfigurationAdapter {
    /**
     * {@inheritDoc}
     *
     * @see org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#configureMessageConverters(java.util.List)
     */
    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
        // Add all converters.
        final List<HttpMessageConverter<?>> baseConverters = new ArrayList<>();
        super.configureMessageConverters(baseConverters);

        // Exclude the Jaxb2RootElementHttpMessageConverter as it causes
        // problems when mapping regular POJOs with no JAXB annotations.
        baseConverters.stream() //
                .filter(baseConverter -> !(baseConverter instanceof Jaxb2RootElementHttpMessageConverter)) //
                .sequential() //
                .forEach(converters::add);
    }
}
