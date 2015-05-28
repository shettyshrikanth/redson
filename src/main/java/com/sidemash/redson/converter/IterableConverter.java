package com.sidemash.redson.converter;

import com.sidemash.redson.JsonArray;
import com.sidemash.redson.JsonValue;


public enum IterableConverter implements  JsonConverter<Iterable<?>> {

    INSTANCE
    ;
    @Override
    public Iterable<Object> fromJsonValue(JsonValue jsonValue) {
        return jsonValue.asListOf(Object.class);
    }

    @Override
    public JsonValue toJsonValue(Iterable<?> obj, JsonValue jsonValue) {
        return JsonArray.of(obj);
    }
}
