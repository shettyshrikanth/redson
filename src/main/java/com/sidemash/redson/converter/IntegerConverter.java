package com.sidemash.redson.converter;


import com.sidemash.redson.JsonNumber;
import com.sidemash.redson.JsonValue;

public enum IntegerConverter implements  JsonConverter<Integer> {
    INSTANCE
    ;
    @Override
    public Integer fromJsonValue(JsonValue jsonValue) {
        return jsonValue.asInt();
    }

    @Override
    public JsonValue toJsonValue(Integer obj, JsonValue jsonValue) {
        return JsonNumber.of(obj);
    }
}
