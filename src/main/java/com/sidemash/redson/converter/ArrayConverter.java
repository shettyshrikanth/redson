package com.sidemash.redson.converter;

import com.sidemash.redson.JsonArray;
import com.sidemash.redson.JsonValue;
import java.lang.reflect.Array;
import java.lang.reflect.Type;

@SuppressWarnings("rawtypes")
public enum ArrayConverter implements JsonConverter {
    INSTANCE ;

    @Override
    public Object fromJsonValue(JsonValue jsonValue) {
        throw new AssertionError(String.format("This method should have never been called in class %s ", this.getClass()));
    }

    @Override
    public Object fromJsonValue(JsonValue jsonValue, Type type) {
        JsonArray jsonArray = (JsonArray) jsonValue;
        Class<?> componentType = ((Class<?>) type).getComponentType();
        Object result = Array.newInstance(componentType, jsonArray.length());
        jsonArray.entryStream().forEach(entry ->
                        Array.set(result, entry.getKey(), entry.getValue().asType(componentType))
        );
        return result;
    }

    @Override
    public JsonValue toJsonValue(Object objArray, JsonValue jsonValue) {
        int objArrayLength = Array.getLength(objArray);
        if(objArrayLength == 0)
            return JsonArray.EMPTY;

        JsonArray.Builder builder = JsonArray.builder();
        for(int i = 0; i < objArrayLength; i++){
            builder.append(JsonValue.of(Array.get(objArray, i)));
        }
        return builder.build();
    }

}
