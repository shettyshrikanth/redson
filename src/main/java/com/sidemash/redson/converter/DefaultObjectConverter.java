package com.sidemash.redson.converter;

import com.sidemash.redson.Json;
import com.sidemash.redson.JsonObject;
import com.sidemash.redson.JsonValue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.BiFunction;

public enum DefaultObjectConverter implements  JsonConverter<Object> {
    INSTANCE
    ;
    private static List<Field> getFieldsFor(Class<?> cl) {
        List<Field> result = new ArrayList<>();
        int modifier;
        Field[] fields =  cl.getDeclaredFields();
        for (Field field : fields) {
            //if (!field.isSynthetic())
            {
                modifier = field.getModifiers();
                // If the field is not transient AND is not a reference to currentObject being analyzed
                // Modifier.toString is non optimized code. seed StringBuffer append sequence
                if (!Modifier.isTransient(modifier) && !Modifier.isStatic(modifier))
                    result.add(field);
            }
        }
        return result;
    }

    @Override
    public Map<String, Object> fromJsonValue(JsonValue jsonValue) {
        return jsonValue.asStringIndexedMapOf(Object.class);
    }

    @Override
    public JsonValue toJsonValue(Object obj, JsonValue jsonValue) {

        Class<?> tmpClass = obj.getClass();
        if(tmpClass.equals(Object.class))
            return JsonObject.EMPTY;


        Object fieldValue;
        Map<String, Object> attributeMap = new LinkedHashMap<>();
        JsonValue result = JsonObject.EMPTY;

        try {
            // Recursively build a list of all class in "obj" class hierarchy
            List<Class<?>> classList = new ArrayList<>();
            while (!tmpClass.equals(Object.class)) {
                classList.add(tmpClass);
                tmpClass = tmpClass.getSuperclass();
            }

            // Reverse the list to walk within it from super to sub classes
            Collections.reverse(classList);

            BiFunction<Object, JsonValue, JsonValue> fnResult;
            for (Class<?> c : classList) {
                // System.out.println(c);
                // System.out.println("Recu : " + result.stringify());

                if(Json.isWriterRegisteredFor(c)){
                    fnResult = (BiFunction<Object, JsonValue, JsonValue>) Json.getJsonWriterFunctionFor(c);
                    result = fnResult.apply(obj, result);
                }
                else {
                    List<Field> fields = getFieldsFor(c);
                    // System.out.print("Fields : ");
                    attributeMap.clear();
                    for(Field field : fields) {
                        // System.out.print(field.getName() + ", ");
                        fieldValue = field.get(obj);
                        if (fieldValue != obj )
                            attributeMap.put(field.getName(), fieldValue);
                    }
                    // System.out.println("\nBefore : " + result.stringify());
                    result = result.union(JsonObject.of(attributeMap));
                    // System.out.println();
                }
                // System.out.println(result.prettyStringify());
                // System.out.println();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return result;
    }
}