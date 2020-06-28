package com.github.jinahya.jsonrpc.bind.v2.moshi;

import com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessage;
import com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessageError;

import javax.validation.constraints.AssertTrue;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.moshi.IJsonrpcMessageHelper.setResponseError;
import static com.github.jinahya.jsonrpc.bind.v2.moshi.IJsonrpcMessageHelper.setResponseResult;
import static com.github.jinahya.jsonrpc.bind.v2.moshi.IJsonrpcObjectHelper.evaluatingTrue;
import static com.github.jinahya.jsonrpc.bind.v2.moshi.IJsonrpcObjectHelper.hasOneThenEvaluateOrFalse;
import static com.github.jinahya.jsonrpc.bind.v2.moshi.IJsonrpcObjectHelper.hasOneThenEvaluateOrTrue;
import static com.github.jinahya.jsonrpc.bind.v2.moshi.IJsonrpcObjectHelper.hasOneThenMapAsArrayOrNull;
import static com.github.jinahya.jsonrpc.bind.v2.moshi.IJsonrpcObjectHelper.hasOneThenMapAsObjectOrNull;
import static java.util.Objects.requireNonNull;

interface IJsonrpcResponseMessage<S extends IJsonrpcResponseMessage<S>>
        extends IJsonrpcMessage<S>,
                JsonrpcResponseMessage {

    @Override
    @AssertTrue
    default boolean isResultAndErrorExclusive() {
        return JsonrpcResponseMessage.super.isResultAndErrorExclusive();
    }

    // ---------------------------------------------------------------------------------------------------------- result
    @Override
    default boolean hasResult() {
        return hasOneThenEvaluateOrFalse(
                getClass(),
                this,
                IJsonrpcMessageHelper::getResponseResult,
                evaluatingTrue()
        );
    }

    @Override
    @AssertTrue
    default boolean isResultContextuallyValid() {
        return JsonrpcResponseMessage.super.isResultContextuallyValid();
    }

    @Override
    default <T> List<T> getResultAsArray(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        return hasOneThenMapAsArrayOrNull(
                getClass(),
                this,
                IJsonrpcMessageHelper::getResponseResult,
                elementClass
        );
    }

    @Override
    default void setResultAsArray(final List<?> result) {
        setResponseResult(getClass(), this, result);
    }

    @Override
    default <T> T getResultAsObject(final Class<T> objectClass) {
        return hasOneThenMapAsObjectOrNull(
                getClass(),
                this,
                IJsonrpcMessageHelper::getResponseResult,
                objectClass
        );
    }

    @Override
    default void setResultAsObject(final Object result) {
        setResponseResult(getClass(), this, result);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    default boolean hasError() {
        return hasOneThenEvaluateOrFalse(
                getClass(),
                this,
                IJsonrpcMessageHelper::getResponseError,
                evaluatingTrue()
        );
    }

    @Override
    default <T extends JsonrpcResponseMessageError> T getErrorAs(final Class<T> objectClass) {
        return hasOneThenMapAsObjectOrNull(
                getClass(),
                this,
                IJsonrpcMessageHelper::getResponseError,
                objectClass
        );
    }

    @Override
    default void setErrorAs(final JsonrpcResponseMessageError error) {
        setResponseError(getClass(), this, error);
    }

    @Override
    default JsonrpcResponseMessageError getErrorAsDefaultType() {
        return getErrorAs(MoshiJsonrpcResponseMessageError.class);
    }
}
