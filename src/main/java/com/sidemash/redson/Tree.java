package com.sidemash.redson;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Tree<T> {

    public T node;
    public List<Tree<T>> children;
    private static final Tree<?> EMPTY = new Tree<>();

    public static<T> Tree<T> empty(){
        @SuppressWarnings("unchecked")
        Tree<T> result = (Tree<T>) EMPTY;
        return result;
    }

    static {
        Json.registerWriter(Tree.class, ((tree, jsonValue) -> JsonObject.of(
                JsonEntry.of("node", tree.getNode()),
                JsonEntry.of("children", tree.getChildren())
        )));

        Json.registerReader(Tree.class, (JsonValue jsVal, Type type) ->{
            if (jsVal.isJsonObject() && jsVal.isEmpty())
                return Tree.empty();

            ParameterizedType p = (ParameterizedType) type;
            List<Tree<Object>> children = new ArrayList<>();
            ((JsonArray) jsVal.get("children")).valuesStream().forEach(value -> children.add(value.as(type)));

            return new Tree<>(
                    jsVal.get("node").as(p.getActualTypeArguments()[0]),
                    children
            );
        });
    }


    public T getNode() {
        return node;
    }

    public List<Tree<T>> getChildren() {
        return children;
    }

    public Tree(T node, List<Tree<T>> children) {
        this.node = node;
        this.children = children;
    }

    public Tree(T node) {
        this(node, new ArrayList<>());
    }
    private Tree() {
        this(null, null);
    }

    public boolean isEmpty(){
        return (node == null && children == null);
    }

    @Override
    public String toString() {
        return "Tree{" +
                "node=" + node +
                ", children=" + children +
                '}';
    }
}
