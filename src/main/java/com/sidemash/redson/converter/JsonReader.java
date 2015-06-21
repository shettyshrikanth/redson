package com.sidemash.redson.converter;


import com.sidemash.redson.JsonValue;

import java.lang.reflect.Type;

public interface  JsonReader<T> {

    T fromJsonValue(JsonValue jsonValue);

    default T fromJsonValue(JsonValue jsonValue,  Type type){
        return  fromJsonValue(jsonValue);
    }

}
