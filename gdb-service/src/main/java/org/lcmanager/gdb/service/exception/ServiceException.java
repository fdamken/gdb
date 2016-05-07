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
package org.lcmanager.gdb.service.exception;

import org.lcmanager.gdb.base.exception.GdbException;

/**
 * The base exception for all service exceptions.
 *
 */
public abstract class ServiceException extends GdbException {
    /**
     * The serial version UID.
     *
     */
    private static final long serialVersionUID = -6187158756269942804L;

    /**
     * Constructor of ServiceException.
     *
     * @param message
     *            A detailed error message.
     * @param cause
     *            The causing error.
     */
    public ServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor of ServiceException.
     *
     * @param message
     *            A detailed error message.
     */
    public ServiceException(final String message) {
        super(message);
    }
}
