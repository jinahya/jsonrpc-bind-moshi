package com.github.jinahya.jsonrpc.bind.v2;

import javax.validation.constraints.AssertTrue;
import java.util.List;

import static java.util.Objects.requireNonNull;

interface IMoshiJsonrpcResponseMessage<S extends IMoshiJsonrpcResponseMessage<S>>
        extends IMoshiJsonrpcMessage<S>,
                JsonrpcResponseMessage {

    @Override
    @AssertTrue
    default boolean isResultAndErrorExclusive() {
        return JsonrpcResponseMessage.super.isResultAndErrorExclusive();
    }

    // ---------------------------------------------------------------------------------------------------------- result
    @Override
    default boolean hasResult() {
        return IMoshiJsonrpcObjectHelper.hasOneThenEvaluateOrFalse(
                getClass(),
                this,
                IMoshiJsonrpcMessageHelper::getResponseResult,
                IMoshiJsonrpcObjectHelper.evaluatingTrue()
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
        return IMoshiJsonrpcObjectHelper.hasOneThenMapAsArrayOrNull(
                getClass(),
                this,
                IMoshiJsonrpcMessageHelper::getResponseResult,
                elementClass
        );
    }

    @Override
    default void setResultAsArray(final List<?> result) {
        IMoshiJsonrpcMessageHelper.setResponseResult(getClass(), this, result);
    }

    @Override
    default <T> T getResultAsObject(final Class<T> objectClass) {
        return IMoshiJsonrpcObjectHelper.hasOneThenMapAsObjectOrNull(
                getClass(),
                this,
                IMoshiJsonrpcMessageHelper::getResponseResult,
                objectClass
        );
    }

    @Override
    default void setResultAsObject(final Object result) {
        IMoshiJsonrpcMessageHelper.setResponseResult(getClass(), this, result);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    default boolean hasError() {
        return IMoshiJsonrpcObjectHelper.hasOneThenEvaluateOrFalse(
                getClass(),
                this,
                IMoshiJsonrpcMessageHelper::getResponseError,
                IMoshiJsonrpcObjectHelper.evaluatingTrue()
        );
    }

    @Override
    default JsonrpcResponseMessageError getErrorAs() {
        return null;
//        return hasOneThenMapAsObjectOrNull(
//                getClass(),
//                this,
//                IMoshiJsonrpcMessageHelper::getResponseError,
//                objectClass
//        );
    }

    @Override
    default void setErrorAs(final JsonrpcResponseMessageError error) {
        IMoshiJsonrpcMessageHelper.setResponseError(getClass(), this, error);
    }
}
