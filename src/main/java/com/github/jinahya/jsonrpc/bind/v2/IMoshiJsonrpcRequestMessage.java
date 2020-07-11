package com.github.jinahya.jsonrpc.bind.v2;

import com.squareup.moshi.JsonAdapter;

import javax.validation.constraints.AssertTrue;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import static com.squareup.moshi.Types.newParameterizedType;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

interface IMoshiJsonrpcRequestMessage<S extends IMoshiJsonrpcRequestMessage<S>>
        extends IMoshiJsonrpcMessage<S>,
                JsonrpcRequestMessage {

    @Override
    default boolean isNotification() {
        return JsonrpcRequestMessage.super.isNotification();
    }

}
