package com.github.jinahya.jsonrpc.bind.v2.moshi;

import com.github.jinahya.jsonrpc.bind.v2.AbstractJsonrpcRequestMessage;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

public class MoshiJsonrpcRequestMessage
        extends AbstractJsonrpcRequestMessage
        implements IJsonrpcRequestMessage<MoshiJsonrpcRequestMessage> {

    static {
        //adapter(MoshiJsonrpcRequestMessage.class);
    }

    public static <T extends MoshiJsonrpcRequestMessage> T fromJson(final Class<T> clazz, final Object source)
            throws IOException {
        requireNonNull(clazz, "clazz is null");
        requireNonNull(source, "source is null");
        return MoshiJsonrpcMessages.fromJson(clazz, source);
    }

    public static MoshiJsonrpcRequestMessage fromJson(final Object source) throws IOException {
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
