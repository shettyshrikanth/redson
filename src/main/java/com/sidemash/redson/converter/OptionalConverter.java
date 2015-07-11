package com.sidemash.redson.converter;

import com.sidemash.redson.JsonOptional;
import com.sidemash.redson.JsonValue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public class OptionalConverter<T> implements JsonContainerConverter<Optional<T>> {

    @Override
    public Optional<T> fromJsonValue(JsonValue jsonValue,Type type) {
        Optional<T> result;
        if (jsonValue.isJsonNull() || (jsonValue.isJsonOptional() && jsonValue.isEmpty()) ) {
            result = Optional.empty();
        }
        else if(jsonValue.isJsonOptional()){
            JsonOptional jsOpt = (JsonOptional) jsonValue;
            ParameterizedType p = (ParameterizedType) type;
            result = Optional.of(
                    jsOpt.get().asType(p.getActualTypeArguments()[0])
            );
        } else {
            ParameterizedType p = (ParameterizedType) type;
            result = Optional.of(
                    jsonValue.asType(p.getActualTypeArguments()[0])
            );
        }
        return result;
    }


    @Override
    public JsonValue toJsonValue(Optional<T> optional, JsonValue jsonValue) {
        return optional
                .map(elem -> JsonOptional.of(JsonValue.of(elem)))
                .orElse(JsonOptional.EMPTY);
    }
}
