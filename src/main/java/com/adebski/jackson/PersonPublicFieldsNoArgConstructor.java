package com.adebski.jackson;

import java.util.Objects;

public class PersonPublicFieldsNoArgConstructor {
    public PersonPublicFieldsNoArgConstructor() {
        System.out.println("PersonPublicFieldsNoArgConstructor constructor");
    }

    public String name;
    public int age;

    @Override
    public String toString() {
        return "PersonPublicFieldsNoArgConstructor{" +
            "name='" + name + '\'' +
            ", age=" + age +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonPublicFieldsNoArgConstructor that = (PersonPublicFieldsNoArgConstructor) o;
        return age == that.age &&
            Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
