package com.sidemash.redson.converter;

import com.sidemash.redson.JsonEntry;
import com.sidemash.redson.JsonObject;
import com.sidemash.redson.JsonString;
import com.sidemash.redson.JsonValue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;


public class MapConverter<K,V> implements JsonContainerConverter<Map<K, V>> {

    private final int KEY = 0;
    private final int VALUE = 1;

    @Override
    public Map<K, V> fromJsonValue(JsonValue jsonValue, Type type) {
        ParameterizedType p = (ParameterizedType) type;
        Type keyType = p.getActualTypeArguments()[KEY];

        if(keyType.getTypeName().equals("java.lang.String"))
            return (Map<K, V>) stringMapConvert(jsonValue, type);
        else
            return genericMapConvert(jsonValue, type);

    }

    private Map<K, V> genericMapConvert(JsonValue jsonValue, Type type) {
        Map<K, V> result = new LinkedHashMap<>();
        JsonObject obj = (JsonObject) jsonValue;
        ParameterizedType p = (ParameterizedType) type;
        for (JsonEntry<String> entry : obj)
            result.put(
                    JsonString.of(entry.getKey()).as(p.getActualTypeArguments()[KEY]),
                    entry.getValue().as(p.getActualTypeArguments()[VALUE])
            );

        return result;
    }

    public Map<String, V> stringMapConvert(JsonValue jsonValue, Type type){
        Map<String, V> result = new LinkedHashMap<>();
        JsonObject obj = (JsonObject) jsonValue;
        ParameterizedType p = (ParameterizedType) type;
        for(JsonEntry<String> entry : obj)
            result.put(entry.getKey(), entry.getValue().as(p.getActualTypeArguments()[VALUE]));

        return result;
    }

    @Override
    public JsonValue toJsonValue(Map<K, V> obj, JsonValue jsonValue) {
        return JsonObject.of(obj);
    }
}
