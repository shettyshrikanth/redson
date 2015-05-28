package com.sidemash.redson.converter;

import com.sidemash.redson.JsonObject;
import com.sidemash.redson.JsonValue;

import java.util.Map;


public enum DefaultMapConverter implements  JsonConverter<Map<String, ?>> {
    INSTANCE
    ;
    @Override
    public Map<String, Object> fromJsonValue(JsonValue jsonValue) {
        return jsonValue.asStringIndexedMapOf(Object.class);
    }

    @Override
    public JsonValue toJsonValue(Map<String, ?> obj, JsonValue jsonValue) {
        return JsonObject.of(obj);
    }
}
