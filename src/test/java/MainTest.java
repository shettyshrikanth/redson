import com.fasterxml.jackson.core.type.TypeReference;
import com.sidemash.redson.JsonValue;

import java.util.Arrays;

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



        System.out.println(JsonValue.of(emptyTree).asType(new TypeReference<Tree<Charac<Integer>>>() {}));
        System.out.println(JsonValue.of(singletonTree).asType(new TypeReference<Tree<Charac<Integer>>>() {}));
        System.out.println(JsonValue.of(tree).asType(new TypeReference<Tree<Charac<Integer>>>() {}));

/*
        Charac<String> characInt = new Charac<>("vCPU", "3", "Virtual CPU");
        System.out.println(JsonValue.of(characInt).as(new TypeReference<Charac<Integer>>() {}).equals(characInt));
        System.out.println(JsonValue.of(characInt).as(new TypeReference<Charac<Integer>>() {}));
        */
    }
}
