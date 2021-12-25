package com.adebski.jackson;

import java.util.Objects;

public class PersonPublicFieldsFinalNoArgsExplicitConstructor {

    public PersonPublicFieldsFinalNoArgsExplicitConstructor() {
        System.out.println("PersonPublicFieldsFinalNoArgsExplicitConstructor constructor");
        this.name = "initialName";
        this.age = 25;
    }

    public final String name;
    public final int age;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "PersonPublicFieldsFinalNoArgsExplicitConstructor{" +
            "name='" + name + '\'' +
            ", age=" + age +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonPublicFieldsFinalNoArgsExplicitConstructor that = (PersonPublicFieldsFinalNoArgsExplicitConstructor) o;
        return age == that.age &&
            Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
