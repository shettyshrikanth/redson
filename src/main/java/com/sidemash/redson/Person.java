package com.sidemash.redson;

/**
 * Created by Serge Martial on 09/06/2015.
 */
public class Person {

    public String name;
    public int age;
    public String password = "TopSecret";
    public float height;

    static {
        Json.registerWriter(Person.class, ((person, jsonValue) -> JsonObject.of(
                JsonEntry.of("name", person.name),
                JsonEntry.of("age", person.age)
        )));
    }
    public Person(String name, int age, float height) {
        this.name = name;
        this.age = age;
        this.height = height;
    }
}
