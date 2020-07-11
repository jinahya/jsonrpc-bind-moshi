package com.github.jinahya.jsonrpc.bind.v2;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.ToJson;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

class BigIntegerAdapter extends JsonAdapter<BigInteger> {

    @FromJson
    @Override
    public BigInteger fromJson(final JsonReader reader) throws IOException {
        return new BigDecimal(reader.nextString()).toBigIntegerExact();
    }

    @ToJson
    @Override
    public void toJson(final JsonWriter writer, final BigInteger value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        writer.value(value.toString());
    }
}
