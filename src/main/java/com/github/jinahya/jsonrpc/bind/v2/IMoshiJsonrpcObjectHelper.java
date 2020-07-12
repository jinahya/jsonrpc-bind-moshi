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
import com.squareup.moshi.JsonDataException;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.MoshiJsonrpcConfiguration.getMoshi;
import static com.squareup.moshi.Types.newParameterizedType;
import static java.lang.invoke.MethodType.methodType;
import static java.util.Collections.singletonList;

final class IMoshiJsonrpcObjectHelper {

    @SuppressWarnings({"unchecked"})
    static <T> Class<T> wrap(final Class<T> clazz) {
        assert clazz != null;
        return (Class<T>) methodType(clazz).wrap().returnType();
    }

    @SuppressWarnings("unchecked")
    static <T> Class<T> unwrap(final Class<T> clazz) {
        assert clazz != null;
        return (Class<T>) methodType(clazz).unwrap().returnType();
    }

    static <T> List<T> getAsArray(final Class<T> clazz, final Object value) {
        assert clazz != null;
        assert value != null;
        if (value instanceof List) {
            final ParameterizedType type = newParameterizedType(List.class, wrap(clazz));
            final JsonAdapter<List<T>> adapter = getMoshi().adapter(type);
            return adapter.fromJsonValue(value);
        }
        return new ArrayList<>(singletonList(getAsObject(clazz, value)));
    }

    static <T> T getAsObject(final Class<T> clazz, final Object value) {
        assert clazz != null;
        assert value != null;
        final JsonAdapter<T> adapter = getMoshi().adapter(clazz);
        try {
            return adapter.fromJsonValue(value);
        } catch (final JsonDataException jde) {
            throw new JsonrpcBindException(jde);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    private IMoshiJsonrpcObjectHelper() {
        throw new AssertionError("instantiation is not allowed");
    }
}
