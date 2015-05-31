package com.sidemash.redson.converter;


import com.sidemash.redson.JsonNull;
import com.sidemash.redson.JsonValue;

public enum JsonValueConverter implements JsonConverter<JsonValue>{
    INSTANCE
    ;
    @Override
    public JsonValue fromJsonValue(JsonValue jsonValue) {
        return jsonValue;
    }

    @Override
    public JsonValue toJsonValue(JsonValue obj, JsonValue jsonValue) {
        return obj == null ? JsonNull.INSTANCE : obj;
    }
}
