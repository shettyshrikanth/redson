package com.sidemash.redson.converter;

/**
 * Created by Serge Martial on 22/06/2015.
 */
public class Person {
    public  String name;
    public  int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public int getAAge() {
        return age;
    }

    public String getDateOfBirth() {
        return name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }
}