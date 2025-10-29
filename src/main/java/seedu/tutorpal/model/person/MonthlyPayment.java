package seedu.tutorpal.model.person;

import java.time.YearMonth;
import java.util.Objects;

/**
 * Represents a payment status for a specific month.
 * Guarantees: immutable value object representing a month's payment status.
 */
public class MonthlyPayment {
    private final YearMonth month;
    private final boolean isPaid;

    /**
     * Constructs a {@code MonthlyPayment}.
     *
     * @param month The month this payment is for (must not be null)
     * @param isPaid Whether the payment for this month is paid
     */
    public MonthlyPayment(YearMonth month, boolean isPaid) {
        assert month != null : "Month cannot be null";
        this.month = month;
        this.isPaid = isPaid;
    }

    /**
     * Returns the month this payment is for.
     *
     * @return the month
     */
    public YearMonth getMonth() {
        return month;
    }

    /**
     * Returns whether the payment for this month is paid.
     *
     * @return true if paid, false otherwise
     */
    public boolean isPaid() {
        return isPaid;
    }

    /**
     * Returns a new MonthlyPayment with the specified paid status.
     * This preserves the month while updating the paid status.
     *
     * @param isPaid the new paid status
     * @return a new MonthlyPayment with the updated status
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
        return Objects.hash(month, isPaid);
    }

    @Override
    public String toString() {
        return month + ": " + (isPaid ? "paid" : "unpaid");
    }
}
