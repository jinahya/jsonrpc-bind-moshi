package com.github.jinahya.jsonrpc.bind.v2.moshi;

/*-
 * #%L
 * jsonrpc-bind-jackson
 * %%
 * Copyright (C) 2019 - 2020 Jinahya, Inc.
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

import com.squareup.moshi.JsonAdapter;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.github.jinahya.jsonrpc.bind.v2.moshi.MoshiJsonrpcConfiguration.getMoshi;
import static com.squareup.moshi.Types.newParameterizedType;
import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Collections.singletonList;
import static java.util.Collections.synchronizedMap;
import static java.util.Objects.requireNonNull;

final class IJsonrpcObjectHelper {

    static final Supplier<?> SUPPLYING_NULL = () -> null;

    static final BooleanSupplier SUPPLYING_TRUE = () -> Boolean.TRUE;

    static final BooleanSupplier SUPPLYING_FALSE = () -> Boolean.FALSE;

    static final Supplier<Boolean> SUPPLYING_TRUE_ = SUPPLYING_TRUE::getAsBoolean;

    static final Supplier<Boolean> SUPPLYING_FALSE_ = SUPPLYING_FALSE::getAsBoolean;

    private static final Predicate<?> EVALUATING_TRUE = t -> true;

    private static final Predicate<?> EVALUATING_FALSE = t -> false;

    @SuppressWarnings({"unchecked"})
    static <T> Supplier<T> supplyingNull() {
        return (Supplier<T>) SUPPLYING_NULL;
    }

    @SuppressWarnings({"unchecked"})
    static <T> Predicate<T> evaluatingTrue() {
        return (Predicate<T>) EVALUATING_TRUE;
    }

    @SuppressWarnings({"unchecked"})
    static <T> Predicate<T> evaluatingFalse() {
        return (Predicate<T>) EVALUATING_FALSE;
    }

    // -----------------------------------------------------------------------------------------------------------------
    private static Field find(final Class<?> clazz, final String name) throws NoSuchFieldException {
        assert clazz != null;
        assert name != null;
        for (final Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        final Class<?> superclass = clazz.getSuperclass();
        if (superclass == null) {
            throw new NoSuchFieldException("no field named as " + name);
        }
        return find(superclass, name);
    }

    private static final Map<Class<?>, Map<String, Field>> FIELDS = synchronizedMap(new WeakHashMap<>());

    private static Field field(final Class<?> clazz, final String name) {
        assert clazz != null;
        assert name != null;
        return FIELDS.computeIfAbsent(clazz, c -> synchronizedMap(new HashMap<>()))
                .computeIfAbsent(name, n -> {
                    try {
                        final Field field = find(clazz, n);
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        return field;
                    } catch (final NoSuchFieldException nsfe) {
                        throw new RuntimeException(nsfe);
                    }
                });
    }

    private static final Map<Class<?>, Map<String, MethodHandle>> GETTERS = synchronizedMap(new WeakHashMap<>());

    private static MethodHandle getter(final Class<?> clazz, final String name) {
        assert clazz != null;
        assert name != null;
        return GETTERS.computeIfAbsent(clazz, c -> synchronizedMap(new HashMap<>()))
                .computeIfAbsent(name, n -> {
                    try {
                        return lookup().unreflectGetter(field(clazz, n));
                    } catch (final IllegalAccessException iae) {
                        throw new RuntimeException(iae);
                    }
                });
    }

    private static final Map<Class<?>, Map<String, MethodHandle>> SETTERS = synchronizedMap(new WeakHashMap<>());

    private static MethodHandle setter(final Class<?> clazz, final String name) {
        assert clazz != null;
        assert name != null;
        return SETTERS.computeIfAbsent(clazz, c -> synchronizedMap(new HashMap<>()))
                .computeIfAbsent(name, n -> {
                    try {
                        return lookup().unreflectSetter(field(clazz, n));
                    } catch (final IllegalAccessException iae) {
                        throw new RuntimeException(iae);
                    }
                });
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Object get(final Class<?> clazz, final String name, final Object object) {
        requireNonNull(clazz, "clazz is null");
        requireNonNull(name, "name is null");
        requireNonNull(object, "object is null");
        try {
            return getter(clazz, name).invoke(object);
        } catch (final Throwable t) {
            throw new RuntimeException(t);
        }
    }

    static void set(final Class<?> clazz, final String name, final Object object, final Object value) {
        requireNonNull(clazz, "clazz is null");
        requireNonNull(name, "name is null");
        requireNonNull(object, "object is null");
        try {
            setter(clazz, name).invoke(object, value);
        } catch (final Throwable t) {
            throw new RuntimeException(t);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    static <V, R> R hasOneThenMapOrGet(final Class<?> clazz, final Object object,
                                       final BiFunction<Class<?>, Object, ? extends V> getter,
                                       final Function<? super V, ? extends R> function,
                                       final Supplier<? extends R> supplier) {
        assert getter != null;
        assert function != null;
        assert supplier != null;
        final V value = getter.apply(clazz, object);
        if (value != null) {
            return function.apply(value);
        }
        return supplier.get();
    }

    static <V, R> R hasOneThenMapOrNull(final Class<?> clazz, final Object object,
                                        final BiFunction<Class<?>, Object, ? extends V> getter,
                                        final Function<? super V, ? extends R> function) {
        return hasOneThenMapOrGet(clazz, object, getter, function, supplyingNull());
    }

    private static <V> boolean hasOneThenEvaluateOrGet(
            final Class<?> clazz, final Object object, final BiFunction<Class<?>, Object, ? extends V> getter,
            final Predicate<? super V> predicate, final BooleanSupplier supplier) {
        assert predicate != null;
        assert supplier != null;
        assert supplier == SUPPLYING_TRUE || supplier == SUPPLYING_FALSE;
        return hasOneThenMapOrGet(clazz, object, getter, predicate::test,
                                  supplier == SUPPLYING_TRUE ? SUPPLYING_TRUE_ : SUPPLYING_FALSE_);
    }

    static <V> boolean hasOneThenEvaluateOrTrue(final Class<?> clazz, final Object object,
                                                final BiFunction<Class<?>, Object, ? extends V> getter,
                                                final Predicate<? super V> predicate) {
        return hasOneThenEvaluateOrGet(clazz, object, getter, predicate, SUPPLYING_TRUE);
    }

    static <V> boolean hasOneThenEvaluateOrFalse(
            final Class<?> clazz, final Object object, final BiFunction<Class<?>, Object, ? extends V> getter,
            final Predicate<? super V> predicate) {
        return hasOneThenEvaluateOrGet(clazz, object, getter, predicate, SUPPLYING_FALSE);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static <V> List<V> hasOneThenMapAsArrayOrNull(final Class<?> clazz, final Object object,
                                                  final BiFunction<Class<?>, Object, ?> getter,
                                                  final Class<V> elementClass) {
        return hasOneThenMapOrNull(
                clazz,
                object,
                getter,
                v -> {
                    assert v != null;
                    if (v instanceof List) {
                        final ParameterizedType type = newParameterizedType(List.class, elementClass);
                        final JsonAdapter<List<V>> adapter = getMoshi().adapter(type);
                        return adapter.fromJsonValue(v);
                    }
                    return new ArrayList<>(singletonList(
                            hasOneThenMapAsObjectOrNull(clazz, object, getter, elementClass)));
                }
        );
    }

    static <V> V hasOneThenMapAsObjectOrNull(final Class<?> clazz, final Object object,
                                             final BiFunction<Class<?>, Object, ?> getter,
                                             final Class<V> objectClass) {
        assert objectClass != null;
        return hasOneThenMapOrNull(
                clazz,
                object,
                getter,
                v -> {
                    assert v != null;
                    final JsonAdapter<V> adapter = getMoshi().adapter(objectClass);
                    return adapter.fromJsonValue(v);
                }
        );
    }

    // -----------------------------------------------------------------------------------------------------------------
    private IJsonrpcObjectHelper() {
        throw new AssertionError("instantiation is not allowed");
    }
}
