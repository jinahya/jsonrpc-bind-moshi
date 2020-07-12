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
        if (!(message instanceof MoshiJsonrpcResponseMessage)) {
            throw new IllegalArgumentException(
                    "message(" + message + ") is not an instance of " + MoshiJsonrpcResponseMessage.class);
        }
        MoshiJsonrpcMessageServiceHelper.toJson(
                MoshiJsonrpcResponseMessage.class, target, (MoshiJsonrpcResponseMessage) message);
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
