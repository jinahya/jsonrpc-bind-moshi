package com.github.jinahya.jsonrpc.bind.v2.moshi;

import com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessageError;

import javax.validation.constraints.AssertTrue;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.moshi.IJsonrpcMessageHelper.setResponseErrorData;
import static com.github.jinahya.jsonrpc.bind.v2.moshi.IJsonrpcObjectHelper.evaluatingTrue;
import static com.github.jinahya.jsonrpc.bind.v2.moshi.IJsonrpcObjectHelper.hasOneThenEvaluateOrTrue;
import static com.github.jinahya.jsonrpc.bind.v2.moshi.IJsonrpcObjectHelper.hasOneThenMapAsArrayOrNull;
import static com.github.jinahya.jsonrpc.bind.v2.moshi.IJsonrpcObjectHelper.hasOneThenMapAsObjectOrNull;
import static java.util.Objects.requireNonNull;

// TODO: Move implementations to the concrete class!!!
interface IJsonrpcResponseMessageError<S extends IJsonrpcResponseMessageError<S>>
        extends IJsonrpcObject<S>,
                JsonrpcResponseMessageError {

    @Override
    default boolean hasData() {
        return hasOneThenEvaluateOrTrue(
                getClass(),
                this,
                IJsonrpcMessageHelper::getResponseErrorData,
                evaluatingTrue()
        );
    }

    @Override
    @AssertTrue
    default boolean isDataContextuallyValid() {
        return JsonrpcResponseMessageError.super.isDataContextuallyValid();
    }

    @Override
    default <T> List<T> getDataAsArray(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        return hasOneThenMapAsArrayOrNull(
                getClass(),
                this,
                IJsonrpcMessageHelper::getResponseErrorData,
                elementClass
        );
    }

    @Override
    default void setDataAsArray(final List<?> data) {
        setResponseErrorData(getClass(), this, data);
    }

    @Override
    default <T> T getDataAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        return hasOneThenMapAsObjectOrNull(
                getClass(),
                this,
                IJsonrpcMessageHelper::getResponseErrorData,
                objectClass
        );
    }

    @Override
    default void setDataAsObject(final Object data) {
        setResponseErrorData(getClass(), this, data);
    }
}
