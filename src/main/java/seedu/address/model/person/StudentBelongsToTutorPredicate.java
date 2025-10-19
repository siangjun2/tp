package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Class} matches any of the tutor's classes.
 * This predicate finds tutors by name and filters students who share classes with them.
 */
public class StudentBelongsToTutorPredicate implements Predicate<Person> {
    private final List<String> tutorNames;
    private List<String> tutorClassKeywords;

    /**
     * Constructs the Predicate with the tutor name we wish to find whom
     * the students are under.
     * @param tutorNames Name of the tutor.
     */
    public StudentBelongsToTutorPredicate(List<String> tutorNames) {
        this.tutorNames = tutorNames;
        this.tutorClassKeywords = null; // Will be populated when we find the tutor
    }

    /**
     * Returns the tutor names used for matching as an unmodifiable list.
     */
    public java.util.List<String> getTutorNames() {
        return java.util.Collections.unmodifiableList(tutorNames);
    }

    /**
     * Sets the tutor class keywords after finding the tutor.
     * This method should be called before using the predicate.
     */
    public void setTutorClassKeywords(List<String> tutorClassKeywords) {
        this.tutorClassKeywords = tutorClassKeywords;
    }

    @Override
    public boolean test(Person person) {
        // Only filter students (not tutors)
        if (!"student".equalsIgnoreCase(person.getRole().value)) {
            return false;
        }
        // If we haven't found the tutor's classes yet, return false
        if (tutorClassKeywords == null || tutorClassKeywords.isEmpty()) {
            return false;
        }
        // Check if student has any classes that match the tutor's classes
        return tutorClassKeywords.stream()
                .anyMatch(tutorClass -> person.getClasses().stream()
                        .anyMatch(studentClass -> StringUtil.containsWordIgnoreCase(studentClass.value, tutorClass)));
    }

    /**
     * Finds tutors by name and extracts their classes.
     * Returns the list of tutor class keywords.
     */
    public List<String> findTutorClasses(List<Person> allPersons) {
        return allPersons.stream()
                .filter(person -> "tutor".equalsIgnoreCase(person.getRole().value))
                .filter(person -> tutorNames.stream()
                        .anyMatch(tutorName -> person
                                .getName().fullName.toLowerCase().contains(tutorName.toLowerCase())))
                .flatMap(tutor -> tutor.getClasses().stream())
                .map(personClass -> personClass.value)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof StudentBelongsToTutorPredicate)) {
            return false;
        }

        StudentBelongsToTutorPredicate otherStudentBelongsToTutorPredicate = (StudentBelongsToTutorPredicate) other;
        return tutorNames.equals(otherStudentBelongsToTutorPredicate.tutorNames);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("tutorNames", tutorNames)
                .add("tutorClassKeywords", tutorClassKeywords)
                .toString();
    }

}
