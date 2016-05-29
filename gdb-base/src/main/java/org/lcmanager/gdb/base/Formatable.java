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

import java.util.Locale;

/**
 * This interface marks classes as 'formatable'. That is an invocation of the
 * specified methods {@link #format(Locale)} or {@link #format()} results in a
 * human-readable string that may be displayed to the user.
 *
 */
public interface Formatable {
    /**
     * Formats <code>this</code> (the object this method is invoked on) to a
     * human-readable user-friendly string.
     * 
     * <p>
     * <b> NOTE: Localization/Internationalization is not specified and
     * therefore some implementations may return English text however
     * {@link #format(Locale)} was invoked using a German locale and vice
     * versa. </b>
     * </p>
     *
     * @param locale
     *            The locale to use form formatting.
     * @return The formatted string.
     */
    String format(final Locale locale);

    /**
     * Invokes {@link #format(Locale)} with the default locale.
     *
     * @return The formatted string returned by {@link #format(Locale)}.
     */
    default String format() {
        return this.format(Locale.getDefault());
    }

    /**
     * Invokes {@link #format()}.
     *
     * @return {@link #format()}.
     */
    default String getFormatted() {
        return this.format();
    }
}
