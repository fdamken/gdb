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
package org.lcmanager.gdb.service.data.model.os;

/**
 * An OS family contains multiple operating system that are built upon the same
 * technologies (e.g. the <code>WINDOWS</code> family contains
 * <code>Windows XP</code>, <code>Windows 2000</code> and so on).
 * 
 *
 */
public enum OsFamily {
    /**
     * Windows.
     * 
     */
    WINDOWS,
    /**
     * Unix.
     * 
     */
    UNIX,
    /**
     * Mac OS.
     * 
     */
    MAC,
    /**
     * Other families that are not explicitly listed.
     * 
     */
    OTHER;
}
