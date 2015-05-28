package com.sidemash.redson.converter;

import com.sidemash.redson.JsonObject;
import com.sidemash.redson.JsonValue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public enum DefaultObjectConverter implements  JsonConverter<Object> {
    INSTANCE
    ;
    @Override
    public Map<String, Object> fromJsonValue(JsonValue jsonValue) {
        return jsonValue.asStringIndexedMapOf(Object.class);
    }

    @Override
    public JsonValue toJsonValue(Object obj, JsonValue jsonValue) {
        // Build a LinkedHashMap of the K/V properties of this object
        Map<String, Object> attributeMap = new LinkedHashMap<>();

        // first take non transient fields
        int modifier;
        Object value;
        List<Class<?>> classList = new ArrayList<>();
        Class<?> current =  obj.getClass();
        while (!current.equals(Object.class)) {
            classList.add(current);
            current = current.getSuperclass();
        }
        Collections.reverse(classList);
        try {
            for(Class<?> className : classList) {
                Field[] fields = className.getDeclaredFields();
                for (Field field : fields) {
                    modifier = field.getModifiers();
                    value = field.get(obj);
                    // If the field is not transient AND is not a reference to currentObject being analyzed
                    // the last part of this condition is to avoid circular reference;
                    // Modifier.toString is non optimized code. seed StringBuffer append sequence
                    if (((modifier & Modifier.TRANSIENT) == 0) &&
                        ((modifier & Modifier.STATIC) == 0) &&
                        (value != obj))
                        attributeMap.put(field.getName(), value);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return JsonObject.of(attributeMap);
    }
}
