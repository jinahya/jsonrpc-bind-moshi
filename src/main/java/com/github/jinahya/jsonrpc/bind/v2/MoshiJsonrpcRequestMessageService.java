package com.github.jinahya.jsonrpc.bind.v2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static java.util.Objects.requireNonNull;

public class MoshiJsonrpcRequestMessageService implements JsonrpcRequestMessageService {

    @Override
    public JsonrpcRequestMessage fromJson(final Object source) {
        requireNonNull(source, "source is null");
        return MoshiJsonrpcMessageServiceHelper.fromJson(MoshiJsonrpcRequestMessage.class, source);
    }

    @Override
    public void toJson(final JsonrpcRequestMessage message, final Object target) {
        requireNonNull(message, "message is null");
        requireNonNull(target, "target is null");
        if (!(message instanceof MoshiJsonrpcRequestMessage)) {
            throw new IllegalArgumentException(
                    "message(" + message + ") is not an instance of " + MoshiJsonrpcRequestMessage.class);
        }
        MoshiJsonrpcMessageServiceHelper.toJson(
                MoshiJsonrpcRequestMessage.class, target, (MoshiJsonrpcRequestMessage) message);
    }

    @Override
    public JsonrpcRequestMessage fromJsonString(final String string) {
        requireNonNull(string, "string is null");
        return fromJson(new ByteArrayInputStream(string.getBytes()));
    }

    @Override
    public String toJsonString(final JsonrpcRequestMessage message) {
        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        toJson(message, target);
        return new String(target.toByteArray());
    }
}
