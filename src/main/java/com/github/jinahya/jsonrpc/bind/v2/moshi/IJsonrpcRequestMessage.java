package com.github.jinahya.jsonrpc.bind.v2.moshi;

import com.github.jinahya.jsonrpc.bind.v2.JsonrpcRequestMessage;
import com.squareup.moshi.JsonAdapter;

import javax.validation.constraints.AssertTrue;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import static com.github.jinahya.jsonrpc.bind.v2.moshi.IJsonrpcMessageHelper.getRequestParams;
import static com.github.jinahya.jsonrpc.bind.v2.moshi.IJsonrpcMessageHelper.setRequestParams;
import static com.github.jinahya.jsonrpc.bind.v2.moshi.IJsonrpcObjectHelper.evaluatingTrue;
import static com.github.jinahya.jsonrpc.bind.v2.moshi.IJsonrpcObjectHelper.hasOneThenEvaluateOrFalse;
import static com.github.jinahya.jsonrpc.bind.v2.moshi.IJsonrpcObjectHelper.hasOneThenEvaluateOrTrue;
import static com.github.jinahya.jsonrpc.bind.v2.moshi.MoshiJsonrpcConfiguration.getMoshi;
import static com.squareup.moshi.Types.newParameterizedType;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

interface IJsonrpcRequestMessage<S extends IJsonrpcRequestMessage<S>>
        extends IJsonrpcMessage<S>,
                JsonrpcRequestMessage {

    @Override
    default boolean hasParams() {
        return hasOneThenEvaluateOrFalse(
                getClass(),
                this,
                IJsonrpcMessageHelper::getRequestParams,
                evaluatingTrue()
        );
    }

    @Override
    default @AssertTrue boolean isParamsContextuallyValid() {
        return hasOneThenEvaluateOrTrue(
                getClass(),
                this,
                IJsonrpcMessageHelper::getRequestParams,
                params -> {
                    assert params != null;
                    if (true) {
                        return params instanceof List
                               || params instanceof Map // com.squareup.moshi.LinkedHashTreeMap
                                ;
                    }
                    // TODO: 6/28/2020 Keep thinking!!!
                    return true;
                }
        );
    }

    @Override
    default <T> List<T> getParamsAsArray(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        return ofNullable(getRequestParams(getClass(), this))
                .map(v -> {
                    final ParameterizedType type = newParameterizedType(List.class, elementClass);
                    final JsonAdapter<List<T>> adapter = getMoshi().adapter(type);
                    return adapter.fromJsonValue(v);
                })
                .orElse(null);
    }

    @Override
    default void setParamsAsArray(final List<?> params) {
        setRequestParams(getClass(), this, params);
    }

    @Override
    default <T> T getParamsAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        return ofNullable(getRequestParams(getClass(), this))
                .map(v -> {
                    final JsonAdapter<T> adapter = getMoshi().adapter(objectClass);
                    return adapter.fromJsonValue(v);
                })
                .orElse(null);
    }

    @Override
    default void setParamsAsObject(final Object params) {
        setRequestParams(getClass(), this, params);
    }
}
