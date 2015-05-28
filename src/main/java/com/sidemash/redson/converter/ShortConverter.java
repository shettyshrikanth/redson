package com.sidemash.redson.converter;


import com.sidemash.redson.JsonNumber;
import com.sidemash.redson.JsonValue;

public enum ShortConverter implements  JsonConverter<Short> {
    INSTANCE
    ;
    @Override
    public Short fromJsonValue(JsonValue jsonValue) {
        return jsonValue.asShort();
    }

    @Override
    public JsonValue toJsonValue(Short obj, JsonValue jsonValue) {
        return JsonNumber.of(obj);
    }
}
