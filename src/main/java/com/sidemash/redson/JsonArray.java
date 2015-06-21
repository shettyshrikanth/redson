package com.sidemash.redson;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.sidemash.redson.util.ImmutableVector;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.*;



public class JsonArray implements
        JsonStructure, Iterable<JsonValue>, ImmutableVector<JsonValue> {

    /**
     * Common instance for an Empty JsonArray.
     */
    public static final JsonArray EMPTY = new JsonArray(Collections.EMPTY_LIST);

    /**
     * Storage of the JsonArray contents
     */
    private final List<JsonValue> items;


    /**
     * Construct a JsonArray from List<JsonEntry<Integer>>
     *
     * @param items : List we want to add for the JsonArray storage.
     */
    private JsonArray(List<JsonValue> items) {
        this.items = items;
    }

    /**
     * Factory for create JsonArray from List<JsonEntry<Integer>>.
     * Note : As we wanted an immutable JsonArray, we have made this factory
     * private to ensure that no one else can have a reference to the internal
     * items of a JsonArray.
     *
     * @param items : List we want to add for the JsonArray storage.
     * @return The new JsonArray
     */
    private static JsonArray createJsonArray(List<JsonValue> items) {
        if (items.isEmpty()) return EMPTY;
        else return new JsonArray(items);
    }

    /**
     * Construct a JsonArray from a List of Object
     *
     * @param values the array by which the list will be backed
     * @return The new JsonArray containing all elements passed as parameter
     */
    public static JsonArray of(Object... values){
        return JsonArray.of(Arrays.asList(values));
    }

    /**
     * Construct a JsonArray that contains another JsonArray
     *
     * @param array the inner JsonArray to include in the result
     * @return The new JsonArray
     */
    public static JsonArray of(JsonArray array){
        return JsonArray.of(array.iterator());
    }

    /**
     * Construct a JsonArray from a Stream.
     *
     * @param stream stream containing the items to be added in the JsonArray
     * @param <T> Type of the elements contained by this Stream
     * @param <S> Type of of the stream
     * @return The new JsonArray containing all the items in the stream passed as parameter
     */
    public static <T,S extends BaseStream<T, S>> JsonArray of(BaseStream<T, S> stream){
        return JsonArray.of(stream.iterator());
    }

    /**
     * Construct a JsonArray from the Iterator passed as Parameter
     *
     * @param it Iterator of all items to be added in the result JsonArray
     * @param <T> Type of the items contained by this Iterator
     * @return The new JsonArray
     */
    public static <T> JsonArray of(Iterator<T> it) {
        if(!it.hasNext())
            return JsonArray.EMPTY;

        List<JsonValue> entryItems = new ArrayList<>();
        int i = 0;
        while(it.hasNext()){
            entryItems.add(JsonValue.of(it.next()));
            ++i;
        }
        return createJsonArray(entryItems);
    }

    /**
     * Construct a JsonArray from the Iterable passed as parameter
     *
     * @param values Iterable of all items to be added in the result JsonArray
     * @param <T>  Type of the items contained by this Iterable
     * @return The new JsonArray
     */
    public static <T> JsonArray of(Iterable<T> values) {
        return JsonArray.of(values.iterator());
    }

    /**
     * Append the JsonValue passed as parameter at the end of this JsonArray
     *
     * @param object : JsonValue to be added to the end of this JsonArray
     * @return JsonArray containing all the items of this JsonArray plus the
     *          last element added.
     */
    public JsonArray append(Object object) {
        List<JsonValue> newEntryList = new ArrayList<>();
        newEntryList.addAll(items);
        newEntryList.add(JsonValue.of(object));
        return createJsonArray(newEntryList);
    }

    @Override
    public <T> List<T> asListOf(Class<T> cl, List<T> list) {
        for (JsonValue item : items)
            list.add(item.as(cl));

        return list;
    }

    @Override
    public <T> Map<String, T> asMapOf(Class<T> cl, Map<String, T> map) {
        throw new ClassCastException(
                String.format(
                        "This %s could be interpreted as an instance of Map<String, %s>",
                        this.getClass().getSimpleName(),
                        cl.getSimpleName()
                )
        );
    }

    @Override
    public <T> Set<T> asSetOf(Class<T> cl, Set<T> set) {
        for (JsonValue item : items)
            set.add(item.as(cl));

        return set;
    }

    /**
     * Returns true if this list contains all of the elements of the
     *  specified collection.
     *
     * @param values he array by which the values to be tested will be backed
     * @return Boolean
     */
    public boolean containsAllValues(Object... values) {
        return containsAllValues(Arrays.asList(values));
    }

    /**
     *  Returns <tt>true</tt> if this list contains all of the elements of the
     *  specified collection.
     *
     * @param values collection to be checked for containment in this list
     * @return Boolean
     */
    public boolean containsAllValues(Collection<?> values) {
        return items.containsAll(values);
    }

    /**
     * Returns true if this JsonArray has an item indexed by the integer passed
     * as parameter.
     *
     * @param value index to be tested
     * @return Boolean
     */
    public boolean containsIndex(int value) {
        return (value >= 0 && value < items.size());
    }

    @Override
    public boolean containsValue(Object value) {
        return items.contains(value);
    }

    /**
     * Returns a new JsonArray containing all unique items of this JsonArray
     *
     * @return The new JsonArray
     */
    public JsonArray distinct() {
        return createJsonArray(
                items.stream()
                        .distinct()
                        .collect(Collectors.toList())
        );
    }

    private Iterator<JsonEntry<Integer>> entryIterator() {
        return entryStream().iterator();
    }

    public Stream<JsonEntry<Integer>> entryStream() {
        return IntStream.range(0, items.size())
                .mapToObj(index -> JsonEntry.of(index, items.get(index)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonArray that = (JsonArray) o;

        return items.equals(that.items);
    }

    @Override
    public JsonArray filter(Predicate<? super JsonValue> predicate) {
        return JsonArray.of(this.stream().filter(predicate));
    }

    public JsonArray filterEntry(Predicate<? super JsonEntry<Integer>> predicate) {
        return JsonArray.of(this.entryStream().filter(predicate));
    }

    @Override
    public JsonArray filterNot(Predicate<? super JsonValue> predicate) {
        return this.filter(predicate.negate());
    }

    public JsonArray filterNotEntries(Predicate<? super JsonEntry<Integer>> predicate) {
        return this.filterEntry(predicate.negate());
    }

    public int firstIndexWhere(Predicate<? super JsonEntry<Integer>> predicate) {
        final int from = 0;
        return firstIndexWhere(predicate, from);
    }

    public int firstIndexWhere(Predicate<? super JsonEntry<Integer>> predicate, int from) {
        return this.entryStream()
                .skip(from)
                .filter(predicate)
                .mapToInt(JsonEntry::getKey)
                .findFirst()
                .orElse(-1);
    }

    public JsonArray flatMap(Function<? super JsonValue, ? extends JsonArray> mapper) {
        return this.map(mapper).flatten();
    }

    public JsonArray flatten() {
        if (this.isEmpty())
            return JsonArray.EMPTY;

        Iterator<JsonValue> it = items.iterator();
        JsonValue value;
        List<JsonValue> entryList = new ArrayList<>();
        while (it.hasNext()) {
            value = it.next();
            if (value.isJsonArray())
                entryList.addAll(((JsonArray) value).flatten().items);
            else entryList.add(value);
        }
        return createJsonArray(entryList);
    }

    @Override
    public <U> U foldLeft(U seed, BiFunction<U, JsonValue, U> op) {
        U result = seed;
        for (JsonValue jsonValue : items) {
            result = op.apply(result, jsonValue);
        }
        return result;
    }

    @Override
    public JsonValue get(int index) {
        return items.get(index);
    }

    @Override
    public JsonValue get(String key) {
        throw new UnsupportedOperationException("Get an item by key on a JsonArray");
    }

    @Override
    public JsonValue get() {
        throw new NoSuchElementException(
                String.format(
                        "This method is only available for instances of JsonOptional not %s",
                        this.getClass().getSimpleName()
                )
        );
    }

    @Override
    public JsonValue getHead() {
        if(items.isEmpty())
            throw new NoSuchElementException("Head of an empty JsonArray");

        return items.get(0);
    }

    /**
     * Get a Set containing all indexes of this JsonArray.
     *
     * @return instance of HashSet in which all indexes of this JsonArray have been added.
     */
    public Set<Integer> getIndexSet() {
        return getIndexSet(new HashSet<>());
    }

    /**
     * Get a Set containing all indexes of this JsonArray.
     *
     * @param initialSet The precise (HashSet ? TreeSet ? ... ) set to be used
     * @return The set passed as parameter in which all indexes of this JsonArray
     *          have been added.
     */
    public Set<Integer> getIndexSet(Set<Integer> initialSet) {
        for (int i = 0; i < items.size(); i++)
            initialSet.add(i);

        return initialSet;
    }

    public Set<JsonEntry<Integer>> getIntIndexedEntrySet(Set<JsonEntry<Integer>> initialSet) {
        int index = 0;
        for (JsonValue item : items) {
            initialSet.add(JsonEntry.of(index, item));
            ++index;
        }

        return initialSet;
    }

    public Set<JsonEntry<Integer>> getIntIndexedEntrySet() {
        return this.entryStream().collect(Collectors.toSet());
    }

    @Override
    public JsonValue getLast() {
        if (items.isEmpty())
            throw new NoSuchElementException("Last item of an empty JsonArray");

        return items.get(items.size() - 1);
    }

    @Override
    public Optional<JsonValue> getOptional(int index) {
        return (this.containsIndex(index))
                ? Optional.of(items.get(index))
                : Optional.<JsonValue>empty();
    }

    @Override
    public Optional<JsonValue> getOptional(String key) {
        return Optional.empty();
    }

    @Override
    public Optional<JsonValue> getOptional() {
        return Optional.empty();
    }

    @Override
    public JsonValue getOrDefault(int index, JsonValue jsonValue) {
        return (this.containsIndex(index)) ? items.get(index) : jsonValue;
    }

    @Override
    public JsonValue getOrDefault(String key, JsonValue jsonValue) {
        return jsonValue;
    }

    @Override
    public Set<JsonEntry<String>> getStringIndexedEntrySet() {
        return getStringIndexedEntrySet(new HashSet<>());
    }

    public Set<JsonEntry<String>> getStringIndexedEntrySet(Set<JsonEntry<String>> initialSet) {
        for (int i = 0; i < items.size(); i++)
            initialSet.add( JsonEntry.of(String.valueOf(i), items.get(i)));
        return initialSet;
    }

    @Override
    public JsonArray getTail() {
        if (items.isEmpty())
            throw new UnsupportedOperationException("Get the Tail of an empty JsonArray");

        return this.skip(1);
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }

    public Iterator<Integer> indexIterator() {
        return indexStream().iterator();
    }

    public IntStream indexStream() {
        return IntStream.range(0, items.size());
    }

    public List<Integer> indexesWhere(Predicate<? super JsonEntry<Integer>> predicate) {
        final int from = 0;
        return indexesWhere(predicate, from);
    }

    public List<Integer> indexesWhere(Predicate<? super JsonEntry<Integer>> predicate,
                                      int from) {
        List<Integer> result = new ArrayList<>();
        this.entryStream()
                .skip(from)
                .forEachOrdered(entry -> {
                    if (predicate.test(entry))
                        result.add(entry.getKey());
                });
        return result;
    }

    public JsonArray intersect(JsonArray jsonArray) {
        if (isEmpty() || jsonArray.isEmpty())
            return EMPTY;
        else {
            JsonArray thinner, bigger;
            if (jsonArray.size() < this.size()) {
                thinner = jsonArray;
                bigger = this;
            } else {
                thinner = this;
                bigger = jsonArray;
            }

            return JsonArray.of(
                    thinner.stream()
                            .filter(bigger::containsValue)
            );
        }
    }

    public boolean isDefinedAt(int index) {
        return this.containsIndex(index);
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
    public Iterator<JsonValue> iterator() {
        return items.iterator();
    }

    @Override
    public JsonArray limit(int nb) {
        return createJsonArray(
                items.stream()
                        .limit(nb)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public JsonArray limitRight(int nb) {
        // The idea is the following : If we have a List of 3 items, says [1, 2, 3]
        // then we say that limit 2 items by the Right and return [2,3] is equivalent
        // of skipping 1 value from the left.
        // Hence we ends up with the following formula :
        return this.skip(items.size() - nb);
    }

    public JsonArray limitWhile(Predicate<? super JsonValue> predicate) {
        final Iterator<JsonValue> iterator = items.iterator();
        if (!iterator.hasNext())
            return this;

        boolean keepSkipping = true;
        int index = 0;
        while (keepSkipping && iterator.hasNext()) {
            keepSkipping = predicate.test(iterator.next());
            index++;
        }


        // If all the items have been visited and the last elem have to be taken
        // then we "limit the result to all items of this jsonArray"
        if (!iterator.hasNext() && keepSkipping == true) return this;

            // If the predicate is false for the first element, then there is
            // no need to iterate we return immediately the Empty JsonArray.
        else if (index == 1 && keepSkipping == false) return EMPTY;
        else return this.limit(index);
    }

    public <R> JsonArray map(Function<? super JsonValue, R> mapper) {
        return JsonArray.of(this.stream().map(mapper.andThen(JsonValue::of)));
    }

    public <R> JsonArray mapEntry(Function<? super JsonEntry<Integer>, R> mapper) {
        return JsonArray.of(this.entryStream().map(mapper.andThen(JsonValue::of)));
    }

    /**
     * Prepend the JsonValue passed as parameter at the beginning of this JsonArray
     *
     * @param object : Object to be added at the beginning of this JsonArray
     * @return JsonArray containing the elem passed as param PLUS all the items of
     * this JsonArray
     */
    public JsonArray prepend(Object object) {
        List<JsonValue> newEntryList = new ArrayList<>();
        newEntryList.add(JsonValue.of(object));
        newEntryList.addAll(items);
        return createJsonArray(newEntryList);
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
            Iterator<JsonEntry<Integer>> it = this.entryIterator();
            for (JsonValue jsonValue : items) {
                value = jsonValue;
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
            if(result.equals(String.format("[\n\n%s]", endIncrementation)))
                result = "[]";
        }

        return result;
    }

    public JsonArray removeAt(int index){
        if(!this.containsIndex(index))
            return this;

        List<JsonValue> newList = new ArrayList<>();
        newList.addAll(items);
        newList.remove(index);
        return createJsonArray(newList);
    }

    public JsonArray reverse() {
        return (this.isEmpty())
                ? JsonArray.EMPTY
                : JsonArray.of(this.reversedIterator());
    }

    public Iterator<JsonEntry<Integer>> reversedEntryIterator() {
        return new Iterator<JsonEntry<Integer>>() {
            int index = items.size();

            @Override
            public boolean hasNext() {
                return (index > 0);
            }

            @Override
            public JsonEntry<Integer> next() {
                index--;
                return JsonEntry.of(index, items.get(index));
            }
        };
    }

    public Stream<JsonEntry<Integer>> reversedEntryStream() {
        return StreamSupport.stream(
                Spliterators.spliterator(
                        reversedEntryIterator(),
                        size(),
                        Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.IMMUTABLE
                ),
                false
        );
    }

    public Iterator<Integer> reversedIndexIterator() {
        return reversedIndexStream().iterator();
    }

    public IntStream reversedIndexStream() {
        final int itemsSize = items.size();
        return IntStream
                .range(0, itemsSize)
                .map(i -> itemsSize - i - 1);
    }

    public Iterator<JsonValue> reversedIterator() {
        return new Iterator<JsonValue>() {
            int index = items.size();

            @Override
            public boolean hasNext() {
                return (index > 0);
            }

            @Override
            public JsonValue next() {
                --index;
                return items.get(index);
            }
        };
    }

    public Stream<JsonValue> reversedStream() {
        return StreamSupport.stream(
                Spliterators.spliterator(
                        reversedIterator(),
                        size(),
                        Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.IMMUTABLE
                ),
                false
        );
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public JsonArray skip(int nb) {
        return createJsonArray(
                items.stream()
                        .skip(nb)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public JsonArray skipRight(int nb) {
        // The idea is the following : If we have a List of 3 items, says [1, 2, 3]
        // then we say that skip 2 items by the Right and return [1] is equivalent
        // of limiting the list to 1 value.
        // Hence we ends up with the following formula :
        return this.limit(items.size() - nb);
    }

    public JsonArray skipWhile(Predicate<? super JsonValue> predicate) {
        final Iterator<JsonValue> iterator = items.iterator();
        if (!iterator.hasNext())
            return this;

        boolean keepSkipping = true;
        int index = 0;
        while (keepSkipping && iterator.hasNext()) {
            keepSkipping = predicate.test(iterator.next());
            index++;
        }

        // If the predicate is false for the first element, then there is
        // nothing to skip we return immediately the all the elem.
        if (index == 1 && keepSkipping == false) return this;

            // If we did get out of the loop because all the items match the predicate AND
            // the predicate was true for the last item, then we have to skip all items
            // of the this JsonArray
        else if (!iterator.hasNext() && keepSkipping == true) return EMPTY;
        else return this.skip(index);
    }

    /**
     * Returns a view of the portion of this list between the specified
     * <tt>fromIndex</tt>, inclusive, and <tt>toIndex</tt>, exclusive.  (If
     * <tt>fromIndex</tt> and <tt>toIndex</tt> are equal, the returned list is
     * empty.)
     *
     * @param startInclusive low endpoint (inclusive) of the subList
     * @param endExclusive high endpoint (exclusive) of the subList
     * @return a JsonArray composed by the specified range within this JsonArray
     * @throws IndexOutOfBoundsException for an illegal endpoint index value
     *         (<tt>fromIndex &lt; 0 || toIndex &gt; size ||
     *         fromIndex &gt; toIndex</tt>)
     */
    @Override
    public JsonArray slice(int startInclusive, int endExclusive) {
        if (startInclusive == endExclusive)
            return EMPTY;
        if (startInclusive == 0 && endExclusive == items.size())
            return this;

        return createJsonArray(items.subList(startInclusive, endExclusive));
    }

    /**
     * Returns a JsonArray consisting of the elements of this JsonArray, sorted
     * according to the provided {@code Comparator}.
     * @param comparator {@code Comparator} to be used to compare JsonArray elements
     * @return The new JsonArray
     */
    public JsonArray sorted(Comparator<? super JsonValue> comparator) {
        return JsonArray.of(this.stream().sorted(comparator));
    }

    public Stream<JsonValue> stream() {
        return StreamSupport.stream(
                Spliterators.spliterator(
                        iterator(),
                        items.size(),
                        Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.IMMUTABLE | Spliterator.DISTINCT
                ),
                false
        );
    }

    @Override
    public String stringify(boolean keepingNull, boolean emptyValuesToNull){
        if (this.isEmpty())
            return "[]";

        StringJoiner sj = new StringJoiner(",", "[", "]");
        JsonValue value;
        for(JsonValue jsonValue : items) {
            value = jsonValue;
            if (emptyValuesToNull && value.isJsonOptional() && value.isEmpty())
                value = JsonNull.INSTANCE;
            if(Json.isEligibleForStringify(value, keepingNull)){
                sj = sj.add(value.stringify(keepingNull));
            }
        }
        return sj.toString();
    }

    @Override
    public <T> Map<Integer, T> toIntIndexedMapOf(Class<T> cl, Map<Integer, T> map) {
        int index = 0;
        for (JsonValue item : items) {
            map.put(index, item.as(cl));
            ++index;
        }

        return map;
    }

    @Override
    public JsonNode toJsonNode() {
        ArrayNode result = JsonNodeFactory.instance.arrayNode();
        items.stream().forEachOrdered(value  -> {
            result.add(value.toJsonNode());
        });
        return result;
    }

    @Override
    public String toString() {
        return "JsonArray{" +
                "items=" + items +
                '}';
    }

    @Override
    public <T> Map<String, T> toStringIndexedMapOf(Class<T> cl, Map<String, T> map) {
        int index = 0;
        for (JsonValue item : items) {
            map.put(String.valueOf(index), item.as(cl));
            ++index;
        }

        return map;
    }

    public JsonArray union(JsonArray jsonArray) {
        if(isEmpty())
            return  jsonArray;

        else if(jsonArray.isEmpty())
            return this;

        else {
            List<JsonValue> newList = new ArrayList<>();
            newList.addAll(this.items);
            newList.addAll(jsonArray.items);
            return createJsonArray(newList);
        }
    }

    public JsonArray unionAll(List<JsonArray> jsonArrays) {
        List<JsonValue> newList = new ArrayList<>();
        newList.addAll(this.items);
        for (JsonArray array : jsonArrays)
            newList.addAll(array.items);

        return createJsonArray(newList);
    }

    public JsonArray updateFirst(Predicate<? super JsonEntry<Integer>> predicate,
                                 JsonValue elem) {
        return updateFirst(predicate, ignored -> elem);
    }

    public JsonArray updateFirst(Predicate<? super JsonEntry<Integer>> predicate,
                                 Function<JsonEntry<Integer>, JsonValue> function) {
        return this.entryStream()
                .filter(predicate)
                .findFirst()
                .map(entry -> this.updateValue(entry.getKey(), function))
                .orElse(this);
    }

    @Override
    public JsonArray updateValue(int index, JsonValue newJsonValue) {
        return updateValue(index, ignored -> newJsonValue);
    }

    public JsonArray updateValue(int index, Function<JsonEntry<Integer>, JsonValue> function) {
        if (!this.containsIndex(index))
            return this;

        List<JsonValue> newList = new ArrayList<>();
        int i = 0;
        Iterator<JsonEntry<Integer>> it = this.entryIterator();
        JsonEntry<Integer> entry;
        while (it.hasNext()) {
            entry = it.next();
            if (i == index) newList.add(function.apply(entry));
            else newList.add(entry.getValue());
            i++;
        }
        return createJsonArray(newList);
    }

    public JsonArray updateWhere(Predicate<? super JsonEntry<Integer>> predicate,
                                 JsonValue elem) {
        return updateWhere(predicate, ignored -> elem);
    }

    public JsonArray updateWhere(Predicate<? super JsonEntry<Integer>> predicate,
                                 Function<JsonEntry<Integer>, JsonValue> function) {
        List<JsonValue> newItems = new ArrayList<>();
        int i = 0;
        boolean updated = false;
        JsonEntry<Integer> entry;
        for(JsonValue jsonValue : items){
            entry = JsonEntry.of(i, jsonValue);
            if (predicate.test(entry)){
                newItems.add(function.apply(entry));
                updated = true;
            } else {
                newItems.add(entry.getValue());
            }
            i++;
        }
        if(updated == false) return this;
        return createJsonArray(newItems);
    }

    public JsonArray updateWhile(Predicate<? super JsonEntry<Integer>> predicate, JsonValue elem) {
        return updateWhile(predicate, ignored -> elem);
    }

    public JsonArray updateWhile(Predicate<? super JsonEntry<Integer>> predicate,
                                 Function<JsonEntry<Integer>, JsonValue> function) {
        if (isEmpty())
            return this;

        List<JsonValue> newList = new ArrayList<>();
        Iterator<JsonEntry<Integer>> it = this.entryIterator();
        JsonEntry<Integer> entry;
        while (it.hasNext()) {
            entry = it.next();
            if (predicate.test(entry))
                newList.add(function.apply(entry));
            else
                newList.add(entry.getValue());
        }
        return createJsonArray(newList);
    }

}
