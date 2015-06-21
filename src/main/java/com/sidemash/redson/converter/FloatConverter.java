package com.sidemash.redson.converter;


import com.sidemash.redson.JsonNumber;
import com.sidemash.redson.JsonValue;

public enum FloatConverter implements  JsonConverter<Float> {
    INSTANCE
    ;
    @Override
    public Float fromJsonValue(JsonValue jsonValue) {
        return jsonValue.asFloat();
    }

    @Override
    public JsonValue toJsonValue(Float obj, JsonValue jsonValue) {
        return JsonNumber.of(obj);
    }
}
