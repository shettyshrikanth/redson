package com.sidemash.redson.converter;

import com.sidemash.redson.Json;
import com.sidemash.redson.JsonOptional;
import com.sidemash.redson.JsonValue;

import java.util.Optional;

public class OptionalConverter {
    public static JsonValue toJsonValue(Object opt, JsonValue jsonValue){
        Optional<?> value = (Optional<?>) opt;
        JsonValue result = JsonOptional.EMPTY;
        if (value.isPresent()) {
            result =  JsonOptional.of(Json.toJsonValue(value.get()));
        }
        return result;
    }

    public static Optional<?> fromJsonValue(JsonValue jsonValue){
        Optional<?> result;
        if (jsonValue.isJsonNull() || (jsonValue.isJsonOptional() && jsonValue.isEmpty()) ) {
            result = Optional.empty();
        }
        else {
            result = Optional.of(Json.fromJsonValue(jsonValue.asOptional().get()));
        }
        return result;
    }
}
