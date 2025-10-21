package seedu.tutorpal.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.tutorpal.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void constructor_validTagName_success() {
        Tag tag = new Tag("friend");
        assertEquals("friend", tag.tagName);
    }

    @Test
    public void isValidTagName_validNames_returnsTrue() {
        // Alphanumeric tags
        assertTrue(Tag.isValidTagName("friend"));
        assertTrue(Tag.isValidTagName("Friend"));
        assertTrue(Tag.isValidTagName("FRIEND"));
        assertTrue(Tag.isValidTagName("friend123"));
        assertTrue(Tag.isValidTagName("123"));
        assertTrue(Tag.isValidTagName("f"));
        assertTrue(Tag.isValidTagName("F1R3ND"));
    }

    @Test
    public void isValidTagName_invalidNames_returnsFalse() {
        // Empty string
        assertFalse(Tag.isValidTagName(""));

        // Contains spaces
        assertFalse(Tag.isValidTagName("best friend"));
        assertFalse(Tag.isValidTagName("friend "));
        assertFalse(Tag.isValidTagName(" friend"));

        // Contains special characters
        assertFalse(Tag.isValidTagName("friend!"));
        assertFalse(Tag.isValidTagName("friend@gmail"));
        assertFalse(Tag.isValidTagName("friend#1"));
        assertFalse(Tag.isValidTagName("friend-colleague"));
        assertFalse(Tag.isValidTagName("friend_colleague"));
        assertFalse(Tag.isValidTagName("friend.colleague"));
        assertFalse(Tag.isValidTagName("friend,colleague"));

        // Contains only special characters
        assertFalse(Tag.isValidTagName("!@#"));
        assertFalse(Tag.isValidTagName("-"));
        assertFalse(Tag.isValidTagName("_"));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Tag tag = new Tag("friend");
        assertTrue(tag.equals(tag));
    }

    @Test
    public void equals_sameValue_returnsTrue() {
        Tag tag1 = new Tag("friend");
        Tag tag2 = new Tag("friend");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void equals_differentCase_returnsFalse() {
        Tag tag1 = new Tag("friend");
        Tag tag2 = new Tag("Friend");
        assertFalse(tag1.equals(tag2)); // Case-sensitive
    }

    @Test
    public void equals_differentValue_returnsFalse() {
        Tag tag1 = new Tag("friend");
        Tag tag2 = new Tag("colleague");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void equals_null_returnsFalse() {
        Tag tag = new Tag("friend");
        assertFalse(tag.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        Tag tag = new Tag("friend");
        assertFalse(tag.equals("friend"));
    }

    @Test
    public void hashCode_sameValue_returnsSameHashCode() {
        Tag tag1 = new Tag("friend");
        Tag tag2 = new Tag("friend");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void toString_returnsCorrectFormat() {
        Tag tag = new Tag("friend");
        assertEquals("[friend]", tag.toString());
    }

    @Test
    public void toString_withNumbers_returnsCorrectFormat() {
        Tag tag = new Tag("friend123");
        assertEquals("[friend123]", tag.toString());
    }
}
