/*
 * #%L
 * BH2K Portal Data API
 * %%
 * Copyright (C) 2015 - 2016 Blockhaus2000
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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import lombok.Value;
import lombok.experimental.Accessors;

/**
 * This class provides some utilities for using the new Java 8 {@link Stream
 * Streams}.
 *
 */
public final class StreamUtil {
    /**
     * Constructor of StreamUtil.
     *
     */
    public StreamUtil() {
        // Nothing to do.
    }

    /**
     * Checks whether the given stream is empty.
     *
     * @param stream
     *            The stream to check.
     * @return Whether the stream is empty or not.
     */
    public static <T> boolean isEmpty(final Stream<T> stream) {
        return stream.count() <= 0;
    }

    /**
     * Instantiates a {@link Collector} that is resulting in a {@link Map} which
     * collects all elements of the stream it is used on and retrieves a key for
     * every value from the given supplier which is than used to map the
     * previous values of the stream to that supplied keys. This may be used in
     * order to create multi-linked maps for fast access without searching
     * through a whole list (i.e. when working with IDs in any way).
     *
     * @param keySupplier
     *            The {@link Supplier Supplier<V, K>} that maps values to keys.
     * @return The collector for collecting the elements as described above.
     */
    // - - - - - - - - - | Generics might be confusing some times... |
    public static <K, V> Collector<V, Set<Map.Entry<K, V>>, Map<K, V>> collectMap(final Function<V, K> keySupplier) {
        if (keySupplier == null) {
            throw new IllegalArgumentException("keySupplier must not be null!");
        }

        // Magic. Do not touch.
        return new CollectorImpl<>(HashSet::new, (set, value) -> {
            set.add(CollectionUtil.createMapEntry(keySupplier.apply(value), value));
        }, (final Set<Map.Entry<K, V>> left, final Set<Map.Entry<K, V>> right) -> {
            final Set<Entry<K, V>> result = new HashSet<>();
            result.addAll(left);
            result.addAll(right);
            return result;
        }, set -> {
            final Map<K, V> result = new HashMap<>();
            set.forEach(entry -> {
                result.put(entry.getKey(), entry.getValue());
            });
            return result;
        });
    }

    /**
     * Instantiates a {@link Collector} that is resulting in a {@link String}
     * which collects all elements of the stream that it may be used in
     * conjunction with and appends them to each other (like a regular join(...)
     * method does, but using {@link Stream Streams}).
     *
     * @param delimiter
     *            The delimiter that is places between all parts of the created
     *            {@link String}. It is not appended to the end and therefore
     *            provides a join(...) method.
     * @return The collector for collecting the elements as described above.
     */
    public static Collector<Object, StringBuilder, String> collectString(final String delimiter) {
        return new CollectorImpl<>(StringBuilder::new, (builder, str) -> builder.append(str).append(delimiter),
                (left, right) -> left.append(right),
                builder -> builder.delete(builder.length() - delimiter.length(), builder.length()).toString());
    }

    /**
     * Invokes {@link #collectString(String)} with <code>delimiter = ""</code>.
     *
     * @return See {@link #collectString(String)}.
     * @see org.lcmanager.gdb.base.StreamUtil#collectString(java.lang.String)
     */
    public static Collector<Object, StringBuilder, String> collectString() {
        return StreamUtil.collectString("");
    }

    /**
     * Provides a simple POJO implementation of {@link Collector}.
     *
     * @param <T>
     *            The type of the input elements.
     * @param <A>
     *            The type of the accumulator objects.
     * @param <R>
     *            The type of the result object.
     * @see java.util.stream.Collector
     */
    @Value
    @Accessors(fluent = true)
    private static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
        /**
         * The supplier for {@link #supplier()}.
         *
         */
        private final Supplier<A> supplier;
        /**
         * The accumulator for {@link #accumulator()}.
         *
         */
        private final BiConsumer<A, T> accumulator;
        /**
         * The combiner for {@link #combiner()}.
         *
         */
        private final BinaryOperator<A> combiner;
        /**
         * The finisher for {@link #finisher()}.
         *
         */
        private final Function<A, R> finisher;
        /**
         * The characteristics for {@link #characteristics()}.
         *
         */
        private final Set<Collector.Characteristics> characteristics;

        /**
         * Constructor of CollectorImpl.
         *
         * @param supplier
         *            The supplier for {@link #supplier()}.
         * @param accumulator
         *            The accumulator for {@link #accumulator()}.
         * @param combiner
         *            The combiner for {@link #combiner()}.
         * @param finisher
         *            The finisher for {@link #finisher()}.
         * @param characteristics
         *            The characteristics for {@link #characteristics()}.
         */
        public CollectorImpl(final Supplier<A> supplier, final BiConsumer<A, T> accumulator, final BinaryOperator<A> combiner,
                final Function<A, R> finisher, final Set<Collector.Characteristics> characteristics) {
            assert supplier != null;
            assert accumulator != null;
            assert combiner != null;
            assert finisher != null;
            assert characteristics != null;

            this.supplier = supplier;
            this.accumulator = accumulator;
            this.combiner = combiner;
            this.finisher = finisher;
            this.characteristics = new HashSet<Collector.Characteristics>(characteristics);
        }

        /**
         * Constructor of CollectorImpl.
         *
         * @param supplier
         *            The supplier for {@link #supplier()}.
         * @param accumulator
         *            The accumulator for {@link #accumulator()}.
         * @param combiner
         *            The combiner for {@link #combiner()}.
         * @param finisher
         *            The finisher for {@link #finisher()}.
         */
        public CollectorImpl(final Supplier<A> supplier, final BiConsumer<A, T> accumulator, final BinaryOperator<A> combiner,
                final Function<A, R> finisher) {
            this(supplier, accumulator, combiner, finisher, Collections.emptySet());
        }
    }
}
