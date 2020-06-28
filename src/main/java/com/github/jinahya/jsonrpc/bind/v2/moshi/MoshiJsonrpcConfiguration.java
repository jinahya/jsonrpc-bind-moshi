package com.github.jinahya.jsonrpc.bind.v2.moshi;

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

import com.squareup.moshi.Moshi;

import java.math.BigInteger;

import static java.util.Objects.requireNonNull;

/**
 * A configuration class for JSON-RPC 2.0.
 *
 * @author Jin Kwon &lt;onacit_at_gmail.com&gt;
 */
public final class MoshiJsonrpcConfiguration {

    private static Moshi moshi;

    /**
     * Returns current moshi instance.
     *
     * @return current moshi instance.
     */
    public static synchronized Moshi getMoshi() {
        return moshi;
    }

    /**
     * Replaces current moshi instance with specified value.
     *
     * @param moshi new moshi instance.
     */
    static synchronized void setMoshi(final Moshi moshi) {
        MoshiJsonrpcConfiguration.moshi = requireNonNull(moshi, "moshi is null");
    }

    /**
     * Replaces current moshi instance with specified value.
     *
     * @param builder new moshi instance.
     */
    public static synchronized void setMoshi(final Moshi.Builder builder) {
        builder.add(BigInteger.class, new BigIntegerAdapter());
        setMoshi(builder.build());
    }

    static {
        setMoshi(new Moshi.Builder());
    }

    private MoshiJsonrpcConfiguration() {
        throw new AssertionError("instantiation is not allowed");
    }
}
