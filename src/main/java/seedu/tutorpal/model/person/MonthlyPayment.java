package seedu.tutorpal.model.person;

import static java.util.Objects.requireNonNull;

import java.time.YearMonth;

/**
 * Represents a payment status for a specific month.
 * Guarantees: immutable; month and status are not null.
 */
public class MonthlyPayment {
    private final YearMonth month;
    private final boolean isPaid;
    /**
     * Constructs a {@code MonthlyPayment}.
     *
     * @param month The month this payment is for.
     * @param isPaid Whether the payment for this month is paid.
     */
    public MonthlyPayment(YearMonth month, boolean isPaid) {
        requireNonNull(month);
        this.month = month;
        this.isPaid = isPaid;
    }
    /**
     * Returns the month this payment is for.
     */
    public YearMonth getMonth() {
        return month;
    }
    /**
     * Returns whether the payment for this month is paid.
     */
    public boolean isPaid() {
        return isPaid;
    }
    /**
     * Returns a new MonthlyPayment with the specified paid status.
     */
    public MonthlyPayment withPaidStatus(boolean isPaid) {
        return new MonthlyPayment(this.month, isPaid);
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof MonthlyPayment)) {
            return false;
        }
        MonthlyPayment otherPayment = (MonthlyPayment) other;
        return month.equals(otherPayment.month)
                && isPaid == otherPayment.isPaid;
    }
    @Override
    public int hashCode() {
        return month.hashCode() + Boolean.hashCode(isPaid);
    }
    @Override
    public String toString() {
        return month + ": " + (isPaid ? "paid" : "unpaid");
    }
}
