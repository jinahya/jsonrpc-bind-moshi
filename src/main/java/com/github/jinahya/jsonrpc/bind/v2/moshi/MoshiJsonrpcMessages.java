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

import com.github.jinahya.jsonrpc.bind.JsonrpcBindException;
import com.squareup.moshi.JsonAdapter;
import okio.Okio;
import okio.Sink;
import okio.Source;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static com.github.jinahya.jsonrpc.bind.v2.moshi.MoshiJsonrpcConfiguration.getMoshi;
import static okio.Okio.buffer;

public final class MoshiJsonrpcMessages {

    static Source source(final Object source) throws IOException {
        assert source != null;
        for (final Method method : Okio.class.getMethods()) {
            final int modifiers = method.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                continue;
            }
            if (!"source".equals(method.getName())) {
                continue;
            }
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                continue;
            }
            if (!parameterTypes[0].isAssignableFrom(source.getClass())) {
                continue;
            }
            try {
                return (Source) method.invoke(null, source);
            } catch (final ReflectiveOperationException roe) {
                if (roe instanceof InvocationTargetException) {
                    final Throwable cause = roe.getCause();
                    if (cause instanceof IOException) {
                        throw (IOException) cause;
                    }
                }
                throw new JsonrpcBindException(roe);
            }
        }
        throw new JsonrpcBindException(new NoSuchMethodException("unable to find source method for " + source));
    }

    @SuppressWarnings({"unchecked"})
    static <T extends IJsonrpcMessage<?>> T fromJson(final Class<T> clazz, final Object source)
            throws IOException {
        assert clazz != null;
        assert source != null;
        final JsonAdapter<T> adapter = getMoshi().adapter(clazz);
        for (final Method method : JsonAdapter.class.getMethods()) {
            if (!"fromJson".equals(method.getName())) {
                continue;
            }
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                continue;
            }
            if (!parameterTypes[0].isAssignableFrom(source.getClass())) {
                continue;
            }
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            try {
                return (T) method.invoke(adapter, source);
            } catch (final ReflectiveOperationException roe) {
                if (roe instanceof InvocationTargetException) {
                    final Throwable cause = roe.getCause();
                    if (cause instanceof IOException) {
                        throw (IOException) cause;
                    }
                }
                throw new JsonrpcBindException(roe);
            }
        }
        return adapter.fromJson(buffer(source(source)));
    }

    static Sink sink(final Object target) {
        assert target != null;
        for (final Method method : Okio.class.getMethods()) {
            final int modifiers = method.getModifiers();
            if (!Modifier.isStatic(modifiers)) {
                continue;
            }
            if (!"sink".equals(method.getName())) {
                continue;
            }
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                continue;
            }
            if (!parameterTypes[0].isAssignableFrom(target.getClass())) {
                continue;
            }
            try {
                return (Sink) method.invoke(null, target);
            } catch (final ReflectiveOperationException roe) {
                throw new JsonrpcBindException(roe);
            }
        }
        throw new JsonrpcBindException(new NoSuchMethodException("unable to find sink method for " + target));
    }

    static <T extends IJsonrpcMessage<?>> void toJson(final Class<T> clazz, final Object target, final T value)
            throws IOException {
        assert clazz != null;
        assert target != null;
        assert value != null;
        final JsonAdapter<T> adapter = getMoshi().adapter(clazz);
        for (final Method method : JsonAdapter.class.getMethods()) {
            if (!"toJson".equals(method.getName())) {
                continue;
            }
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length < 2) {
                continue;
            }
            if (!parameterTypes[0].isAssignableFrom(target.getClass())) {
                continue;
            }
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            try {
                method.invoke(adapter, target, value);
            } catch (final ReflectiveOperationException roe) {
                if (roe instanceof InvocationTargetException) {
                    final Throwable cause = roe.getCause();
                    if (cause instanceof IOException) {
                        throw (IOException) cause;
                    }
                }
                throw new JsonrpcBindException(roe);
            }
        }
        adapter.toJson(buffer(sink(target)), value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private MoshiJsonrpcMessages() {
        throw new AssertionError("instantiation is not allowed");
    }
}
