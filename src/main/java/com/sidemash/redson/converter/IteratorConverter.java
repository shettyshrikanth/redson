package com.sidemash.redson.converter;


import com.sidemash.redson.JsonArray;
import com.sidemash.redson.JsonValue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;

public class IteratorConverter<T> implements  JsonContainerConverter<Iterator<T>>{

    @Override
    public JsonValue toJsonValue(Iterator<T> obj, JsonValue jsonValue) {
        return JsonArray.of(obj);
    }


    @Override
    public Iterator<T> fromJsonValue(JsonValue jsonValue, Type type) {
        return new Iterator<T>() {
            JsonArray jsonArray = (JsonArray) jsonValue;
            final Type typeT = ((ParameterizedType) type).getActualTypeArguments()[0] ;

            @Override
            public boolean hasNext() {
                return jsonArray.isNotEmpty();
            }

            @Override
            public T next() {
                T next = jsonArray.getHead().asType(typeT);
                jsonArray = jsonArray.skip(1);
                return next;
            }
        };
    }

}
