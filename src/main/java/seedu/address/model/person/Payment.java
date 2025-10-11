package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's payment status in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPayment(String)}
 */
public class Payment {

    public static final String MESSAGE_CONSTRAINTS =
            "Work in progress";

    /*
     * Role must be exactly "student" or "tutor" (case-insensitive)
     */
    public static final String VALIDATION_REGEX = "(?i)(unpaid|overdue|paid)";

    public final String value;

    /**
     * Constructs a {@code Role}.
     *
     * @param paymentStatus A valid payment status.
     */
    public Payment(String paymentStatus) {
        requireNonNull(paymentStatus);
        String trimmedRole = paymentStatus.trim().toLowerCase();
        checkArgument(isValidPayment(trimmedRole), MESSAGE_CONSTRAINTS);
        value = trimmedRole;
    }

    /**
     * Returns true if a given string is a valid role.
     */
    public static boolean isValidPayment(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Payment)) {
            return false;
        }

        Payment otherPayment = (Payment) other;
        return value.equals(otherPayment.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
