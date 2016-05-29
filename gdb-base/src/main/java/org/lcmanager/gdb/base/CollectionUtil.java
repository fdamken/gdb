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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import lombok.experimental.UtilityClass;

/**
 * Provides some utility methods for working with {@link Collection Collections}
 * (and {@link Map Maps}).
 *
 */
@UtilityClass
public class CollectionUtil {
    /**
     * Checks whether the given list is <code>null</code>. If so, an empty list
     * is returned.
     *
     * @param list
     *            The list to check.
     * @return The given list or an empty list.
     */
    public static <T> List<T> orEmpty(final List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }

    /**
     * Checks whether the given set is <code>null</code>. If so, an empty set is
     * returned.
     *
     * @param set
     *            The set to check.
     * @return The given set or an empty list.
     */
    public static <T> Set<T> orEmpty(final Set<T> set) {
        return set == null ? Collections.emptySet() : set;
    }

    /**
     * Checks whether the given map is <code>null</code>. If so, an empty map is
     * returned.
     *
     * @param map
     *            The map to check.
     * @return The given map or an empty list.
     */
    public static <K, V> Map<K, V> orEmpty(final Map<K, V> map) {
        return map == null ? Collections.emptyMap() : map;
    }

    /**
     * Unions the given sets.
     * 
     * <p>
     * Formal:
     * 
     * <pre>
     * A := left
     * B := right
     * R := result
     * 
     * R := { x | (x &#x2208; A) &#x2228; (x &#x2208; B) }
     * </pre>
     * </p>
     *
     * @param left
     *            The left set.
     * @param right
     *            The right set.
     * @return The union of both sets.
     */
    public static <T> Set<T> union(final Set<? extends T> left, final Set<? extends T> right) {
        final Set<T> result = new HashSet<>(left);
        result.addAll(right);
        return result;
    }

    /**
     * Filters out all elements from <code>left</code> that are present in
     * <code>right</code>.
     * 
     * <p>
     * Formal:
     * 
     * <pre>
     * A := left
     * B := right
     * R := result
     *
     * R := { x | (x &#x2208; A) &#x2227; (x &#x2209; B) }
     * </pre>
     * </p>
     *
     * @param left
     *            The left set.
     * @param right
     *            The right set.
     * @return The left-difference of <code>left</code> and <code>right</code>.
     */
    public static <T> Set<T> leftDifference(final Set<? extends T> left, final Set<? extends T> right) {
        final Set<T> result = new HashSet<>(left);
        result.removeAll(right);
        return result;
    }

    /**
     * Delegates to {@link #leftDifference(Set, Set)}.
     *
     * @param left
     *            Passed to {@link #leftDifference(Set, Set)}.
     * @param right
     *            Passed to {@link #leftDifference(Set, Set)}.
     * @return The result of {@link #leftDifference(Set, Set)}.
     * @see org.lcmanager.gdb.base.CollectionUtil#leftDifference(java.util.Set,
     *      java.util.Set)
     */
    public static <T> Set<T> leftDifference(final Collection<? extends T> left, final Collection<? extends T> right) {
        return CollectionUtil.leftDifference(new HashSet<>(left), new HashSet<>(right));
    }

    /**
     * Filters out all elements from <code>right</code> that are present in
     * <code>left</code>.
     * 
     * <p>
     * Formal:
     * 
     * <pre>
     * A := left
     * B := right
     * R := result
     *
     * R := { x | (x &#x2209; A) &#x2227; (x &#x2208; B) }
     * </pre>
     * </p>
     *
     * @param left
     *            The left set.
     * @param right
     *            The right set.
     * @return The right-difference of <code>left</code> and <code>right</code>.
     */
    public static <T> Set<T> rightDifference(final Set<? extends T> left, final Set<? extends T> right) {
        final Set<T> result = new HashSet<>(right);
        result.removeAll(left);
        return result;
    }

    /**
     * Delegates to {@link #rightDifference(Set, Set)}.
     *
     * @param left
     *            Passed to {@link #rightDifference(Set, Set)}.
     * @param right
     *            Passed to {@link #rightDifference(Set, Set)}.
     * @return The result of {@link #rightDifference(Set, Set)}.
     * @see org.lcmanager.gdb.base.CollectionUtil#rightDifference(java.util.Set,
     *      java.util.Set)
     */
    public static <T> Set<T> rightDifference(final Collection<? extends T> left, final Collection<? extends T> right) {
        return CollectionUtil.rightDifference(new HashSet<>(left), new HashSet<>(right));
    }

    /**
     * Finds a value of the given map that is associated with the key that the
     * given predicate returns <code>true</code> for.
     *
     * @param <K>
     *            The key type of the map.
     * @param <V>
     *            The value type of the map.
     * @param <R>
     *            The result type.
     * @param map
     *            The map to find the value of.
     * @param keyPredicate
     *            The predicate that decides whether a key is eligible to get
     *            the value. The given key is never <code>null</code>.
     * @param finisher
     *            Is invoked after a value was found with the found value. This
     *            may be used in order to finalize the value and make is
     *            eligible for further processing (e.g. converting the value
     *            type). If the value was <code>null</code>, the finisher is not
     *            invoked.
     * @return The value that is associated with any key the predicate returns
     *         <code>true</code> for or <code>null</code> is no value was found
     *         or the associated value is null or no key was found matched.
     */
    public static <K, V, R> R findValue(final Map<K, V> map, final Predicate<K> keyPredicate, final Function<V, R> finisher) {
        final V value = map
                .get(map.keySet().stream().filter(FunctionUtil.notNull()).filter(keyPredicate::test).findAny().orElse(null));
        if (value == null) {
            return null;
        }
        return finisher.apply(value);
    }

    /**
     * Finds a value of the given map that is associated with the key that the
     * given predicate returns <code>true</code> for.
     *
     * @param map
     *            The map to find the value of.
     * @param keyPredicate
     *            The predicate that decides whether a key is eligible to get
     *            the value. The given key is never <code>null</code>.
     * @return The value that is associated with any key the predicate returns
     *         <code>true</code> for or <code>null</code> is no value was found
     *         or the associated value is null or no key was found matched.
     */
    public static <K, V> V findValue(final Map<K, V> map, final Predicate<K> keyPredicate) {
        return CollectionUtil.findValue(map, keyPredicate, FunctionUtil.noopFunction());
    }

    /**
     * Creates an instance of {@link AddOnlyListSet} with the given
     * {@link Consumer} as the <code>adder</code>.
     *
     * @param delegator
     *            The {@link Consumer} to set as the <code>adder</code>.
     * @return The newly created instance of {@link AddOnlyListSet}.
     */
    public static <E> Collection<E> createAddOnlyCollection(final Consumer<? super E> delegator) {
        return new AddOnlyListSet<>(delegator == null ? FunctionUtil.noopConsumer() : delegator);
    }

    /**
     * Invokes {@link #createAddOnlyCollection(Consumer)} and casts the result
     * to a {@link List}.
     *
     * @param delegator
     *            The {@link Consumer} to use as the <code>delegator</code>.
     * @return The casted result of {@link #createAddOnlyCollection(Consumer)}.
     */
    @SuppressWarnings("unchecked")
    public static <E> List<E> createAddOnlyList(final Consumer<? super E> delegator) {
        return (List<E>) CollectionUtil.createAddOnlyCollection(delegator);
    }

    /**
     * Invokes {@link #createAddOnlyCollection(Consumer)} and casts the result
     * to a {@link Set}.
     *
     * @param delegator
     *            The {@link Consumer} to use as the <code>delegator</code>.
     * @return The casted result of {@link #createAddOnlyCollection(Consumer)}.
     */
    @SuppressWarnings("unchecked")
    public static <E> Set<E> createAddOnlySet(final Consumer<? super E> delegator) {
        return (Set<E>) CollectionUtil.createAddOnlyCollection(delegator);
    }

    /**
     * Creates a new {@link java.util.Map.Entry} with the given key and the
     * given value.
     *
     * @param key
     *            The key to use.
     * @param value
     *            The value to use.
     * @return The created {@link java.util.Map.Entry}.
     */
    public static <K, V> Map.Entry<K, V> createMapEntry(final K key, final V value) {
        return new MapEntryImpl<>(key, value);
    }

    /**
     * Creates a new {@link java.util.Map.Entry} with the given key.
     *
     * @param key
     *            The key to use.
     * @return The created {@link java.util.Map.Entry}.
     */
    public static <K, V> Map.Entry<K, V> createMapEntry(final K key) {
        return new MapEntryImpl<>(key);
    }

    /**
     * Provides an implementation of {@link java.util.Map.Entry} for internal
     * usage in streams.
     *
     * @param <K>
     *            The type of the key.
     * @param <V>
     *            The type of the value.
     */
    private static class MapEntryImpl<K, V> implements Map.Entry<K, V> {
        /**
         * The key of the entry.
         *
         */
        private final K key;
        /**
         * The value of the entry.
         *
         */
        private V value;

        /**
         * Constructor of MapEntryImpl.
         *
         * @param key
         *            The key of the entry.
         * @param value
         *            The value of the entry.
         */
        public MapEntryImpl(final K key, final V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Constructor of MapEntryImpl.
         *
         * @param key
         *            The key of the entry.
         */
        public MapEntryImpl(final K key) {
            this(key, null);
        }

        /**
         * {@inheritDoc}
         *
         * @see java.util.Map.Entry#getKey()
         */
        @Override
        public K getKey() {
            return this.key;
        }

        /**
         * {@inheritDoc}
         *
         * @see java.util.Map.Entry#getValue()
         */
        @Override
        public V getValue() {
            return this.value;
        }

        /**
         * {@inheritDoc}
         *
         * @see java.util.Map.Entry#setValue(java.lang.Object)
         */
        @Override
        public V setValue(final V value) {
            final V oldValue = this.value;

            this.value = value;

            return oldValue;
        }
    }

    /**
     * This class implements both {@link List} and {@link Set} but does not
     * provide any functionality of either as it can only add elements by
     * delegating to the set adder which is represented by a {@link Consumer}.
     *
     * <p>
     * This can be used to do some operations on a list add without allocating a
     * lot of RAM by storing the values.
     * </p>
     *
     * @param <E>
     *            The type of the elements.
     */
    private static class AddOnlyListSet<E> implements List<E>, Set<E> {
        /**
         * The {@link Consumer} all {@link #add(Object)} invocations are
         * delegated to.
         * 
         */
        private final Consumer<? super E> adder;

        /**
         * Constructor of AddOnlyListSet.
         *
         * @param adder
         *            The {@link #adder} to set. If <code>null</code>, an
         *            no-operation {@link Consumer} is used which causes this
         *            list to throw away the elements instantly.
         */
        public AddOnlyListSet(final Consumer<? super E> adder) {
            this.adder = adder == null ? FunctionUtil.noopConsumer() : adder;
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * <b> NOTE: This method is not supported! </b>
         * </p>
         *
         * @see java.util.List#spliterator()
         */
        @Override
        public Spliterator<E> spliterator() {
            throw new UnsupportedOperationException("This is an add-only list!");
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * <b> NOTE: This method is not supported! </b>
         * </p>
         *
         * @see java.util.List#size()
         */
        @Override
        public int size() {
            throw new UnsupportedOperationException("This is an add-only list!");
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * <b> NOTE: This method is not supported! </b>
         * </p>
         *
         * @see java.util.List#isEmpty()
         */
        @Override
        public boolean isEmpty() {
            throw new UnsupportedOperationException("This is an add-only list!");
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * <b> NOTE: This method is not supported! </b>
         * </p>
         *
         * @see java.util.List#contains(java.lang.Object)
         */
        @Override
        public boolean contains(final Object o) {
            throw new UnsupportedOperationException("This is an add-only list!");
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * <b> NOTE: This method is not supported! </b>
         * </p>
         *
         * @see java.util.List#iterator()
         */
        @Override
        public Iterator<E> iterator() {
            throw new UnsupportedOperationException("This is an add-only list!");
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * <b> NOTE: This method is not supported! </b>
         * </p>
         *
         * @see java.util.List#toArray()
         */
        @Override
        public Object[] toArray() {
            throw new UnsupportedOperationException("This is an add-only list!");
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * <b> NOTE: This method is not supported! </b>
         * </p>
         *
         * @see java.util.List#toArray(java.lang.Object[])
         */
        @Override
        public <T> T[] toArray(final T[] a) {
            throw new UnsupportedOperationException("This is an add-only list!");
        }

        /**
         * {@inheritDoc}
         * 
         * <p>
         * Delegates to {@link #adder}.
         * </p>
         *
         * @see java.util.List#add(java.lang.Object)
         */
        @Override
        public boolean add(final E e) {
            this.adder.accept(e);

            return true;
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * <b> NOTE: This method is not supported! </b>
         * </p>
         *
         * @see java.util.List#remove(java.lang.Object)
         */
        @Override
        public boolean remove(final Object o) {
            throw new UnsupportedOperationException("This is an add-only list!");
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * <b> NOTE: This method is not supported! </b>
         * </p>
         *
         * @see java.util.List#containsAll(java.util.Collection)
         */
        @Override
        public boolean containsAll(final Collection<?> c) {
            throw new UnsupportedOperationException("This is an add-only list!");
        }

        /**
         * {@inheritDoc}
         * 
         * <p>
         * Delegates to {@link #add(Object)}.
         * </p>
         *
         * @see java.util.List#addAll(java.util.Collection)
         */
        @Override
        public boolean addAll(final Collection<? extends E> c) {
            c.forEach(this::add);

            return true;
        }

        /**
         * {@inheritDoc}
         * 
         * <p>
         * Delegates to {@link #add(Object)}.
         * </p>
         *
         * @see java.util.List#addAll(int, java.util.Collection)
         */
        @Override
        public boolean addAll(final int index, final Collection<? extends E> c) {
            c.forEach(this::add);

            return true;
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * <b> NOTE: This method is not supported! </b>
         * </p>
         *
         * @see java.util.List#removeAll(java.util.Collection)
         */
        @Override
        public boolean removeAll(final Collection<?> c) {
            throw new UnsupportedOperationException("This is an add-only list!");
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * <b> NOTE: This method is not supported! </b>
         * </p>
         *
         * @see java.util.List#retainAll(java.util.Collection)
         */
        @Override
        public boolean retainAll(final Collection<?> c) {
            throw new UnsupportedOperationException("This is an add-only list!");
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * <b> NOTE: This method is not supported! </b>
         * </p>
         *
         * @see java.util.List#clear()
         */
        @Override
        public void clear() {
            throw new UnsupportedOperationException("This is an add-only list!");
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * <b> NOTE: This method is not supported! </b>
         * </p>
         *
         * @see java.util.List#get(int)
         */
        @Override
        public E get(final int index) {
            throw new UnsupportedOperationException("This is an add-only list!");
        }

        /**
         * {@inheritDoc}
         * 
         * <p>
         * Delegates to {@link #add(Object)}.
         * </p>
         *
         * @see java.util.List#set(int, java.lang.Object)
         */
        @Override
        public E set(final int index, final E element) {
            this.add(element);

            return null;
        }

        /**
         * {@inheritDoc}
         * 
         * <p>
         * Delegates to {@link #add(Object)}.
         * </p>
         *
         * @see java.util.List#add(int, java.lang.Object)
         */
        @Override
        public void add(final int index, final E element) {
            this.add(element);
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * <b> NOTE: This method is not supported! </b>
         * </p>
         *
         * @see java.util.List#remove(int)
         */
        @Override
        public E remove(final int index) {
            throw new UnsupportedOperationException("This is an add-only list!");
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * <b> NOTE: This method is not supported! </b>
         * </p>
         *
         * @see java.util.List#indexOf(java.lang.Object)
         */
        @Override
        public int indexOf(final Object o) {
            throw new UnsupportedOperationException("This is an add-only list!");
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * <b> NOTE: This method is not supported! </b>
         * </p>
         *
         * @see java.util.List#lastIndexOf(java.lang.Object)
         */
        @Override
        public int lastIndexOf(final Object o) {
            throw new UnsupportedOperationException("This is an add-only list!");
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * <b> NOTE: This method is not supported! </b>
         * </p>
         *
         * @see java.util.List#listIterator()
         */
        @Override
        public ListIterator<E> listIterator() {
            throw new UnsupportedOperationException("This is an add-only list!");
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * <b> NOTE: This method is not supported! </b>
         * </p>
         *
         * @see java.util.List#listIterator(int)
         */
        @Override
        public ListIterator<E> listIterator(final int index) {
            throw new UnsupportedOperationException("This is an add-only list!");
        }

        /**
         * {@inheritDoc}
         *
         * <p>
         * <b> NOTE: This method is not supported! </b>
         * </p>
         *
         * @see java.util.List#subList(int, int)
         */
        @Override
        public List<E> subList(final int fromIndex, final int toIndex) {
            throw new UnsupportedOperationException("This is an add-only list!");
        }
    }
}
