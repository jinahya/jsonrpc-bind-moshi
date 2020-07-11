package com.github.jinahya.jsonrpc.bind.v2;

import com.squareup.moshi.Json;

public class MoshiJsonrpcResponseMessage
        extends AbstractJsonrpcResponseMessage
        implements IMoshiJsonrpcResponseMessage<MoshiJsonrpcResponseMessage> {

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
