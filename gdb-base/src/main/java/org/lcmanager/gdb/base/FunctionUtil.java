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

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import lombok.experimental.UtilityClass;

/**
 * Provides some basic implementations of some functions in the
 * <code>java.util.function</code> package.
 *
 */
@UtilityClass
public class FunctionUtil {
    /**
     * Creates a predication that returns <code>true</code> if the value is not
     * <code>null</code> and <code>false</code> if it is.
     *
     * @return The predicate.
     */
    public static <T> Predicate<T> notNull() {
        return obj -> obj != null;
    }

    /**
     * Creates a {@link Function} using the given type that does nothing
     * (NO-OPeration).
     *
     * @param <R>
     *            The result type of the {@link Function}. Must be the same as
     *            <code>T</code> or a subclass.
     * @param <T>
     *            The parameter type of the {@link Function}.
     * @return The {@link Function}.
     */
    public static <R, T extends R> Function<T, R> noopFunction() {
        return obj -> obj;
    }

    /**
     * Creates a {@link Consumer} using the given type that does nothing
     * (NO-OPeration)
     *
     * @return The {@link Consumer}.
     */
    public static <T> Consumer<T> noopConsumer() {
        return obj -> {
            // Nothing to do.
        };
    }
}
