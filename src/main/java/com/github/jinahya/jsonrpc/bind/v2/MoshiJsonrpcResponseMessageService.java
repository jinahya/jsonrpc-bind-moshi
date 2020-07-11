package com.github.jinahya.jsonrpc.bind.v2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static java.util.Objects.requireNonNull;

public class MoshiJsonrpcResponseMessageService implements JsonrpcResponseMessageService {

    @Override
    public JsonrpcResponseMessage fromJson(final Object source) {
        requireNonNull(source, "source is null");
        return MoshiJsonrpcMessageServiceHelper.fromJson(MoshiJsonrpcResponseMessage.class, source);
    }

    @Override
    public void toJson(final JsonrpcResponseMessage message, final Object target) {
        requireNonNull(message, "message is null");
        requireNonNull(target, "target is null");
        MoshiJsonrpcMessageServiceHelper.toJson(JsonrpcResponseMessage.class, target, message);
    }

    @Override
    public JsonrpcResponseMessage fromJsonString(final String string) {
        requireNonNull(string, "string is null");
        return fromJson(new ByteArrayInputStream(string.getBytes()));
    }

    @Override
    public String toJsonString(final JsonrpcResponseMessage message) {
        final ByteArrayOutputStream target = new ByteArrayOutputStream();
        toJson(message, target);
        return new String(target.toByteArray());
    }
}
