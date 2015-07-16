package com.sidemash.redson;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

@SuppressWarnings("unused")
public class JsonCollector {

    public static<T>  Collector<T, JsonArray.Builder, JsonArray> toJsonArray() {
        return Collector.of(
                JsonArray::builder,
                JsonArray.Builder::append,
                JsonArray.Builder::append,
                JsonArray.Builder::build
        );
    }

    public static<V, T extends Map.Entry<String,V>>  Collector<T, JsonObject.Builder, JsonObject> toJsonObject() {
        return toJsonObject(Function.<String>identity());
    }


    public static <K, V, T extends Map.Entry<K,V>>  Collector<T, JsonObject.Builder, JsonObject> toJsonObject(Function<K, String> keyToStringFn) {
        return Collector.of(
                JsonObject::builder,
                (builder, elem) -> builder.append(
                        JsonEntry.of(keyToStringFn.apply(elem.getKey()), elem.getValue())
                ),
                JsonObject.Builder::append,
                JsonObject.Builder::build
        );
    }
}
