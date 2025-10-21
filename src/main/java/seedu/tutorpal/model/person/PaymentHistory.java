package seedu.tutorpal.model.person;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a person's payment history across multiple months.
 * Guarantees: immutable; manages monthly payment statuses.
 */
public class PaymentHistory {
    private final LocalDate joinDate;
    private final Map<YearMonth, Boolean> monthlyPayments;
    /**
     * Constructs a {@code PaymentHistory} with the given join date.
     * Automatically initializes payment history from join date to current month.
     *
     * @param joinDate The date when the person joined the system.
     */
    public PaymentHistory(LocalDate joinDate) {
        requireNonNull(joinDate);
        this.joinDate = joinDate;
        this.monthlyPayments = initializePaymentHistory(joinDate);
    }
    /**
     * Constructs a {@code PaymentHistory} with existing payment data.
     * Used for deserialization from storage.
     *
     * @param joinDate The date when the person joined the system.
     * @param monthlyPayments Existing payment data.
     */
    public PaymentHistory(LocalDate joinDate, Map<YearMonth, Boolean> monthlyPayments) {
        requireNonNull(joinDate);
        requireNonNull(monthlyPayments);
        this.joinDate = joinDate;
        this.monthlyPayments = new HashMap<>(monthlyPayments);
    }
    /**
     * Initializes payment history from join date to current month.
     * All months are initially set to unpaid.
     */
    private Map<YearMonth, Boolean> initializePaymentHistory(LocalDate joinDate) {
        Map<YearMonth, Boolean> payments = new HashMap<>();
        YearMonth currentMonth = YearMonth.now();
        YearMonth joinMonth = YearMonth.from(joinDate);
        YearMonth month = joinMonth;
        while (!month.isAfter(currentMonth)) {
            payments.put(month, false);
            month = month.plusMonths(1);
        }
        return payments;
    }
    /**
     * Returns the join date.
     */
    public LocalDate getJoinDate() {
        return joinDate;
    }
    /**
     * Returns a copy of the monthly payments map.
     */
    public Map<YearMonth, Boolean> getMonthlyPayments() {
        return new HashMap<>(monthlyPayments);
    }
    /**
     * Marks a specific month as paid.
     *
     * @param month The month to mark as paid.
     * @return A new PaymentHistory with the updated payment status.
     */
    public PaymentHistory markMonthAsPaid(YearMonth month) {
        requireNonNull(month);
        if (month.isBefore(YearMonth.from(joinDate))) {
            throw new IllegalArgumentException("Cannot mark payment for month before join date");
        }
        if (month.isAfter(YearMonth.now())) {
            throw new IllegalArgumentException("Cannot mark payment for future month");
        }
        Map<YearMonth, Boolean> newPayments = new HashMap<>(monthlyPayments);
        newPayments.put(month, true);
        return new PaymentHistory(joinDate, newPayments);
    }
    /**
     * Returns the overall payment status based on monthly payments.
     * - "paid": All months including current month are paid
     * - "unpaid": All previous months paid, current month unpaid
     * - "overdue": Any previous month is unpaid
     */
    public String getOverallStatus() {
        YearMonth currentMonth = YearMonth.now();
        YearMonth joinMonth = YearMonth.from(joinDate);
        // Check if any previous month is unpaid (overdue)
        YearMonth month = joinMonth;
        while (month.isBefore(currentMonth)) {
            if (!monthlyPayments.getOrDefault(month, false)) {
                return "overdue";
            }
            month = month.plusMonths(1);
        }
        // Check current month
        boolean currentMonthPaid = monthlyPayments.getOrDefault(currentMonth, false);
        return currentMonthPaid ? "paid" : "unpaid";
    }
    /**
     * Returns whether a specific month is paid.
     */
    public boolean isMonthPaid(YearMonth month) {
        return monthlyPayments.getOrDefault(month, false);
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PaymentHistory)) {
            return false;
        }
        PaymentHistory otherHistory = (PaymentHistory) other;
        return joinDate.equals(otherHistory.joinDate)
                && monthlyPayments.equals(otherHistory.monthlyPayments);
    }
    @Override
    public int hashCode() {
        return Objects.hash(joinDate, monthlyPayments);
    }
    @Override
    public String toString() {
        return "PaymentHistory{" +
                "joinDate=" + joinDate +
                ", monthlyPayments=" + monthlyPayments +
                ", status=" + getOverallStatus() +
                '}';
    }
}
