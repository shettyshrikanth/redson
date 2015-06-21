package com.sidemash.redson.converter;


import com.sidemash.redson.JsonValue;

public interface JsonContainerConverter<T> extends JsonConverter<T>{

    @Override
    default T fromJsonValue(JsonValue jsonValue) {
        throw new AssertionError(String.format("This method should have never been called in class %s ", this.getClass()));
    }

}
