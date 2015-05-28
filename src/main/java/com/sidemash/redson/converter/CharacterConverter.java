package com.sidemash.redson.converter;

import com.sidemash.redson.JsonString;
import com.sidemash.redson.JsonValue;

public enum CharacterConverter implements  JsonConverter<Character> {
    INSTANCE
    ;
    @Override
    public Character fromJsonValue(JsonValue jsonValue) {
        return jsonValue.asChar();
    }

    @Override
    public JsonValue toJsonValue(Character obj, JsonValue jsonValue) {
        return JsonString.of(obj);
    }
}
