package com.sidemash.redson.converter;


import com.sidemash.redson.JsonValue;

public interface  JsonReader<T> {

    T fromJsonValue(JsonValue jsonValue);
}
