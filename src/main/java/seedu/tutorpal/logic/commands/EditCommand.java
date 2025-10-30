package seedu.tutorpal.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_JOIN_DATE;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.tutorpal.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.commons.util.CollectionUtil;
import seedu.tutorpal.commons.util.ToStringBuilder;
import seedu.tutorpal.logic.Messages;
import seedu.tutorpal.logic.commands.exceptions.CommandException;
import seedu.tutorpal.model.Model;
import seedu.tutorpal.model.person.Address;
import seedu.tutorpal.model.person.AttendanceHistory;
import seedu.tutorpal.model.person.Class;
import seedu.tutorpal.model.person.Email;
import seedu.tutorpal.model.person.JoinDate;
import seedu.tutorpal.model.person.Name;
import seedu.tutorpal.model.person.PaymentHistory;
import seedu.tutorpal.model.person.Person;
import seedu.tutorpal.model.person.Phone;
import seedu.tutorpal.model.person.Role;
import seedu.tutorpal.model.person.Student;
import seedu.tutorpal.model.person.Tutor;
import seedu.tutorpal.model.person.exceptions.InvalidRangeException;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_CLASS + "CLASS] "
            + "[" + PREFIX_JOIN_DATE + "JOINDATE] "
            + "...\n"
            + "Note: Tutors may specify multiple classes by repeating " + PREFIX_CLASS + "; "
            + "students must specify exactly one class.\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_USAGE_SHORTENED = COMMAND_WORD + ":\t\t" + COMMAND_WORD + " "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_CLASS + "CLASS]... "
            + "[" + PREFIX_ADDRESS + "ADDRESS]\n"
            + "\t\tNote: Tutors may specify multiple classes by repeating " + PREFIX_CLASS + "; "
            + "students must specify exactly one class.\n"
            + "\t\tExample: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";
    public static final String MESSAGE_STUDENT_MULTIPLE_CLASSES = "Students can only have one class. "
            + "Please specify only one class.";
    public static final String MESSAGE_INVALID_EDIT = "Invalid argument %1$s given to Edit Command!";
    public static final String MESSAGE_AT_LEAST_ONE_CLASS = "At least one class must be specified.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index                of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        // Check class constraints before creating edited person
        if (editPersonDescriptor.getClasses().isPresent()) {
            Set<Class> newClasses = editPersonDescriptor.getClasses().get();
            if (newClasses.isEmpty()) {
                throw new CommandException(MESSAGE_AT_LEAST_ONE_CLASS);
            }
            Role roleToCheck = personToEdit.getRole();
            if (roleToCheck == Role.STUDENT && newClasses.size() > 1) {
                throw new CommandException(MESSAGE_STUDENT_MULTIPLE_CLASSES);
            }
        }

        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor)
            throws CommandException {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Set<Class> updatedClasses = editPersonDescriptor.getClasses().orElse(personToEdit.getClasses());
        JoinDate updatedJoinDate = editPersonDescriptor.getJoinDate().orElse(personToEdit.getJoinDate());

        // Validate that new joinDate is not after existing payment history
        try {
            personToEdit.getPaymentHistory().validateJoinDate(updatedJoinDate);
        } catch (InvalidRangeException e) {
            throw new CommandException(e.getMessage());
        }

        // Rebuild payment history only if join date actually changes
        PaymentHistory updatedPaymentHistory = personToEdit.getPaymentHistory();
        if (!updatedJoinDate.equals(personToEdit.getJoinDate())) {
            try {
                updatedPaymentHistory = personToEdit.getPaymentHistory().withJoinDate(updatedJoinDate);
            } catch (InvalidRangeException e) {
                throw new CommandException(e.getMessage());
            }
        }

        // Construct appropriate subtype; role is not editable per parser
        if (personToEdit.getRole() == Role.STUDENT) {
            // Validating invariant : Student always have attendance history
            AttendanceHistory oldAttendanceHistory = personToEdit.getAttendanceHistory();
            assert oldAttendanceHistory != null;
            // When editing person, AttendanceHistory is reinitialised with updated Join date.
            // Constructor then checks and ensure all WeeklyAttendance inside are still valid in the new range.
            AttendanceHistory updatedAttendanceHistory;
            try {
                updatedAttendanceHistory = oldAttendanceHistory.changeJoinDate(updatedJoinDate);
            } catch (IllegalArgumentException e) {
                // Surface AttendanceHistory validation failures to the user
                throw new CommandException(e.getMessage());
            }
            return new Student(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedClasses,
                    updatedJoinDate, updatedAttendanceHistory, updatedPaymentHistory);
        } else {
            // Validating invariant : Tutor never have attendance history
            if (personToEdit.hasAttendanceHistory()) {
                // Defensive: should never happen since roles cannot be swapped
                throw new IllegalStateException(String.format(Person.MESSAGE_NO_ATTENDANCE_HISTORY, Tutor.PERSON_WORD));
            }
            return new Tutor(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedClasses,
                    updatedJoinDate, updatedPaymentHistory);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will
     * replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Class> classes;
        private JoinDate joinDate;

        public EditPersonDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code classes} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setClasses(toCopy.classes);
            setJoinDate(toCopy.joinDate);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, classes, joinDate);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code classes} to this object's {@code classes}.
         * A defensive copy of {@code classes} is used internally.
         */
        public void setClasses(Set<Class> classes) {
            this.classes = (classes != null) ? new HashSet<>(classes) : null;
        }

        /**
         * Returns an unmodifiable class set, which throws
         * {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code classes} is null.
         */
        public Optional<Set<Class>> getClasses() {
            return (classes != null) ? Optional.of(Collections.unmodifiableSet(classes)) : Optional.empty();
        }

        /**
         * Sets {@code joinDate} to this object's {@code joinDate}
         * @param joinDate New Join date
         */
        public void setJoinDate(JoinDate joinDate) {
            this.joinDate = joinDate;
        }

        /**
         * Returns {@code joinDate}.
         * @return joinDate New join date
         */
        public Optional<JoinDate> getJoinDate() {
            return Optional.ofNullable(joinDate);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && Objects.equals(classes, otherEditPersonDescriptor.classes)
                    && Objects.equals(joinDate, otherEditPersonDescriptor.joinDate);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("classes", classes)
                    .add("joinDate", joinDate)
                    .toString();
        }
    }
}
