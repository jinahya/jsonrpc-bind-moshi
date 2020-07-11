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

import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcMessage.PROPERTY_NAME_ID;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcRequestMessage.PROPERTY_NAME_PARAMS;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessage.PROPERTY_NAME_ERROR;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessage.PROPERTY_NAME_RESULT;
import static com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessageError.PROPERTY_NAME_DATA;

final class IMoshiJsonrpcMessageHelper {

    // -----------------------------------------------------------------------------------------------------------------
    static Object getId(final Class<?> clazz, final Object object) {
        return IMoshiJsonrpcObjectHelper.get(clazz, PROPERTY_NAME_ID, object);
    }

    static void setId(final Class<?> clazz, final Object object, final Object value) {
        IMoshiJsonrpcObjectHelper.set(clazz, PROPERTY_NAME_ID, object, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Object getRequestParams(final Class<?> clazz, final Object object) {
        return IMoshiJsonrpcObjectHelper.get(clazz, PROPERTY_NAME_PARAMS, object);
    }

    static void setRequestParams(final Class<?> clazz, final Object object, final Object value) {
        IMoshiJsonrpcObjectHelper.set(clazz, PROPERTY_NAME_PARAMS, object, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Object getResponseResult(final Class<?> clazz, final Object object) {
        return IMoshiJsonrpcObjectHelper.get(clazz, PROPERTY_NAME_RESULT, object);
    }

    static void setResponseResult(final Class<?> clazz, final Object object, final Object value) {
        IMoshiJsonrpcObjectHelper.set(clazz, PROPERTY_NAME_RESULT, object, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @SuppressWarnings({"unchecked"})
    static <T extends JsonrpcResponseMessageError> T getResponseError(final Class<?> clazz, final Object object) {
        return (T) IMoshiJsonrpcObjectHelper.get(clazz, PROPERTY_NAME_ERROR, object);
    }

    static void setResponseError(final Class<?> clazz, final Object object, final JsonrpcResponseMessageError value) {
        IMoshiJsonrpcObjectHelper.set(clazz, PROPERTY_NAME_ERROR, object, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    static Object getResponseErrorData(final Class<?> clazz, final Object object) {
        return IMoshiJsonrpcObjectHelper.get(clazz, PROPERTY_NAME_DATA, object);
    }

    static void setResponseErrorData(final Class<?> clazz, final Object object, final Object value) {
        IMoshiJsonrpcObjectHelper.set(clazz, PROPERTY_NAME_DATA, object, value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    private IMoshiJsonrpcMessageHelper() {
        throw new AssertionError("instantiation is not allowed");
    }
}
