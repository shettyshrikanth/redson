package com.sidemash.redson.converter;

import com.sidemash.redson.Json;
import com.sidemash.redson.JsonOptional;
import com.sidemash.redson.JsonValue;

import java.util.Optional;

public class OptionalConverter implements JsonConverter<Optional<?>> {

    @Override
    public JsonValue toJsonValue(Optional<?> obj, JsonValue jsonValue) {
        Optional<?> value = (Optional<?>) obj;
        JsonValue result = JsonOptional.EMPTY;
        if (value.isPresent()) {
            result =  JsonOptional.of(Json.toJsonValue(value.get()));
        }
        return result;
    }

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
}
