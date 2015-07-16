import com.fasterxml.jackson.core.type.TypeReference;
import com.sidemash.redson.JsonArray;
import com.sidemash.redson.JsonCollector;
import com.sidemash.redson.JsonEntry;
import com.sidemash.redson.JsonValue;
import org.specs2.json.JSONArray;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class MainTest {

    public static void main(String[] args) {

        Tree<Charac<Integer>> emptyTree = Tree.empty();

        Tree<Charac<Integer>> singletonTree =
                new Tree<>(new Charac<>("vCpu", 4, "VirtuaU"));

        Tree<Charac<Integer>> tree =
                new Tree<>(new Charac<>("Hyperviseur", 1, "Virtual Hyperviseur"),
                        Arrays.asList(new Tree<>(new Charac<>("vCPU", 3, "Virtual CPU"),
                                        Arrays.asList(new Tree<>(new Charac<>("vCPU", 1, "Virtual CPU")),new Tree<>(new Charac<>("vCPU", 1, "Virtual CPU"))))
                                ,new Tree<>(new Charac<>("Ram", 8, "Virtual Ram"),
                                        Arrays.asList(new Tree<>(new Charac<>("Ram", 4, "Virtual Ram")),new Tree<>(new Charac<>("Ram", 4, "Virtual Ram"))))
                                ,new Tree<>(new Charac<>("Disk", 100, "Virtual Disk"),
                                        Arrays.asList(new Tree<>(new Charac<>("disk", 60, "Virtual Disk")),new Tree<>(new Charac<>("disk", 40, "Virtual Disk"))))
                        )
                );
/*
        System.out.println(JsonValue.of(emptyTree).asType(new TypeReference<Tree<Charac<Integer>>>() {}));
        System.out.println(JsonValue.of(singletonTree).asType(new TypeReference<Tree<Charac<Integer>>>() {}));
        System.out.println(JsonValue.of(tree).asType(new TypeReference<Tree<Charac<Integer>>>() {}));
*/
        int[] intArr = {1, 2,3,4,5,6,7};
        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 2);
        map.put(2, 3);
        map.put(0, 1);
        System.out.println(
                map.entrySet()
                        .stream()
                        .map(entry -> JsonEntry.of(entry.getKey().toString(), entry.getValue()))
                        .collect(JsonCollector.toJsonObject())
                        .prettyStringify()
        );


        System.out.println();
        System.out.println(JsonArray.of(1,2,3,4)); //.asListOf(Integer.class));



        JsonArray.Builder builder = JsonArray.builder();
        JsonArray array1  = builder.append(1).append(2).append(93).build();

        JsonArray array2  = builder.build();
        System.out.println("Array 1 " + array1);
        System.out.println("Array 2 " + array2);
        System.out.println("Array 1 " + array1);
    }
}
