package com.github.jinahya.jsonrpc.bind.v2.moshi;

import com.squareup.moshi.JsonAdapter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.github.jinahya.jsonrpc.bind.v2.moshi.MoshiJsonrpcConfiguration.getMoshi;

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
        final JsonAdapter<A> adapter = getMoshi().adapter(A.class);
        final A a = adapter.fromJson(json);
        log.debug("a.value1: {} {}", a.value1, a.value1.getClass());
        log.debug("a.value2: {} {}", a.value2, a.value2.getClass());
        log.debug("a.value3: {} {}", a.value3, a.value3.getClass());
        log.debug("a.value4: {} {}", a.value4, a.value4.getClass());
        final Sub sub = getMoshi().adapter(Sub.class).fromJsonValue(a.value4);
        log.debug("sub: {}", sub);
    }
}
