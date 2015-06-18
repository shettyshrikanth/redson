package com.sidemash.redson;


public class Main {

    public static void main(String[] args) {

        // Tester limit(), limitWhile(), limitRight()
        // Revoir les regles de conversion par defaut d'un objet

        System.out.println(JsonObject.of(JsonEntry.of("serge", "martial")).prettyStringify());

        // System.out.println(JsonObject.of(tree).prettyStringify());
        // Cannot transform nested classes to Json by reflexion
        // Implements monadic methods on JsonValue DONE with STREAM
        // Give sense to all Exceptions
        // Implements equals and hashcode OK
        // Test
        // Document
        // Implement ordered converter list, Many Converetr for exemple for String OK
        // Implement converter for date
        // Handle conversion to List Map, Arrays OK
        // Handle conversion of recursive Parent classes OK
        // Handle conversion of recursive data structures ( List<List<String>>, List<List<Map<String, List<String>>>> ) OK
    }

}
