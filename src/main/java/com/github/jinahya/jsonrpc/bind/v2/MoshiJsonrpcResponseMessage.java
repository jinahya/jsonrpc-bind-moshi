package com.github.jinahya.jsonrpc.bind.v2;

/*-
 * #%L
 * jsonrpc-bind-moshi
 * %%
 * Copyright (C) 2019 - 2020 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.github.jinahya.jsonrpc.bind.JsonrpcBindException;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;

import javax.validation.constraints.AssertTrue;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.IMoshiJsonrpcObjectHelper.getAsObject;
import static com.github.jinahya.jsonrpc.bind.v2.MoshiJsonrpcConfiguration.getMoshi;
import static java.util.Objects.requireNonNull;

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

    // -------------------------------------------------------------------------------------------------------------- id
    @Override
    public boolean hasId() {
        return id != null;
    }

    @Override
    @AssertTrue
    public boolean isIdContextuallyValid() {
        if (!hasId()) {
            return true;
        }
        return id instanceof String || id instanceof Number;
    }

    @Override
    public String getIdAsString() {
        if (!hasId()) {
            return null;
        }
        if (id instanceof Number) {
            try {
                return new BigDecimal(id.toString()).toBigIntegerExact().toString();
            } catch (final ArithmeticException ae) {
                // suppressed
            }
        }
        return id.toString();
    }

    @Override
    public void setIdAsString(final String id) {
        this.id = id;
    }

    @Override
    public BigInteger getIdAsNumber() {
        if (id instanceof BigInteger) {
            return (BigInteger) id;
        }
        if (id instanceof BigDecimal) {
            return ((BigDecimal) id).toBigIntegerExact();
        }
        if (id instanceof Number) {
            return new BigDecimal(id.toString()).toBigIntegerExact();
        }
        try {
            return new BigDecimal(getIdAsString()).toBigIntegerExact();
        } catch (final NumberFormatException nfe) {
            // suppressed
        }
        throw new JsonrpcBindException("unable to bind id as a number");
    }

    @Override
    public void setIdAsNumber(final BigInteger id) {
        this.id = id;
    }

    @Override
    public Long getIdAsLong() {
        if (!hasId()) {
            return null;
        }
        if (id instanceof Long) {
            return (Long) id;
        }
        if (id instanceof Number) {
            return new BigDecimal(id.toString()).toBigIntegerExact().longValueExact();
        }
        return super.getIdAsLong();
    }

    @Override
    public void setIdAsLong(final Long id) {
        this.id = id;
    }

    @Override
    public Integer getIdAsInteger() {
        if (!hasId()) {
            return null;
        }
        if (id instanceof Integer) {
            return (Integer) id;
        }
        if (id instanceof Number) {
            return new BigDecimal(id.toString()).toBigIntegerExact().intValueExact();
        }
        return super.getIdAsInteger();
    }

    @Override
    public void setIdAsInteger(final Integer id) {
        this.id = id;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    @AssertTrue
    public boolean isResultAndErrorExclusive() {
        return super.isResultAndErrorExclusive();
    }

    // ---------------------------------------------------------------------------------------------------------- result
    @Override
    public boolean hasResult() {
        return result != null;
    }

    @Override
    @AssertTrue
    public boolean isResultContextuallyValid() {
        return super.isResultContextuallyValid();
    }

    @Override
    public <T> List<T> getResultAsArray(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        if (!hasResult()) {
            return null;
        }
        return IMoshiJsonrpcObjectHelper.getAsArray(elementClass, result);
    }

    @Override
    public void setResultAsArray(final List<?> result) {
        this.result = result;
        if (hasResult()) {
            error = null;
        }
    }

    @Override
    public <T> T getResultAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        if (!hasResult()) {
            return null;
        }
        return getAsObject(objectClass, result);
    }

    @Override
    public void setResultAsObject(final Object result) {
        this.result = result;
        if (hasResult()) {
            error = null;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Override
    public boolean hasError() {
        return error != null;
    }

    @Override
    public JsonrpcResponseMessageError getErrorAs() {
        if (!hasError()) {
            return null;
        }
        final JsonAdapter<MoshiJsonrpcResponseMessageError> adapter
                = getMoshi().adapter(MoshiJsonrpcResponseMessageError.class);
        return adapter.fromJsonValue(error);
    }

    @Override
    public void setErrorAs(final JsonrpcResponseMessageError error) {
        this.error = error;
        if (hasError()) {
            result = null;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Json(name = PROPERTY_NAME_ID)
    private Object id;

    @Json(name = PROPERTY_NAME_RESULT)
    private Object result;

    @Json(name = PROPERTY_NAME_ERROR)
    private Object error;
}
