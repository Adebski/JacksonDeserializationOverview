package com.adebski.jackson;

import java.util.Objects;

public class PersonPrivateFieldsNoArgConstructor {

    public PersonPrivateFieldsNoArgConstructor() {
        System.out.println("PersonPrivateFieldsNoArgConstructor constructor");
    }

    private String name;
    private int age;

    @Override
    public String toString() {
        return "PersonPrivateFieldsNoArgConstructor{" +
            "name='" + name + '\'' +
            ", age=" + age +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonPrivateFieldsNoArgConstructor that = (PersonPrivateFieldsNoArgConstructor) o;
        return age == that.age &&
            Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    // Because the fields are private we are not able to construct an "expected" instance directly in the tests.
    public static PersonPrivateFieldsNoArgConstructor getExpectedValue() {
        PersonPrivateFieldsNoArgConstructor result = new PersonPrivateFieldsNoArgConstructor();
        result.name = "fooName";
        result.age = 23;
        return result;
    }
}
