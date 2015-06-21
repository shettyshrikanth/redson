package com.sidemash.redson.converter;


import com.sidemash.redson.JsonNumber;
import com.sidemash.redson.JsonValue;

public enum LongConverter implements  JsonConverter<Long> {
    INSTANCE
    ;
    @Override
    public Long fromJsonValue(JsonValue jsonValue) {
        return jsonValue.asLong();
    }

    @Override
    public JsonValue toJsonValue(Long obj, JsonValue jsonValue) {
        return JsonNumber.of(obj);
    }
}
