package com.sidemash.redson.converter;


import com.sidemash.redson.JsonBoolean;
import com.sidemash.redson.JsonValue;

public class BooleanConverter implements  JsonConverter<Boolean> {
    @Override
    public Boolean fromJsonValue(JsonValue jsonValue) {
        return jsonValue.asBoolean();
    }

    @Override
    public JsonValue toJsonValue(Boolean obj, JsonValue jsonValue) {
        return JsonBoolean.of(obj);
    }
}
