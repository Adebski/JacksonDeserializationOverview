package com.adebski.jackson;

import java.util.Objects;

public class PersonPublicFieldsFinalNoArgsConstructor {

    public PersonPublicFieldsFinalNoArgsConstructor() {
        System.out.println("PersonPublicFieldsFinalNoArgsConstructor constructor");
    }

    public final String name = "initialValue";
    public final int age = -25;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "PersonPublicFieldsFinalNoArgsConstructor{" +
            "name='" + name + '\'' +
            ", age=" + age +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonPublicFieldsFinalNoArgsConstructor that = (PersonPublicFieldsFinalNoArgsConstructor) o;
        return age == that.age &&
            Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
