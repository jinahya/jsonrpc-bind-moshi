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

import com.squareup.moshi.JsonAdapter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@Slf4j
class MoshiTest {

    static class A {

        Object value1;

        Object value2;

        Object value3;

        Object value4;
    }

    @ToString
    static class Sub {

        String name;

        int age;
    }

    @Test
    void test1() throws IOException {
        final String json = "{\"value1\":\"aaa\",\"value2\":1,\"value3\":[1,2],\"value4\":{\"name\":null,\"age\":10}}";
        final JsonAdapter<A> adapter = MoshiJsonrpcConfiguration.getMoshi().adapter(A.class);
        final A a = adapter.fromJson(json);
        log.debug("a.value1: {} {}", a.value1, a.value1.getClass());
        log.debug("a.value2: {} {}", a.value2, a.value2.getClass());
        log.debug("a.value3: {} {}", a.value3, a.value3.getClass());
        log.debug("a.value4: {} {}", a.value4, a.value4.getClass());
        final Sub sub = MoshiJsonrpcConfiguration.getMoshi().adapter(Sub.class).fromJsonValue(a.value4);
        log.debug("sub: {}", sub);
    }
}
