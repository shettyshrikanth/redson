package com.sidemash.redson.converter;


import com.sidemash.redson.JsonString;
import com.sidemash.redson.JsonValue;

public enum StringConverter implements  JsonConverter<String> {
    INSTANCE
    ;
    @Override
    public String fromJsonValue(JsonValue jsonValue) {
        return jsonValue.asString();
    }

    @Override
    public JsonValue toJsonValue(String obj, JsonValue jsonValue) {
        return JsonString.of(obj);
    }
}
