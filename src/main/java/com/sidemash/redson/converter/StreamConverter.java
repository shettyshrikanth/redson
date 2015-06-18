package com.sidemash.redson.converter;

import com.sidemash.redson.Json;
import com.sidemash.redson.JsonArray;
import com.sidemash.redson.JsonValue;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Stream;


public class StreamConverter<T>  implements  JsonContainerConverter<Stream<T>> {

    @Override
    public Stream<T> fromJsonValue(JsonValue jsonValue, Type type) {
        @SuppressWarnings("unchecked")
        Stream<T> result =  ((List<T>) Json.getJsonContainerReaderFunctionFor(List.class)
                .apply(jsonValue, type)).stream();
        return result;
    }

    @Override
    public JsonValue toJsonValue(Stream<T> obj, JsonValue jsonValue) {
        return JsonArray.of(obj);
    }
}
