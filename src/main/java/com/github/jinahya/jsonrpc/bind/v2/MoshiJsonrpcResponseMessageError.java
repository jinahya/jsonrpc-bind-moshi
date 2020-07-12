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

import com.squareup.moshi.Json;

import javax.validation.constraints.AssertTrue;
import java.util.List;

import static com.github.jinahya.jsonrpc.bind.v2.IMoshiJsonrpcObjectHelper.getAsArray;
import static com.github.jinahya.jsonrpc.bind.v2.IMoshiJsonrpcObjectHelper.getAsObject;
import static java.util.Objects.requireNonNull;

public class MoshiJsonrpcResponseMessageError
        extends AbstractJsonrpcResponseMessageError
        implements IMoshiJsonrpcResponseMessageError<MoshiJsonrpcResponseMessageError>,
                   JsonrpcResponseMessageError {

    @Override
    public String toString() {
        return super.toString() + "{" +
               PROPERTY_NAME_DATA + "=" + data
               + "}";
    }

    // ------------------------------------------------------------------------------------------------------------ data
    @Override
    public boolean hasData() {
        return data != null;
    }

    @Override
    @AssertTrue
    public boolean isDataContextuallyValid() {
        return super.isDataContextuallyValid();
    }

    @Override
    public <T> List<T> getDataAsArray(final Class<T> elementClass) {
        requireNonNull(elementClass, "elementClass is null");
        if (!hasData()) {
            return null;
        }
        return getAsArray(elementClass, data);
    }

    @Override
    public void setDataAsArray(final List<?> data) {
        this.data = data;
    }

    @Override
    public <T> T getDataAsObject(final Class<T> objectClass) {
        requireNonNull(objectClass, "objectClass is null");
        if (!hasData()) {
            return null;
        }
        return getAsObject(objectClass, data);
    }

    @Override
    public void setDataAsObject(final Object data) {
        this.data = data;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Json(name = PROPERTY_NAME_DATA)
    private Object data;
}
