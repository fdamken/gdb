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
package org.lcmanager.gdb.steam.exception;

import org.lcmanager.gdb.service.game.exception.GameServiceException;

/**
 * Thrown when any error occurs in the Steam game service.
 *
 */
public class SteamGameServiceException extends GameServiceException {
    /**
     * The serial version UID.
     * 
     */
    private static final long serialVersionUID = -586050823141471091L;

    /**
     * Constructor of SteamGameServiceException.
     *
     * @param message
     *            A detailed error message.
     * @param cause
     *            The causing error.
     */
    public SteamGameServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor of SteamGameServiceException.
     *
     * @param message
     *            A detailed error message.
     */
    public SteamGameServiceException(final String message) {
        super(message);
    }
}
