package com.sidemash.redson.converter;


import com.sidemash.redson.JsonString;
import com.sidemash.redson.JsonValue;

public enum EnumConverter implements JsonConverter<Enum<?>> {
    INSTANCE;

    @Override
    public Enum<?> fromJsonValue(JsonValue jsonValue) {
        return null;
    }

    @Override
    public JsonValue toJsonValue(Enum<?> obj, JsonValue jsonValue) {
        return JsonString.of(obj.toString());
    }
}
