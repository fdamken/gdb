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
package org.lcmanager.gdb.service.platform;

import java.util.Set;

import org.lcmanager.gdb.service.data.util.OsFamily;

/**
 * The platform service is used for working with {@link OsFamily platforms}.
 *
 */
public interface PlatformService {
    /**
     * Retrieves all known platforms.
     *
     * @return All known platforms.
     */
    Set<OsFamily> retrievePlatforms();
}
