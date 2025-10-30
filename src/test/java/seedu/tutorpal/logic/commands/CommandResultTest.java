package seedu.tutorpal.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CommandResultTest {
    @Test
    public void equals() {
        CommandResult commandResult = new CommandResult("feedback");

        // same values -> returns true
        assertTrue(commandResult.equals(new CommandResult("feedback")));
        assertTrue(commandResult.equals(new CommandResult("feedback", false, false)));

        // same object -> returns true
        assertTrue(commandResult.equals(commandResult));

        // null -> returns false
        assertFalse(commandResult.equals(null));

        // different types -> returns false
        assertFalse(commandResult.equals(0.5f));

        // different feedbackToUser value -> returns false
        assertFalse(commandResult.equals(new CommandResult("different")));

        // different showHelp value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", true, false)));

        // different exit value -> returns false
        assertFalse(commandResult.equals(new CommandResult("feedback", false, true)));
    }

    @Test
    public void hashcode() {
        CommandResult commandResult = new CommandResult("feedback");

        // same values -> returns same hashcode
        assertEquals(commandResult.hashCode(), new CommandResult("feedback").hashCode());

        // different feedbackToUser value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("different").hashCode());

        // different showHelp value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", true, false).hashCode());

        // different exit value -> returns different hashcode
        assertNotEquals(commandResult.hashCode(), new CommandResult("feedback", false, true).hashCode());
    }

    @Test
    public void toStringMethod() {
        CommandResult commandResult = new CommandResult("feedback");
        String expected = CommandResult.class.getCanonicalName() + "{feedbackToUser="
                + commandResult.getFeedbackToUser() + ", showHelp=" + commandResult.isShowHelp()
                + ", showDisplay=" + commandResult.isShowDisplay() + ", exit=" + commandResult.isExit() + "}";
        assertEquals(expected, commandResult.toString());
    }

    @Test
    public void constructor_withPersonInfo_setsShowDisplayTrue_andStoresInfo() {
        CommandResult withInfo = new CommandResult("feedback", false, false, "Alice • +65 9999 9999");
        assertTrue(withInfo.isShowDisplay());
        assertEquals("Alice • +65 9999 9999", withInfo.getPersonInfo());
    }

    @Test
    public void getPersonInfo_whenNotProvided_returnsDefaultMessage() {
        CommandResult withoutInfo = new CommandResult("feedback");
        assertFalse(withoutInfo.isShowDisplay());
        assertEquals("There is no information on this person.", withoutInfo.getPersonInfo());
    }

    @Test
    public void equals_ignoresPersonInfoContent_butRespectsShowDisplay() {
        // personInfo content is ignored in equals()
        CommandResult a = new CommandResult("feedback", false, false, "A");
        CommandResult b = new CommandResult("feedback", false, false, "B");
        assertTrue(a.equals(b));

        // showDisplay difference matters
        CommandResult noDisplay = new CommandResult("feedback", false, false);
        CommandResult withDisplay = new CommandResult("feedback", false, false, "A");
        assertFalse(noDisplay.equals(withDisplay));
        assertFalse(withDisplay.equals(noDisplay));
    }

    @Test
    public void hashCode_differsWhenShowDisplayDiffers() {
        CommandResult noDisplay = new CommandResult("feedback", false, false);
        CommandResult withDisplay = new CommandResult("feedback", false, false, "Alice");
        assertNotEquals(noDisplay.hashCode(), withDisplay.hashCode());
    }

    @Test
    public void toString_includesShowDisplayTrueWhenPersonInfoPresent() {
        CommandResult withInfo = new CommandResult("feedback", false, false, "Alice");
        String expected = CommandResult.class.getCanonicalName()
            + "{feedbackToUser=" + withInfo.getFeedbackToUser()
            + ", showHelp=" + withInfo.isShowHelp()
            + ", showDisplay=" + withInfo.isShowDisplay()
            + ", exit=" + withInfo.isExit()
            + "}";
        assertEquals(expected, withInfo.toString());
    }
}
