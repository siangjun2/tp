package seedu.tutorpal.commons.core.index;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.tutorpal.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class IndexTest {

    /**
     * Testing creating {@Code Index} using {@link Index#fromOneBased(int)}.
     */
    @Test
    public void createOneBasedIndex() {
        // invalid index
        assertThrows(IndexOutOfBoundsException.class, () -> Index.fromOneBased(0));

        // check equality using the same base
        assertEquals(1, Index.fromOneBased(1).getOneBased());
        assertEquals(5, Index.fromOneBased(5).getOneBased());

        // convert from one-based index to zero-based index
        assertEquals(0, Index.fromOneBased(1).getZeroBased());
        assertEquals(4, Index.fromOneBased(5).getZeroBased());
    }

    /**
     * Testing creating {@Code Index} using {@link Index#fromZeroBased(int)}.
     */
    @Test
    public void createZeroBasedIndex() {
        // invalid index
        assertThrows(IndexOutOfBoundsException.class, () -> Index.fromZeroBased(-1));

        // check equality using the same base
        assertEquals(0, Index.fromZeroBased(0).getZeroBased());
        assertEquals(5, Index.fromZeroBased(5).getZeroBased());

        // convert from zero-based index to one-based index
        assertEquals(1, Index.fromZeroBased(0).getOneBased());
        assertEquals(6, Index.fromZeroBased(5).getOneBased());
    }

    /**
     * Test comparing {@Code Index} made using {@link Index#fromZeroBased(int)}
     * and {@link Index#fromOneBased(int)}.
     */
    @Test
    public void compareZeroAndOneBasedIndex() {
        //accessed via getZeroBased() : int
        assertEquals(Index.fromZeroBased(0).getZeroBased(),
                Index.fromOneBased(1).getZeroBased());
        assertEquals(Index.fromZeroBased(99).getZeroBased(),
                Index.fromOneBased(100).getZeroBased());

        //accessed via getOneBased() : int
        assertEquals(Index.fromZeroBased(0).getOneBased(),
                Index.fromOneBased(1).getOneBased());
        assertEquals(Index.fromZeroBased(99).getOneBased(),
                Index.fromOneBased(100).getOneBased());
    }

    @Test
    public void equals() {
        final Index fifthPersonIndex = Index.fromOneBased(5);

        // same values -> returns true
        assertTrue(fifthPersonIndex.equals(Index.fromOneBased(5)));
        assertTrue(fifthPersonIndex.equals(Index.fromZeroBased(4)));

        // same object -> returns true
        assertTrue(fifthPersonIndex.equals(fifthPersonIndex));

        // null -> returns false
        assertFalse(fifthPersonIndex.equals(null));

        // different types -> returns false
        assertFalse(fifthPersonIndex.equals(5.0f));

        // different index -> returns false
        assertFalse(fifthPersonIndex.equals(Index.fromOneBased(1)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromZeroBased(0);
        String expected = Index.class.getCanonicalName() + "{zeroBasedIndex=" + index.getZeroBased() + "}";
        assertEquals(expected, index.toString());
    }
}
