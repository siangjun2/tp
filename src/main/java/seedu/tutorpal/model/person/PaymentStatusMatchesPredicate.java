package seedu.tutorpal.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import seedu.tutorpal.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Payment} status matches any of the keywords given.
 */
public class PaymentStatusMatchesPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public PaymentStatusMatchesPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * Returns the keywords used for matching as an unmodifiable list.
     */
    public List<String> getKeywords() {
        return Collections.unmodifiableList(keywords);
    }

    @Override
    public boolean test(Person person) {
        requireNonNull(person);
        Payment paymentStatus = person.getPaymentStatus();
        return keywords.stream()
                .anyMatch(keyword -> paymentStatus.value.equalsIgnoreCase(keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PaymentStatusMatchesPredicate)) {
            return false;
        }

        PaymentStatusMatchesPredicate otherPredicate = (PaymentStatusMatchesPredicate) other;
        return keywords.equals(otherPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}

