package seedu.tutorpal.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.tutorpal.commons.util.StringUtil;
import seedu.tutorpal.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Class} matches any of the keywords given.
 */
public class ClassContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public ClassContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * Returns the keywords used for matching as an unmodifiable list.
     */
    public java.util.List<String> getKeywords() {
        return java.util.Collections.unmodifiableList(keywords);
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> person.getClasses().stream()
                        .anyMatch(personClass -> StringUtil.containsWordIgnoreCase(personClass.value, keyword)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ClassContainsKeywordsPredicate)) {
            return false;
        }

        ClassContainsKeywordsPredicate otherClassContainsKeywordsPredicate = (ClassContainsKeywordsPredicate) other;
        return keywords.equals(otherClassContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}

