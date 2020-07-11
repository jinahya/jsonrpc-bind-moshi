package com.github.jinahya.jsonrpc.bind.v2;

/*-
 * #%L
 * jsonrpc-bind-jackson
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

import javax.validation.constraints.AssertTrue;
import java.math.BigDecimal;
import java.math.BigInteger;

import static com.github.jinahya.jsonrpc.bind.v2.IMoshiJsonrpcMessageHelper.setId;
import static com.github.jinahya.jsonrpc.bind.v2.IMoshiJsonrpcObjectHelper.*;
import static java.util.Optional.ofNullable;

interface IMoshiJsonrpcMessage<S extends IMoshiJsonrpcMessage<S>>
        extends JsonrpcMessage, IMoshiJsonrpcObject<S> {

    @Override
    default boolean hasId() {
        return hasOneThenEvaluateOrFalse(
                getClass(),
                this,
                IMoshiJsonrpcMessageHelper::getId,
                evaluatingTrue()
        );
    }

    @Override
    default @AssertTrue boolean isIdContextuallyValid() {
        return hasOneThenEvaluateOrTrue(
                getClass(),
                this,
                IMoshiJsonrpcMessageHelper::getId,
                id -> id instanceof CharSequence || id instanceof Number
        );
    }

    @Override
    default String getIdAsString() {
        return hasOneThenMapOrNull(
                getClass(),
                this,
                IMoshiJsonrpcMessageHelper::getId,
                id -> {
                    assert id != null;
                    if (id instanceof Number) {
                        try {
                            return new BigDecimal(id.toString()).toBigIntegerExact().toString();
                        } catch (final ArithmeticException ae) {
                            // suppressed
                        }
                    }
                    return id.toString();
                }
        );
    }

    @Override
    default void setIdAsString(final String id) {
        setId(getClass(), this, id);
    }

    @Override
    default BigInteger getIdAsNumber() {
        return hasOneThenMapOrNull(
                getClass(),
                this,
                IMoshiJsonrpcMessageHelper::getId,
                id -> {
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
        );
    }

    @Override
    default void setIdAsNumber(final BigInteger id) {
        setId(getClass(), this, id);
    }

    @Override
    default Long getIdAsLong() {
        return ofNullable(hasOneThenMapOrNull(
                getClass(),
                this,
                IMoshiJsonrpcMessageHelper::getId,
                id -> {
                    if (id instanceof Long) {
                        return (Long) id;
                    }
                    if (id instanceof Number) {
                        return new BigDecimal(id.toString()).toBigIntegerExact().longValueExact();
                    }
                    return null;
                }))
                .orElseGet(JsonrpcMessage.super::getIdAsLong);
    }

    @Override
    default void setIdAsLong(final Long id) {
        setId(getClass(), this, id);
    }

    @Override
    default Integer getIdAsInteger() {
        return ofNullable(hasOneThenMapOrNull(
                getClass(),
                this,
                IMoshiJsonrpcMessageHelper::getId,
                id -> {
                    if (id instanceof Integer) {
                        return (Integer) id;
                    }
                    if (id instanceof Number) {
                        return new BigDecimal(id.toString()).toBigIntegerExact().intValueExact();
                    }
                    return null;
                }))
                .orElseGet(JsonrpcMessage.super::getIdAsInteger);
    }

    @Override
    default void setIdAsInteger(final Integer id) {
        setId(getClass(), this, id);
    }
}

