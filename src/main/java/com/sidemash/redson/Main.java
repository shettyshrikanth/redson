package com.sidemash.redson;


public class Main {

    public static void main(String[] args) {
        System.out.println(JsonObject.of(
                JsonEntry.of("Serge", "MArtial"),
                JsonEntry.of("Serge", "Strike"),
                JsonEntry.of("martial", "Venan"),
                JsonEntry.of("Serge", "Strike"),
                JsonEntry.of("martial", "Strike"),
                JsonEntry.of("Serge", "VRadioenan")
           ).stringify());
    }

}
        /*
        // Tester limit(), limitWhile(), limitRight()

        // Revoir les regles de conversion par defaut d'un objet
        // Revoir les regles de conversion en general

        JsonValue jsonValue = JsonArray.of(1, 2, 3, 4);
        jsonValue.getOptional("user")
                .flatMap(user -> user.getOptional("bestFriend"))
                .flatMap(bestFriend -> bestFriend.getOptional("name"))
                .map(JsonValue::asString)
                .orElse("Unknown User");

        System.out.println(JsonArray.of(1, 2, 3, 4).asListOf(Integer.class));

        // System.out.println(JsonObject.of(tree).prettyStringify());
        // Cannot transform nested classes to Json by reflexion
        // Implements monadic methods on JsonValue DONE with STREAM
        // Give sense to all Exceptions OK
        // Implements equals and hashcode OK
        // Test
        // Document
        // Implement ordered converter list, Many Converetr for exemple for String OK
        // Implement converter for date
        // Handle conversion to List Map, Arrays OK
        // Handle conversion of recursive Parent classes OK
        // Handle conversion of recursive data structures ( List<List<String>>, List<List<Map<String, List<String>>>> ) OK
  */