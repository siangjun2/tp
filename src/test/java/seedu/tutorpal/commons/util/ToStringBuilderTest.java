package seedu.tutorpal.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ToStringBuilderTest {
    @Test
    void objectConstructorProducesExpectedString() {
        String expected = "seedu.tutorpal.commons.util.ToStringBuilder{}";
        String actual = new ToStringBuilder(new ToStringBuilder("doesn't matter")).toString();
        assertEquals(expected, actual);
    }

    @Test
    void stringConstructorProducesExpectedString() {
        String expected = "whatever{}";
        String actual = new ToStringBuilder("whatever").toString();
        assertEquals(expected, actual);
    }

    @Test
    void objectConstructorWithNullThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ToStringBuilder((Object) null));
    }

    @Test
    void addSingleFieldProducesExpectedString() {
        ToStringBuilder builder = new ToStringBuilder("Person");
        String result = builder.add("name", "Alice").toString();
        String expected = "Person{name=Alice}";
        assertEquals(expected, result);
    }

    @Test
    void addMultipleFieldsAndChainingProducesExpectedString() {
        ToStringBuilder builder = new ToStringBuilder("Pair");
        String result = builder.add("a", 1).add("b", 2).toString();
        String expected = "Pair{a=1, b=2}";
        assertEquals(expected, result);
    }

    @Test
    void nullFieldValueIsRenderedAsStringNull() {
        ToStringBuilder builder = new ToStringBuilder("Nullable");
        String result = builder.add("x", null).toString();
        String expected = "Nullable{x=null}";
        assertEquals(expected, result);
    }
}
