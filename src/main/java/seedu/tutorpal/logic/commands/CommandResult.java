package seedu.tutorpal.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.tutorpal.commons.util.ToStringBuilder;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** Detailed information about contact should be shown to the user */
    private final boolean showDisplay;

    private String personInfo;

    /** The application should exit. */
    private final boolean exit;

    /**
     * Constructs a {@code CommandResult} with the specified fields and set showDisplay to false.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.showDisplay = false;
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields, including showDisplay.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit, String personInfo) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.showDisplay = true;
        this.personInfo = personInfo;
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public String getPersonInfo() {
        if (this.personInfo == null) {
            return "There is no information on this person.";
        }
        return this.personInfo;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isShowDisplay() {
        return showDisplay;
    }

    public boolean isExit() {
        return exit;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && showDisplay == otherCommandResult.showDisplay
                && exit == otherCommandResult.exit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, showDisplay, exit);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("showDisplay", showDisplay)
                .add("exit", exit)
                .toString();
    }

}
