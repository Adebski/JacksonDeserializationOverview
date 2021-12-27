package com.adebski.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

public class JacksonDeserializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void deserializeWithPublicFieldsNoArgConstructor() throws IOException {
        PersonPublicFieldsNoArgConstructor deserializedValue =
            objectMapper.readValue(getSamplePersonFileURL(), PersonPublicFieldsNoArgConstructor.class);

        System.out.println(deserializedValue);
        PersonPublicFieldsNoArgConstructor expectedValue = new PersonPublicFieldsNoArgConstructor();
        expectedValue.name = "fooName";
        expectedValue.age = 23;
        Assertions.assertEquals(expectedValue, deserializedValue);
    }

    @Test
    public void deserializeWithPrivateFieldsNoArgConstructor() throws IOException {
        UnrecognizedPropertyException unrecognizedPropertyException = Assertions.assertThrows(
            UnrecognizedPropertyException.class,
            () -> objectMapper.readValue(getSamplePersonFileURL(), PersonPrivateFieldsNoArgConstructor.class)
        );

        String expectedMessagePrefix =
            "Unrecognized field \"name\" (class com.adebski.jackson.PersonPrivateFieldsNoArgConstructor), not marked as ignorable (0 known properties: ])";
        Assertions.assertTrue(
            unrecognizedPropertyException.getMessage().startsWith(expectedMessagePrefix)
        );
    }

    @Test
    public void deserializeWithPrivateFieldsNoArgConstructorWithAdditionalConfig() throws IOException {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        PersonPrivateFieldsNoArgConstructor deserializedValue =
            objectMapper.readValue(getSamplePersonFileURL(), PersonPrivateFieldsNoArgConstructor.class);
        System.out.println(deserializedValue);
        Assertions.assertEquals(PersonPrivateFieldsNoArgConstructor.getExpectedValue(), deserializedValue);
    }

    @Test
    public void deserializeWithPersonPublicFieldsFinalNoArgsConstructorDefaultConfiguration() throws IOException {
        PersonPublicFieldsFinalNoArgsConstructor deserializedValue =
            objectMapper.readValue(getSamplePersonFileURL(), PersonPublicFieldsFinalNoArgsConstructor.class);
        PersonPublicFieldsFinalNoArgsConstructor manuallyConstructedValue = new PersonPublicFieldsFinalNoArgsConstructor();
        System.out.println("Deserialized value: " + deserializedValue);
        System.out.println("Manually constructed value: " + manuallyConstructedValue);

        Assertions.assertEquals(
            manuallyConstructedValue,
            deserializedValue
        );
        System.out.println(
            String.format(
                "Actual name '%s' actual age '%d'",
                getNameThroughReflection(deserializedValue),
                getAgeThroughReflection(deserializedValue)
            )
        );
        Assertions.assertEquals(
            getNameThroughReflection(manuallyConstructedValue),
            manuallyConstructedValue.getName()
        );
        Assertions.assertEquals(
            getAgeThroughReflection(manuallyConstructedValue),
            manuallyConstructedValue.getAge()
        );
        Assertions.assertNotEquals(
            getNameThroughReflection(deserializedValue),
            deserializedValue.getName()
        );
        Assertions.assertNotEquals(
            getAgeThroughReflection(deserializedValue),
            deserializedValue.getAge()
        );
    }

    @Test
    public void deserializeWithPersonPublicFieldsFinalNoArgsExplicitConstructorDefaultConfiguration() throws IOException {
        PersonPublicFieldsFinalNoArgsExplicitConstructor deserializedValue =
            objectMapper.readValue(getSamplePersonFileURL(), PersonPublicFieldsFinalNoArgsExplicitConstructor.class);
        PersonPublicFieldsFinalNoArgsExplicitConstructor manuallyConstructedValue = new PersonPublicFieldsFinalNoArgsExplicitConstructor();
        System.out.println("Deserialized value: " + deserializedValue);
        System.out.println("Manually constructed value: " + manuallyConstructedValue);

        Assertions.assertNotEquals(
            manuallyConstructedValue,
            deserializedValue
        );
        Assertions.assertEquals(
            "fooName",
            deserializedValue.name
        );
        Assertions.assertEquals(
            23,
            deserializedValue.age
        );
    }

    @Test
    public void deserializeWithPersonPublicFieldsFinalNoArgsConstructorDoNotModifyFinalFields() throws IOException {
        objectMapper.configure(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS, false);

        UnrecognizedPropertyException unrecognizedPropertyException = Assertions.assertThrows(
            UnrecognizedPropertyException.class,
            () -> objectMapper.readValue(getSamplePersonFileURL(), PersonPublicFieldsFinalNoArgsConstructor.class),
            "Unrecognized field \"name\" (class com.adebski.jackson.PersonPublicFieldsFinalNoArgsConstructor), not marked as ignorable (0 known properties: ])"
        );

        String expectedMessagePrefix =
            "Unrecognized field \"name\" (class com.adebski.jackson.PersonPublicFieldsFinalNoArgsConstructor), not marked as ignorable (0 known properties: ])";
        Assertions.assertTrue(
            unrecognizedPropertyException.getMessage().startsWith(expectedMessagePrefix)
        );
    }

    private URL getSamplePersonFileURL() {
        return this.getClass().getClassLoader().getResource("person.json");
    }

    private static String getNameThroughReflection(PersonPublicFieldsFinalNoArgsConstructor value) {
        try {
            return PersonPublicFieldsFinalNoArgsConstructor.class.getDeclaredField("name").get(value).toString();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getAgeThroughReflection(PersonPublicFieldsFinalNoArgsConstructor value) {
        try {
            return PersonPublicFieldsFinalNoArgsConstructor.class.getDeclaredField("age").getInt(value);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
