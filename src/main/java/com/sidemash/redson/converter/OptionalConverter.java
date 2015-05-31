package com.sidemash.redson.converter;

import com.sidemash.redson.Json;
import com.sidemash.redson.JsonOptional;
import com.sidemash.redson.JsonValue;

import java.util.Optional;

public enum OptionalConverter implements JsonConverter<Optional<?>> {

    INSTANCE
    ;
    @Override
    public Optional<?> fromJsonValue(JsonValue jsonValue) {
        Optional<?> result;
        if (jsonValue.isJsonNull() || (jsonValue.isJsonOptional() && jsonValue.isEmpty()) ) {
            result = Optional.empty();
        }
        else {
            result = Optional.of(Json.fromJsonValue(jsonValue.asOptional().get()));
        }
        return result;
    }

    @Override
    public JsonValue toJsonValue(Optional<?> obj, JsonValue jsonValue) {
         JsonValue result = JsonOptional.EMPTY;
        if (obj.isPresent()) {
            result =  JsonOptional.of(Json.toJsonValue(obj.get()));
        }
        return result;
    }
}
