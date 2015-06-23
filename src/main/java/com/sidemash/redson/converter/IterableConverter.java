package com.sidemash.redson.converter;

import com.sidemash.redson.JsonArray;
import com.sidemash.redson.JsonValue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class IterableConverter<T> implements JsonContainerConverter<Iterable<T>> {

    @Override
    public Iterable<T> fromJsonValue(JsonValue jsonValue, Type type) {
        List<T> result = new ArrayList<>();
        JsonArray array = (JsonArray) jsonValue;
        ParameterizedType p = (ParameterizedType) type;
        for(JsonValue value : array)
            result.add(value.as(p.getActualTypeArguments()[0]));

        return result;
    }

    @Override
    public JsonValue toJsonValue(Iterable<T> obj, JsonValue jsonValue) {
        return JsonArray.of(obj);
    }
}
