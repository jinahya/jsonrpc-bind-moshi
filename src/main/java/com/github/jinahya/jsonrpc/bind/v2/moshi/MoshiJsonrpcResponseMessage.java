package com.github.jinahya.jsonrpc.bind.v2.moshi;

import com.github.jinahya.jsonrpc.bind.v2.AbstractJsonrpcResponseMessage;
import com.squareup.moshi.Json;

import java.io.IOException;
import java.io.UncheckedIOException;

import static java.util.Objects.requireNonNull;

public class MoshiJsonrpcResponseMessage
        extends AbstractJsonrpcResponseMessage
        implements IJsonrpcResponseMessage<MoshiJsonrpcResponseMessage> {

    public static <T extends MoshiJsonrpcResponseMessage> T fromJson(final Class<T> clazz, final Object source) {
        requireNonNull(clazz, "clazz is null");
        requireNonNull(source, "source is null");
        try {
            return MoshiJsonrpcMessages.fromJson(clazz, source);
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    public static MoshiJsonrpcResponseMessage fromJson(final Object source) {
        return fromJson(MoshiJsonrpcResponseMessage.class, source);
    }

    @Override
    public String toString() {
        return super.toString() + "{" +
               PROPERTY_NAME_ERROR + "=" + error
               + "," + PROPERTY_NAME_RESULT + "=" + result
               + "," + PROPERTY_NAME_ID + "=" + id
               + "}";
    }

    @Json(name = PROPERTY_NAME_ERROR)
    private Object error;

    @Json(name = PROPERTY_NAME_RESULT)
    private Object result;

    @Json(name = PROPERTY_NAME_ID)
    private Object id;
}
