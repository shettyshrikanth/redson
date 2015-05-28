package com.sidemash.redson.converter;


import com.sidemash.redson.JsonNumber;
import com.sidemash.redson.JsonValue;

public enum DoubleConverter implements  JsonConverter<Double> {
    INSTANCE
    ;

    @Override
    public Double fromJsonValue(JsonValue jsonValue) {
        return jsonValue.asDouble();
    }

    @Override
    public JsonValue toJsonValue(Double obj, JsonValue jsonValue) {
        return JsonNumber.of(obj);
    }
}
