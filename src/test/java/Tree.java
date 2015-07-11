import com.sidemash.redson.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class  Tree<T> {

    private static final Tree<?> EMPTY = new Tree<>();

    static {
        Json.registerWriter(Tree.class, (tree, jsonValue) -> {
            if(tree.isEmpty())
                return JsonObject.EMPTY;
            else {
                return
                        JsonObject.of(
                                JsonEntry.of("node", tree.getNode()),
                                JsonEntry.of("children", tree.getChildren())
                        );
            }
        });

        Json.registerReader(Tree.class, (JsonValue jsVal, Type type) -> {
            if (jsVal.isJsonObject() && jsVal.isEmpty())
                return Tree.empty();

            System.out.println("We are here !!");
            ParameterizedType p = (ParameterizedType) type;
            List<Tree<Object>> children = new ArrayList<>();
            ((JsonArray) jsVal.get("children")).stream().forEach(value -> children.add(value.asType(type)));

            return new Tree<>(
                    jsVal.get("node").asType(p.getActualTypeArguments()[0]),
                    children
            );
        });
    }

    public T node;
    public List<Tree<T>> children;

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

    public static <T> Tree<T> empty() {
        @SuppressWarnings("unchecked")
        Tree<T> result = (Tree<T>) EMPTY;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tree<?> tree = (Tree<?>) o;

        return !(node != null ? !node.equals(tree.node) : tree.node != null)
                && !(children != null ? !children.equals(tree.children) : tree.children != null);

    }

    public List<Tree<T>> getChildren() {
        return children;
    }

    public T getNode() {
        return node;
    }

    @Override
    public int hashCode() {
        int result = node != null ? node.hashCode() : 0;
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
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
