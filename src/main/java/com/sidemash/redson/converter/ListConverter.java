package com.sidemash.redson.converter;


import com.sidemash.redson.JsonArray;
import com.sidemash.redson.JsonEntry;
import com.sidemash.redson.JsonValue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class ListConverter<T> implements JsonContainerConverter<List<T>> {

    @Override
    public List<T> fromJsonValue(JsonValue jsonValue, Type type){
        List<T> result = new ArrayList<>();
        JsonArray array = (JsonArray) jsonValue;
        ParameterizedType p = (ParameterizedType) type;
        for(JsonEntry<Integer> entry : array)
            result.add(entry.getValue().as(p.getActualTypeArguments()[0]));

        return result;
    }

    @Override
    public JsonValue toJsonValue(List<T> obj, JsonValue jsonValue) {
        return JsonArray.of(obj);
    }
}
