package com.github.jinahya.jsonrpc.bind.v2.moshi;

import com.github.jinahya.jsonrpc.bind.v2.AbstractJsonrpcResponseMessageError;
import com.github.jinahya.jsonrpc.bind.v2.JsonrpcResponseMessageError;
import com.squareup.moshi.Json;

public class MoshiJsonrpcResponseMessageError
        extends AbstractJsonrpcResponseMessageError
        implements IJsonrpcResponseMessageError<MoshiJsonrpcResponseMessageError>,
                   JsonrpcResponseMessageError {

    @Override
    public String toString() {
        return super.toString() + "{" +
               PROPERTY_NAME_DATA + "=" + data
               + "}";
    }

    @Json(name = PROPERTY_NAME_DATA)
    private Object data;
}
