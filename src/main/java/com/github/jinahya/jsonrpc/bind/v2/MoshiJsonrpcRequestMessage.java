package com.github.jinahya.jsonrpc.bind.v2;

import com.github.jinahya.jsonrpc.bind.JsonrpcBindException;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Types;

import javax.validation.constraints.AssertTrue;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.jinahya.jsonrpc.bind.v2.IMoshiJsonrpcObjectHelper.wrap;
import static com.github.jinahya.jsonrpc.bind.v2.MoshiJsonrpcConfiguration.getMoshi;
import static com.squareup.moshi.Types.newParameterizedType;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;

public class MoshiJsonrpcRequestMessage
        extends AbstractJsonrpcRequestMessage
        implements IMoshiJsonrpcRequestMessage<MoshiJsonrpcRequestMessage> {

    @Override
    public String toString() {
        return super.toString() + "{"
               + PROPERTY_NAME_PARAMS + "=" + params
               + "," + PROPERTY_NAME_ID + "=" + id
               + "}";
    }

    // ---------------------------------------------------------------------------------------------------------- params
    @Override
    public boolean hasParams() {
        return params != null;
    }

    @Override
    @AssertTrue
    public boolean isParamsContextuallyValid() {
        if (!hasParams()) {
            return true;
        }
        if (true) {
            return true;
        }
        return params.getClass().isArray()
               || params instanceof List
               || params instanceof Map // com.squareup.moshi.LinkedHashTreeMap
                ;
    }

    @Override
    public <T> List<T> getParamsAsArray(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        if (!hasParams()) {
            return null;
        }
        if (params instanceof List) {
            final ParameterizedType type = newParameterizedType(List.class, elementClass.isPrimitive() ? wrap(elementClass) : elementClass);
            final JsonAdapter<List<T>> adapter = getMoshi().adapter(type);
            return adapter.fromJsonValue(params);
        }
        return new ArrayList<>(singletonList(getParamsAsObject(elementClass)));
    }

    @Override
    public void setParamsAsArray(final List<?> params) {
        this.params = params;
    }

    @Override
    public <T> T getParamsAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        if (!hasParams()) {
            return null;
        }
        if (params instanceof Map) {
            final JsonAdapter<T> adapter = getMoshi().adapter(objectClass);
            return adapter.fromJsonValue(params);
        }
        assert params instanceof List;
        if (objectClass.isArray()) {
            final Class<?> componentType = objectClass.getComponentType();
            final Type type = Types.newParameterizedType(
                    List.class, componentType.isPrimitive() ? wrap(componentType) : componentType);
            final JsonAdapter<List<?>> adapter = getMoshi().adapter(type);
            final List<?> list = adapter.fromJsonValue(params);
            assert list != null;
            final Object array = Array.newInstance(componentType, list.size());
            for (int i = 0; i < list.size(); i++) {
                Array.set(array, i, list.get(i));
            }
            return (T) array;
        }
        throw new JsonrpcBindException("unable to get params as an object; params: " + params);
    }

    @Override
    public void setParamsAsObject(final Object params) {
        this.params = params;
    }

    @Json(name = PROPERTY_NAME_ID)
    private Object id;

    @Json(name = PROPERTY_NAME_PARAMS)
    private Object params;
}
