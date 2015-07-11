package com.sidemash.redson.converter;

import com.sidemash.redson.JsonArray;
import com.sidemash.redson.JsonValue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;


public class SetConverter<T> implements JsonContainerConverter<Set<T>>  {

    @Override
    public Set<T> fromJsonValue(JsonValue jsonValue, Type type) {
        Set<T> result = new HashSet<>();
        JsonArray array = (JsonArray) jsonValue;
        ParameterizedType p = (ParameterizedType) type;
        for (JsonValue value : array)
            result.add(value.asType(p.getActualTypeArguments()[0]));

        return result;
    }

    @Override
    public JsonValue toJsonValue(Set<T> obj, JsonValue jsonValue) {
        return JsonArray.of(obj);
    }
}
