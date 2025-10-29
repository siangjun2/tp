package seedu.tutorpal.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_JOIN_DATE;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_ROLE;

import seedu.tutorpal.commons.util.ToStringBuilder;
import seedu.tutorpal.logic.commands.exceptions.CommandException;
import seedu.tutorpal.model.Model;
import seedu.tutorpal.model.person.Person;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a student or tutor to the system. "
            + "A student can only have one class.\n"
            + "Parameters: "
            + PREFIX_ROLE + "ROLE "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_CLASS + "CLASS "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_JOIN_DATE + "JOINDATE] "
            + "[" + PREFIX_CLASS + "MORE_CLASSES]... \n"
            + "Note: Tutors may specify multiple classes by repeating " + PREFIX_CLASS + "; "
            + "students must specify exactly one class.\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ROLE + "student "
            + PREFIX_NAME + "Kevin "
            + PREFIX_PHONE + "98761234 "
            + PREFIX_EMAIL + "kevin@gmail.com "
            + PREFIX_ADDRESS + "Kent Ridge "
            + PREFIX_CLASS + "s4mon1600 "
            + PREFIX_JOIN_DATE + "06-10-2025";

    // SHORTENED is used for help command
    public static final String MESSAGE_USAGE_SHORTENED = COMMAND_WORD + ":\t\t" + COMMAND_WORD + " "
        + PREFIX_ROLE + "ROLE "
        + PREFIX_NAME + "NAME "
        + PREFIX_PHONE + "PHONE "
        + PREFIX_EMAIL + "EMAIL "
        + PREFIX_CLASS + "CLASS "
        + "[" + PREFIX_ADDRESS + "ADDRESS] "
        + "[" + PREFIX_JOIN_DATE + "JOINDATE] "
        + "[" + PREFIX_CLASS + "MORE_CLASSES]... \n"
        + "\t\tNote: Tutors may specify multiple classes by repeating " + PREFIX_CLASS + "; "
        + "students must specify exactly one class.\n"
        + "\t\tExample: " + COMMAND_WORD + " "
        + PREFIX_ROLE + "student "
        + PREFIX_NAME + "Kevin "
        + PREFIX_PHONE + "98761234 "
        + PREFIX_EMAIL + "kevin@gmail.com "
        + PREFIX_ADDRESS + "Kent Ridge "
        + PREFIX_CLASS + "s4mon1600 "
        + PREFIX_CLASS + "s4wed1400";

    public static final String MESSAGE_SUCCESS = "%1$s %2$s added successfully";
    public static final String MESSAGE_DUPLICATE_PERSON = "This student/tutor already exists in the system";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        String role = toAdd.getRole().toString();
        String name = toAdd.getName().toString();
        return new CommandResult(String.format(MESSAGE_SUCCESS, role, name));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
