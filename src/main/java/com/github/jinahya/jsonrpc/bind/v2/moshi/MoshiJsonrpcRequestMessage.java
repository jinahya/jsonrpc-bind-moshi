package com.github.jinahya.jsonrpc.bind.v2.moshi;

import com.github.jinahya.jsonrpc.bind.v2.AbstractJsonrpcRequestMessage;

import java.io.IOException;
import java.io.UncheckedIOException;

import static java.util.Objects.requireNonNull;

public class MoshiJsonrpcRequestMessage
        extends AbstractJsonrpcRequestMessage
        implements IJsonrpcRequestMessage<MoshiJsonrpcRequestMessage> {

    public static <T extends MoshiJsonrpcRequestMessage> T fromJson(final Class<T> clazz, final Object source) {
        requireNonNull(clazz, "clazz is null");
        requireNonNull(source, "source is null");
        try {
            return MoshiJsonrpcMessages.fromJson(clazz, source);
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    public static MoshiJsonrpcRequestMessage fromJson(final Object source) {
        return fromJson(MoshiJsonrpcRequestMessage.class, source);
    }

    @Override
    public String toString() {
        return super.toString() + "{"
               + PROPERTY_NAME_PARAMS + "=" + params
               + "," + PROPERTY_NAME_ID + "=" + id
               + "}";
    }

    private Object params;

    private Object id;
}
