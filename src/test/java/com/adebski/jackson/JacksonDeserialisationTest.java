package com.adebski.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.stream.Stream;

public class JacksonDeserialisationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void deserialiseWithPublicFieldsNoArgConstructor() throws IOException {
        PersonPublicFieldsNoArgConstructor deserialisedValue =
            objectMapper.readValue(getSamplePersonFileURL(), PersonPublicFieldsNoArgConstructor.class);

        System.out.println(deserialisedValue);
        PersonPublicFieldsNoArgConstructor expectedValue = new PersonPublicFieldsNoArgConstructor();
        expectedValue.name = "fooName";
        expectedValue.age = 23;
        Assertions.assertEquals(expectedValue, deserialisedValue);
    }

    @Test
    public void deserialiseWithPrivateFieldsNoArgConstructor() throws IOException {
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
    public void deserialiseWithPrivateFieldsNoArgConstructorWithAdditionalConfig() throws IOException {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        PersonPrivateFieldsNoArgConstructor deserialisedValue =
            objectMapper.readValue(getSamplePersonFileURL(), PersonPrivateFieldsNoArgConstructor.class);
        System.out.println(deserialisedValue);
        Assertions.assertEquals(PersonPrivateFieldsNoArgConstructor.getExpectedValue(), deserialisedValue);
    }

    @Test
    public void deserialiseWithPersonPublicFieldsFinalNoArgsConstructorDefaultConfiguration() throws IOException {
        PersonPublicFieldsFinalNoArgsConstructor deserialisedValue =
            objectMapper.readValue(getSamplePersonFileURL(), PersonPublicFieldsFinalNoArgsConstructor.class);
        PersonPublicFieldsFinalNoArgsConstructor manuallyConstructedValue = new PersonPublicFieldsFinalNoArgsConstructor();
        System.out.println("Deserialised value: " + deserialisedValue);
        System.out.println("Manually constructed value: " + manuallyConstructedValue);

        Assertions.assertEquals(
            manuallyConstructedValue,
            deserialisedValue
        );
        System.out.println(
            String.format(
                "Actual name '%s' actual age '%d'",
                getNameThroughReflection(deserialisedValue),
                getAgeThroughReflection(deserialisedValue)
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
            getNameThroughReflection(deserialisedValue),
            deserialisedValue.getName()
        );
        Assertions.assertNotEquals(
            getAgeThroughReflection(deserialisedValue),
            deserialisedValue.getAge()
        );
    }

    @Test
    public void deserialiseWithPersonPublicFieldsFinalNoArgsExplicitConstructorDefaultConfiguration() throws IOException {
        PersonPublicFieldsFinalNoArgsExplicitConstructor deserialisedValue =
            objectMapper.readValue(getSamplePersonFileURL(), PersonPublicFieldsFinalNoArgsExplicitConstructor.class);
        PersonPublicFieldsFinalNoArgsExplicitConstructor manuallyConstructedValue = new PersonPublicFieldsFinalNoArgsExplicitConstructor();
        System.out.println("Deserialised value: " + deserialisedValue);
        System.out.println("Manually constructed value: " + manuallyConstructedValue);

        Assertions.assertNotEquals(
            manuallyConstructedValue,
            deserialisedValue
        );
        Assertions.assertEquals(
            "fooName",
            deserialisedValue.name
        );
        Assertions.assertEquals(
            23,
            deserialisedValue.age
        );
    }

    @Test
    public void deserialiseWithPersonPublicFieldsFinalNoArgsConstructorDoNotModifyFinalFields() throws IOException {
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
