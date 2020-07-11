package com.github.jinahya.jsonrpc.bind.v2;

import javax.validation.constraints.AssertTrue;
import java.util.List;

import static java.util.Objects.requireNonNull;

// TODO: Move implementations to the concrete class!!!
interface IJsonrpcResponseMessageError<S extends IJsonrpcResponseMessageError<S>>
        extends IMoshiJsonrpcObject<S>,
                JsonrpcResponseMessageError {

    @Override
    default boolean hasData() {
        return IMoshiJsonrpcObjectHelper.hasOneThenEvaluateOrTrue(
                getClass(),
                this,
                IMoshiJsonrpcMessageHelper::getResponseErrorData,
                IMoshiJsonrpcObjectHelper.evaluatingTrue()
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
        return IMoshiJsonrpcObjectHelper.hasOneThenMapAsArrayOrNull(
                getClass(),
                this,
                IMoshiJsonrpcMessageHelper::getResponseErrorData,
                elementClass
        );
    }

    @Override
    default void setDataAsArray(final List<?> data) {
        IMoshiJsonrpcMessageHelper.setResponseErrorData(getClass(), this, data);
    }

    @Override
    default <T> T getDataAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        return IMoshiJsonrpcObjectHelper.hasOneThenMapAsObjectOrNull(
                getClass(),
                this,
                IMoshiJsonrpcMessageHelper::getResponseErrorData,
                objectClass
        );
    }

    @Override
    default void setDataAsObject(final Object data) {
        IMoshiJsonrpcMessageHelper.setResponseErrorData(getClass(), this, data);
    }
}
