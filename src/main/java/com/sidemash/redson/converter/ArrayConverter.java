package com.sidemash.redson.converter;

import com.sidemash.redson.JsonArray;
import com.sidemash.redson.JsonValue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ArrayConverter<T> implements JsonContainerConverter<T[]> {

    @Override
    public T[] fromJsonValue(JsonValue jsonValue,Type type) {
        List<T> list = new ArrayList<>();
        JsonArray array = (JsonArray) jsonValue;
        ParameterizedType p = (ParameterizedType) type;
        for(JsonValue value : array)
            list.add(value.as(p.getActualTypeArguments()[0]));

        @SuppressWarnings("unchecked")
        T[] result = (T[]) list.toArray();
        return result;
    }

    @Override
    public JsonValue toJsonValue(T[] obj, JsonValue jsonValue) {
        return JsonArray.of(obj);
    }
}
