package com.github.jinahya.jsonrpc.bind.v2;

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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static com.github.jinahya.jsonrpc.bind.v2.MoshiJsonrpcConfiguration.getMoshi;
import static okio.Okio.buffer;

public final class MoshiJsonrpcMessageServiceHelper {

    // -----------------------------------------------------------------------------------------------------------------
    static Source source(final Object source) {
        assert source != null;
        final Class<?> sourceClass = source.getClass();
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
            if (!parameterTypes[0].isAssignableFrom(sourceClass)) {
                continue;
            }
            try {
                return (Source) method.invoke(null, source);
            } catch (final ReflectiveOperationException roe) {
                throw new JsonrpcBindException(roe);
            }
        }
        throw new JsonrpcBindException("unable to find source method for " + sourceClass);
    }

    @SuppressWarnings({"unchecked"})
    static <T extends JsonrpcMessage> T fromJson(final Class<T> clazz, final Object source) {
        assert clazz != null;
        assert source != null;
        final Class<?> sourceClass = source.getClass();
        final JsonAdapter<T> adapter = getMoshi().adapter(clazz);
        for (final Method method : JsonAdapter.class.getMethods()) {
            if (!"fromJson".equals(method.getName())) {
                continue;
            }
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                continue;
            }
            if (!parameterTypes[0].isAssignableFrom(sourceClass)) {
                continue;
            }
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            try {
                return (T) method.invoke(adapter, source);
            } catch (final ReflectiveOperationException roe) {
                throw new JsonrpcBindException(roe);
            }
        }
        try {
            return adapter.fromJson(buffer(source(source)));
        } catch (final IOException ioe) {
            throw new JsonrpcBindException(ioe);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Sink sink(final Object target) {
        assert target != null;
        final Class<?> targetClass = target.getClass();
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
            if (!parameterTypes[0].isAssignableFrom(targetClass)) {
                continue;
            }
            try {
                return (Sink) method.invoke(null, target);
            } catch (final ReflectiveOperationException roe) {
                throw new JsonrpcBindException(roe);
            }
        }
        throw new JsonrpcBindException("unable to find sink method for " + targetClass);
    }

    static <T extends JsonrpcMessage> void toJson(final Class<T> clazz, final Object target, final T value) {
        assert clazz != null;
        assert target != null;
        assert value != null;
        final Class<?> targetClass = target.getClass();
        final JsonAdapter<T> adapter = getMoshi().adapter(clazz);
        for (final Method method : JsonAdapter.class.getMethods()) {
            if (!"toJson".equals(method.getName())) {
                continue;
            }
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length < 2) {
                continue;
            }
            if (!parameterTypes[0].isAssignableFrom(targetClass)) {
                continue;
            }
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            try {
                method.invoke(adapter, target, value);
                return;
            } catch (final ReflectiveOperationException roe) {
                throw new JsonrpcBindException(roe);
            }
        }
        try {
            adapter.toJson(buffer(sink(target)), value);
        } catch (final IOException ioe) {
            throw new JsonrpcBindException(ioe);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private MoshiJsonrpcMessageServiceHelper() {
        throw new AssertionError("instantiation is not allowed");
    }
}
