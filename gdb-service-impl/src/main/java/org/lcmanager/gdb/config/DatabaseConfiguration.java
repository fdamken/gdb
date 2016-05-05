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

import javax.annotation.PostConstruct;

import org.apache.ibatis.logging.LogFactory;
import org.lcmanager.gdb.service.impl.data.mapper.BaseMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configures the database and MyBatis.
 *
 */
@Configuration
@MapperScan(basePackageClasses = BaseMapper.class,
            markerInterface = BaseMapper.class)
@PropertySource("classpath:database.properties")
public class DatabaseConfiguration {
    /**
     * Configures some static settings.
     *
     */
    @PostConstruct
    public void configure() {
        LogFactory.useSlf4jLogging();
    }
}
