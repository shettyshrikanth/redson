package com.sidemash.redson;


import scala.Tuple2;
import scala.collection.immutable.Map;
import scala.collection.immutable.Map$;
import scala.collection.mutable.Builder;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonObject implements JsonStructure, Iterable<JsonEntry<String>> {

    public static final JsonObject EMPTY = new JsonObject(Map$.MODULE$.empty());
    private final Map<String, JsonValue> bindings;

    private JsonObject(Map<String, JsonValue> bindings) {
        this.bindings = bindings;
    }

    public static JsonObject of(String key, JsonValue jsonValue){
        Tuple2<String, JsonValue> tuple = new Tuple2<>(key, jsonValue);
        Map<String, JsonValue> map = Map$.MODULE$.<String, JsonValue>empty();

        return new JsonObject(map.$plus(tuple));
    }

    public static JsonObject of(String key, JsonObject jsonValue1, JsonObject... jsonValues){
        JsonObject root = jsonValue1.unionAll(Arrays.asList(jsonValues));
        return JsonObject.of(key, root);
    }

    public static JsonObject of(JsonObject jsonValue1, JsonObject... jsonValues){
        return jsonValue1.unionAll(Arrays.asList(jsonValues));
    }

    @Override
    public JsonValue append(JsonValue jsValue) {
        throw new UnsupportedOperationException("This operation is not supported for JsonObject.");
    }

    @Override
    public JsonValue append(String key, JsonValue jsValue) {
        Tuple2<String, JsonValue> tuple = new Tuple2<>(key, jsValue);
        return new JsonObject(bindings.$plus(tuple));
    }

    @Override
    public JsonValue appendIfAbsent(String key, JsonValue jsValue) {
        if(bindings.contains(key))
            return this;
        else
            return append(key, jsValue);
    }

    @Override
    public boolean containsAll(List<? extends JsonValue> jsValues) {
        // FIXME find a better way
        for (JsonValue jsonValue: jsValues) {
            if(!containsValue(jsonValue))
                return false;
        }
        return true;
    }

    @Override
    public boolean containsKey(String key) {
        return bindings.contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        scala.collection.Iterator<JsonValue> iterator = bindings.valuesIterator();
        while(iterator.hasNext()){
            if(iterator.next().equals(value))
                return true;
        }
        return false;
    }

    @Override
    public boolean isDefinedAt(int index) {
        return false;
    }

    @Override
    public boolean isDefinedAt(String key) {
        return bindings.isDefinedAt(key);
    }

    @Override
    public boolean isEmpty(){
        return bindings.isEmpty();
    }

    @Override
    public boolean isJsonArray() {
        return false;
    }

    @Override
    public boolean isJsonObject() {
        return true;
    }

    public Iterator<JsonEntry<String>> iterator() {
        return new Iterator<JsonEntry<String>>() {

            scala.collection.Iterator<Tuple2<String, JsonValue>> iterator =
                    (scala.collection.Iterator<Tuple2<String, JsonValue>>) bindings.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public JsonEntry<String> next() {
                Tuple2<String, JsonValue> next = iterator.next();
                return new JsonEntry<>(next._1(), next._2());
            }
        };
    }

    @Override
    public String prettyStringifyRecursive(int indent,
                                            int incrementAcc,
                                            boolean keepingNull,
                                            boolean emptyValuesToNull) {
        String result;
        if (this.isEmpty()) {
            result = toString();
        }
        else {
            StringBuilder startInc = new StringBuilder();
            for (int i = 0; i < incrementAcc; ++i) {
                startInc.append(" ");
            }
            StringBuilder endInc = new StringBuilder();
            for (int i = 0; i < incrementAcc - indent; ++i) {
                endInc.append(" ");
            }
            final String startIncrementation = startInc.toString();
            final String endIncrementation = endInc.toString();
            StringJoiner sj = new StringJoiner(",\n", "{\n", String.format("\n%s}", endIncrementation));
            JsonValue value;
            for(JsonEntry entry : this){
                value = entry.getValue();
                if(emptyValuesToNull && value.isJsonOptional() && value.isEmpty())
                    value = JsonNull.INSTANCE;
                if(keepingNull && value.isJsonNull() || !value.isJsonNull()){
                    sj = sj.add(
                            String.format("%s\"%s\":%s",
                                    startIncrementation,
                                    entry.getKey(),
                                    entry.getValue().prettyStringifyRecursive(indent,
                                            incrementAcc + indent,
                                            keepingNull,
                                            emptyValuesToNull)
                            ));
                }

            }
            result = sj.toString();
        }
        return result;
    }

    public Stream<JsonEntry> stream(){
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator(), Spliterator.NONNULL | Spliterator.IMMUTABLE),
                false
        );
    }

    @Override
    public String stringify(boolean keepingNull, boolean emptyValuesToNull){
        StringJoiner sj = new StringJoiner(",", "{", "}");
        scala.collection.Iterator<Tuple2<String, JsonValue>> iterator =
                (scala.collection.Iterator<Tuple2<String, JsonValue>>) bindings.iterator();
        Tuple2<String, JsonValue> next;
        JsonValue value;
        while(iterator.hasNext()){
            next = iterator.next();
            value = next._2();
            if(emptyValuesToNull && value.isJsonOptional() && value.isEmpty())
                value = JsonNull.INSTANCE;
            if(keepingNull && value.isJsonNull() || !value.isJsonNull())
                sj = sj.add(String.format("\"%s\":%s", next._1(), next._2().toString()));
        }

        return sj.toString();
    }

    @Override
    public String toString() {
        return "JsonObject{" +
                "bindings=" + bindings +
                '}';
    }

    @Override
    public JsonObject union(JsonValue jsonObject){
        if( !(jsonObject instanceof JsonObject) )
            throw new IllegalArgumentException(
                    String.format("union is not possible between JsonObject and %s", jsonObject.getClass())
            );

        JsonObject jo = (JsonObject) jsonObject;
        JsonObject result;
        if(this.isEmpty()){
            result = EMPTY;
        }
        else {
            result = new JsonObject(jo.bindings.$plus$plus(this.bindings));
        }
        return result;
    }

    @Override
    public JsonObject unionAll(List<? extends JsonValue> jsonValues){
        Builder<Tuple2<String, JsonValue>, Map> mapBuilder = Map$.MODULE$.<String, JsonValue>newBuilder();
        mapBuilder.$plus$plus$eq(this.bindings);
        JsonObject temp = null;
        int i = 0;
        try {
            for(JsonValue jo : jsonValues) {
                temp = (JsonObject) jo;
                ++i;
                if (temp.isNotEmpty())
                    mapBuilder.$plus$plus$eq(temp.bindings);
            }
        }
        catch (ClassCastException e) {
            throw new IllegalArgumentException(
                    String.format("union is not possible between JsonObject and %s (item %d of list)", temp.getClass(), i)
            );
        }
        return new JsonObject(mapBuilder.result());
    }
}
