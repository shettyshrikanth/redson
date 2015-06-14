package com.sidemash.redson;


import com.sidemash.redson.util.ImmutableVector;
import scala.collection.immutable.Vector;
import scala.collection.immutable.Vector$;
import scala.collection.immutable.VectorIterator;
import scala.collection.mutable.Builder;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.*;


@SuppressWarnings("WeakerAccess")
public class JsonArray implements
        JsonStructure, Iterable<JsonEntry<Integer>>, ImmutableVector<JsonValue> {

    public static final JsonArray EMPTY = new JsonArray(Vector$.MODULE$.empty());
    private final Vector<JsonValue> items;

    private static JsonArray createJsonArray(Vector<JsonValue> items){
        if(items.isEmpty())
            return EMPTY;
        else
            return new JsonArray(items);
    }

    private JsonArray(Vector<JsonValue> items) {
        this.items = items;
    }


    public static JsonArray of(Object... values){
        return JsonArray.of(Arrays.asList(values));
    }


    public static <T,S extends BaseStream<T, S>> JsonArray of(BaseStream<T, S> stream){
        return JsonArray.of(stream.iterator());
    }


    @SuppressWarnings("WeakerAccess")
    public static <E> JsonArray of(Iterator<E> it){
        if(!it.hasNext())
            return JsonArray.EMPTY;

        Builder<JsonValue, Vector<JsonValue>> vectorBuilder = Vector$.MODULE$.<JsonValue>newBuilder();
        while(it.hasNext()){
            vectorBuilder.$plus$eq(Json.toJsonValue(it.next()));
        }
        return createJsonArray(vectorBuilder.result());
    }

    public static <E> JsonArray of(Iterable<E> values){
        return JsonArray.of(values.iterator());
    }

    public JsonArray append(JsonArray jsonArray) {
        return createJsonArray(items.appendBack(jsonArray));
    }

    @Override
    public <T> Map<Integer, T> asIntIndexedMapOf(Class<T> c, Map<Integer, T> map) {
        if (items.isEmpty())
            return map;
        for (int i = 0; i < items.length(); i++) {
            map.put(i, Json.fromJsonValue(items.apply(i), c));
        }
        return map;
    }

    @Override
    public <T> List<T> asListOf(Class<T> cl, List<T> list) {
        for (int i = 0; i < items.length(); i++) {
            list.add(Json.fromJsonValue(items.apply(i), cl));
        }
        return list;
    }

    @Override
    public <T> Set<T> asSetOf(Class<T> cl, Set<T> set) {
        final scala.collection.Iterator <JsonValue> iterator = items.iterator();
        while (iterator.hasNext())
            set.add(Json.fromJsonValue(iterator.next(), cl));
        return set;
    }

    @Override
    public <T> Map<String, T> asStringIndexedMapOf(Class<T> c, Map<String, T> map) {
        for (int i = 0; i < items.length(); i++)
            map.put(String.valueOf(i), Json.fromJsonValue(items.apply(i), c));
        return map;
    }


    public boolean containsAllValues(Object... values) {
        return containsAllValues(Arrays.asList(values));
    }

    public <E>  boolean containsAllValues(List<E> values) {
        return values.stream().allMatch(items::contains);
    }

    @Override
    public boolean containsValue(Object value) {
        return items.contains(value);
    }

    public boolean containsIndex(int value) {
        return items.isDefinedAt(value);
    }

    public JsonArray distinct() {
        return createJsonArray(items.distinct().toVector());
    }

    @Override
    public JsonArray skip(int nb) {
        return new JsonArray(items.drop(nb));
    }

    @Override
    public JsonArray skipRight(int nb) {
        return new JsonArray(items.dropRight(nb));
    }

    @Override
    public JsonArray take(int nb) {
        return new JsonArray(items.take(nb));
    }

    @Override
    public JsonArray limit(int nb) {
        return this.take(nb);
    }

    @Override
    public JsonArray takeRight(int nb) {
        return new JsonArray(items.takeRight(nb));
    }

    @Override
    public JsonArray limitRight(int nb) {
        return new JsonArray(items.takeRight(nb));
    }

    @Override
    public JsonArray skipWhile(Predicate<? super JsonValue> predicate) {
        final VectorIterator<JsonValue> iterator = items.iterator();
        boolean skipAgain = true;
        int index = 0;
        while (skipAgain && iterator.hasNext()) {
            skipAgain = predicate.test(iterator.next());
            index++;
        }

        return new JsonArray(items.drop(index));
    }

    @Override
    public JsonArray takeWhile(Predicate<? super JsonValue> predicate) {
        final VectorIterator<JsonValue> iterator = items.iterator();
        boolean takeAgain = true;
        int index = 0;
        while (takeAgain && iterator.hasNext()) {
            takeAgain = predicate.test(iterator.next());
            index++;
        }

        return new JsonArray(items.take(index));
    }

    @Override
    public JsonArray sorted(Comparator<? super JsonValue> comparator) {
        return JsonArray.of(this.valuesStream().sorted(comparator));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonArray that = (JsonArray) o;

        return items.equals(that.items);

    }

    @Override
    public JsonValue get(int index) {
        return items.apply(index);
    }

    @Override
    public JsonValue getHead() {
        if(items.isEmpty())
            throw new NoSuchElementException("Head of an empty JsonArray");
        return items.head();
    }

    @Override
    public JsonArray getTail() {
        if(items.isEmpty())
            throw new UnsupportedOperationException("Get the Tail of an empty JsonArray");
        return createJsonArray(items.tail());
    }

    @Override
    public JsonValue getLast() {
        if(items.isEmpty())
            throw new NoSuchElementException("Last of an Empty JsonArray");
        return items.last();
    }

    @Override
    public JsonValue get(String key) {
        throw new UnsupportedOperationException("Get an item by key on a JsonArray");
    }

    public Set<Integer> getIndexSet() {
        return IntStream.range(0, items.length())
                .boxed()
                .collect(Collectors.toSet());
    }

    public Set<Integer> getIndexSet(Set<Integer> initialSet) {
        for (int i = 0; i < items.length(); i++)
            initialSet.add(i);

        return initialSet;
    }


    public Set<JsonEntry<Integer>> getIntIndexedEntrySet(Set<JsonEntry<Integer>> initialSet) {
        for (int i = 0; i < items.length(); i++)
            initialSet.add(new JsonEntry<>(i, items.apply(i)));
        return initialSet;
    }

    public Set<JsonEntry<Integer>> getIntIndexedEntrySet() {
        return this.stream().collect(Collectors.toSet());
    }


    @Override
    public Optional<JsonValue> getOptional(int index) {
        return (items.isDefinedAt(index)) ? Optional.of(items.apply(index)) : Optional.<JsonValue>empty() ;
    }

    @Override
    public Optional<JsonValue> getOptional(String key) {
        return Optional.empty();
    }

    public<R> JsonArray map(Function<? super JsonEntry<Integer>, R> mapper){
        return JsonArray.of(this.stream().map(mapper.andThen(Json::toJsonValue)));
    }

    public JsonArray flatMap(Function<? super JsonEntry<Integer>, ? extends JsonArray> mapper){
       /* Function<? super JsonEntry<Integer>, ? extends Stream<? extends JsonEntry<Integer>>>
                adapterMapper = integerJsonEntry -> mapper.apply(integerJsonEntry).stream();
          return JsonArray.of(this.stream().flatMap(adapterMapper).map(jsonEntry -> jsonEntry.getValue()));
        */
        return this.map(mapper).flatten();
    }

    public JsonArray flatten(){

        if(this.isEmpty())
            return JsonArray.EMPTY;

        Builder<JsonValue, Vector<JsonValue>> vectorBuilder = Vector$.MODULE$.<JsonValue>newBuilder();
        scala.collection.Iterator<JsonValue> it = items.iterator();
        JsonValue value;
        while(it.hasNext()){
            value = it.next();
            if(value.isJsonArray()) vectorBuilder.$plus$plus$eq(((JsonArray)value).flatten().items);
            else vectorBuilder.$plus$eq(value);
        }
        return createJsonArray(vectorBuilder.result());
    }

    @Override
    public JsonValue getOrDefault(int index, JsonValue jsonValue) {
        return (items.isDefinedAt(index)) ? items.apply(index) : jsonValue ;
    }

    @Override
    public JsonValue getOrDefault(String key, JsonValue jsonValue) {
        return jsonValue;
    }

    @Override
    public Set<JsonEntry<String>> getStringIndexedEntrySet() {
        return this.stream()
                .map(entry -> new JsonEntry<>(String.valueOf(entry.getKey()), entry.getValue()))
                .collect(Collectors.toSet());
    }

    public Set<JsonEntry<String>> getStringIndexedEntrySet(Set<JsonEntry<String>> initialSet) {
        for (int i = 0; i < items.length(); i++)
            initialSet.add(new JsonEntry<>(String.valueOf(i), items.apply(i)));
        return initialSet;
    }

    @Override
    public List<JsonValue> getValue() {
        scala.collection.Iterator<JsonValue> iterator = items.iterator();
        final List<JsonValue> value = new ArrayList<>();
        while (iterator.hasNext()) {
            value.add(iterator.next());
        }
        return value;
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }

    public Iterator<Integer> indexesIterator() {
        return new Iterator<Integer>() {

            int index = -1;

            @Override
            public boolean hasNext() {
                return (index +1) < items.size() ;
            }

            @Override
            public Integer next() {
                return  (index++);
            }
        };
    }

    public IntStream indexesStream(){
        return IntStream.range(0, items.size());
    }

    public boolean isDefinedAt(int index) {
        return items.isDefinedAt(index);
    }

    @Override
    public boolean isEmpty(){
        return items.isEmpty();
    }

    @Override
    public boolean isJsonArray() {
        return true;
    }

    @Override
    public boolean isJsonObject() {
        return false;
    }

    @Override
    public Iterator<JsonEntry<Integer>> iterator() {
        return new Iterator<JsonEntry<Integer>>() {

            final scala.collection.Iterator<JsonValue> iterator = items.iterator();
            int index = -1;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public JsonEntry<Integer> next() {
                index++;
                return new JsonEntry<>(index, iterator.next());
            }
        };
    }

    public JsonValue prepend(JsonValue jsValue) {
        return createJsonArray(items.appendFront(jsValue));
    }

    @Override
    public String prettyStringifyRecursive(int indent, int incrementAcc, boolean keepingNull, boolean emptyValuesToNull)  {
        String result;
        if (this.isEmpty()) {
            result = "[]";
        }
        else {
            StringBuilder startInc = new StringBuilder();
            for (int i = 0; i < incrementAcc; ++i) {
                startInc.append(" ");
            }
            StringBuilder endInc  = new StringBuilder();
            for (int i = 0; i < incrementAcc - indent; ++i) {
                endInc.append(" ");
            }
            final String startIncrementation = startInc.toString();
            final String endIncrementation = endInc.toString();
            StringJoiner sj = new StringJoiner(",\n", "[\n", String.format("\n%s]", endIncrementation));
            JsonValue value;
            for (JsonEntry<Integer> jsonValue : this) {
                value = jsonValue.getValue();
                if (emptyValuesToNull && value.isJsonOptional() && value.isEmpty())
                    value = JsonNull.INSTANCE;
                if(Json.isEligibleForStringify(value, keepingNull)){
                    sj = sj.add(
                            String.format("%s%s",
                                    startIncrementation,
                                    value.prettyStringifyRecursive(indent,
                                            incrementAcc + indent,
                                            keepingNull,
                                            emptyValuesToNull))
                    );
                }
            }
            result = sj.toString();
        }

        return result;
    }

    public JsonArray removeAt(int index){
        Builder<JsonValue, Vector<JsonValue>> vectorBuilder = Vector$.MODULE$.<JsonValue>newBuilder();
        vectorBuilder.$plus$plus$eq(this.items.take(index));
        vectorBuilder.$plus$plus$eq(this.items.drop(index+1));
        return createJsonArray(vectorBuilder.result());
    }

    public JsonArray reverse() {
        Iterator<JsonValue> it =  new Iterator<JsonValue>() {
            final scala.collection.Iterator<JsonValue> iterator = items.reverseIterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public JsonValue next() {
                return iterator.next();
            }
        };
        return JsonArray.of(it);
    }


    public Iterator<JsonValue> reversedValuesIterator() {
        return  new Iterator<JsonValue>() {
            final scala.collection.Iterator<JsonValue> iterator = items.reverseIterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public JsonValue next() {
                return iterator.next();
            }
        };
    }


    public Iterator<JsonEntry<Integer>> reversedIterator() {
        return  new Iterator<JsonEntry<Integer>>() {
            final scala.collection.Iterator<JsonValue> iterator = items.reverseIterator();
            int current = items.length();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public JsonEntry<Integer> next() {
                current--;
                return new JsonEntry<>(current, iterator.next());
            }
        };
    }


    public Iterator<Integer> reversedIndexesIterator() {
        return  new Iterator<Integer>() {
            int current = items.length() ;

            @Override
            public boolean hasNext() {
                return (current >= 0);
            }

            @Override
            public Integer next() {
                current--;
                return current;
            }
        };
    }

    @Override
    public int size() {
        return items.size();
    }

    public Stream<JsonEntry<Integer>> stream(){
        return StreamSupport.stream(
                Spliterators.spliterator(
                        iterator(),
                        items.size(),
                        Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.DISTINCT
                ),
                false
        );
    }


    public Stream<JsonEntry<Integer>> reversedStream(){
        return StreamSupport.stream(
                Spliterators.spliterator(
                        reversedIterator(),
                        items.size(),
                        Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.DISTINCT
                ),
                false
        );
    }


    public IntStream reversedIndexesStream(){
        final int itemsLength = items.length();
        return IntStream
                .range(0, itemsLength)
                .map(i -> itemsLength - i - 1);
    }

    @Override
    public String stringify(boolean keepingNull, boolean emptyValuesToNull){
        if (this.isEmpty())
            return "[]";

        StringJoiner sj = new StringJoiner(",", "[", "]");
        VectorIterator<JsonValue> iterator = items.iterator();
        JsonValue value;
        while(iterator.hasNext()){
            value = iterator.next();
            if (emptyValuesToNull && value.isJsonOptional() && value.isEmpty())
                value = JsonNull.INSTANCE;
            if(Json.isEligibleForStringify(value, keepingNull)){
                sj = sj.add(value.stringify());
            }
        }
        return sj.toString();
    }
    @Override
    public <U> U foldLeft(U seed, BiFunction<U, JsonValue, U> op) {
        U result = seed;
        VectorIterator<JsonValue> iterator = items.iterator();
        while(iterator.hasNext()){
            result = op.apply(result, iterator.next());
        }
        return result;
    }



    @Override
    public String toString() {
        return "JsonArray{" +
                "items=" + items +
                '}';
    }

    public JsonArray union(JsonArray jsonArray) {
        if(isEmpty())
            return  jsonArray;

        else if(jsonArray.isEmpty())
            return this;

        else {
            Builder<JsonValue, Vector<JsonValue>> vectorBuilder = Vector$.MODULE$.<JsonValue>newBuilder();
            //noinspection unchecked
            vectorBuilder
                    .$plus$plus$eq(this.items)
                    .$plus$plus$eq(jsonArray.items);
            return  createJsonArray(vectorBuilder.result());
        }
    }


    public JsonArray intersect(JsonArray jsonArray) {
        if(isEmpty() || jsonArray.isEmpty())
            return EMPTY;
        else {
            JsonArray temp, other;
            if(jsonArray.size() < this.size()) { temp = jsonArray; other = this; }
            else { temp = this; other = jsonArray;  }

            return JsonArray.of(
                    temp.valuesStream()
                            .filter(other::containsValue)
            );
        }
    }

    public JsonArray unionAll(List<JsonArray> jsonArrays) {
        Builder<JsonValue, Vector<JsonValue>> vectorBuilder = Vector$.MODULE$.<JsonValue>newBuilder();
        vectorBuilder.$plus$plus$eq(this.items);
        for(JsonArray array : jsonArrays)
           vectorBuilder.$plus$plus$eq(array.items);
        return createJsonArray(vectorBuilder.result());
    }

    @Override
    public JsonArray updateValue(int index, JsonValue newJsonValue){
        return updateValue(index, ignored -> newJsonValue );
    }

    @Override
    public JsonArray updateValue(int index, UnaryOperator<JsonValue> operator){
        if(!items.isDefinedAt(index))
            return this;

       return new JsonArray(items.updateAt(index, operator.apply(items.apply(index))));
    }


    public JsonArray updateWhile(Predicate<? super JsonEntry<Integer>> predicate, JsonValue elem) {
        return updateWhile(predicate, ignored -> elem);
    }

    public JsonArray updateWhile(Predicate<? super JsonEntry<Integer>> predicate, UnaryOperator<JsonValue> operator) {
        Builder<JsonValue, Vector<JsonValue>> vectorBuilder = Vector$.MODULE$.<JsonValue>newBuilder();
        for (JsonEntry<Integer> nextEntry : this) {
            if (predicate.test(nextEntry))
                vectorBuilder.$plus$eq(operator.apply(nextEntry.getValue()));
            else
                vectorBuilder.$plus$eq(nextEntry.getValue());
        }
        return createJsonArray(vectorBuilder.result());
    }


    public JsonArray updateFirst(Predicate<? super JsonEntry<Integer>> predicate, JsonValue elem) {
        return updateFirst(predicate, ignored -> elem);
    }

    public JsonArray updateFirst(Predicate<? super JsonEntry<Integer>> predicate, UnaryOperator<JsonValue> operator) {
        /*
        if (predicate.test(items.apply(0)))
            return new JsonArray(items.updateAt(0, operator.apply(items.apply(0))));
        else
            return this;
        */
        return  this.stream()
                    .filter(predicate)
                    .findFirst()
                    .map(entry -> this.updateValue(entry.getKey(), operator))
                    .orElse(this);
    }


    public int firstIndexWhere(Predicate<? super JsonEntry<Integer>> predicate) {
        final int from = 0;
        return firstIndexWhere(predicate, from);
    }

    public int firstIndexWhere(Predicate<? super JsonEntry<Integer>> predicate, int from) {
        return  this.stream()
                    .skip(from)
                    .filter(predicate)
                    .mapToInt(JsonEntry::getKey)
                    .findFirst()
                    .orElse(-1);
    }

    public List<Integer> indexesWhere(Predicate<? super JsonEntry<Integer>> predicate) {
        final int from = 0;
        return indexesWhere(predicate, from);
    }

    public List<Integer> indexesWhere(Predicate<? super JsonEntry<Integer>> predicate, int from) {
        List<Integer> result = new ArrayList<>();
        this.stream()
                .skip(from)
                .forEachOrdered(entry -> {
                    if (predicate.test(entry))
                        result.add(entry.getKey());
                });
        return result;
    }

    public JsonArray updateWhere(Predicate<? super JsonEntry<Integer>> predicate, JsonValue elem) {
        return updateWhere(predicate, ignored -> elem);
    }


    public JsonArray updateWhere(Predicate<? super JsonEntry<Integer>> predicate, UnaryOperator<JsonValue> operator) {
        return this.map(
                entry -> {
                    if (predicate.test(entry))
                        return operator.apply(entry.getValue());
                    else
                        return entry.getValue();
                }
        );
    }

    @Override
    public JsonArray filterNot(Predicate<? super JsonValue> predicate) {
        return this.filter(predicate.negate());
    }

    @Override
    public JsonArray filter(Predicate<? super JsonValue> predicate) {
        return JsonArray.of(this.valuesStream().filter(predicate));
    }

    @Override
    public Iterator<JsonValue> valuesIterator() {
        return new Iterator<JsonValue>() {

            final scala.collection.Iterator<JsonValue> iterator = items.iterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public JsonValue next() {
                return iterator.next();
            }
        };
    }

}
