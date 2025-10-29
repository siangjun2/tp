package seedu.tutorpal.model.person;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a person's payment history across multiple months.
 * Guarantees: immutable value object that manages monthly payment statuses.
 */
public class PaymentHistory {
    private final LocalDate joinDate;
    private final Set<MonthlyPayment> monthlyPayments;
    /**
     * Constructs a {@code PaymentHistory} with the given join date.
     * Automatically initializes payment history from join date to current month.
     * All months are initially marked as unpaid.
     *
     * @param joinDate The date when the person joined the system (must not be null)
     */
    public PaymentHistory(LocalDate joinDate) {
        assert joinDate != null : "Join date cannot be null";
        this.joinDate = joinDate;
        this.monthlyPayments = initializePaymentHistory(joinDate);
        assert this.monthlyPayments != null : "Monthly payments should not be null after initialization";
    }
    /**
     * Constructs a {@code PaymentHistory} with existing payment data.
     * Used for deserialization from storage and when updating payment history.
     * Automatically filters out payments before the join date to maintain data validity.
     *
     * @param joinDate The date when the person joined the system (must not be null)
     * @param monthlyPayments Existing payment data (must not be null)
     */
    public PaymentHistory(LocalDate joinDate, Set<MonthlyPayment> monthlyPayments) {
        assert joinDate != null : "Join date cannot be null";
        assert monthlyPayments != null : "Monthly payments cannot be null";
        this.joinDate = joinDate;
        YearMonth joinMonth = YearMonth.from(joinDate);
        // Filter out payments before join date to maintain data validity
        this.monthlyPayments = monthlyPayments.stream()
            .filter(payment -> !payment.getMonth().isBefore(joinMonth))
            .collect(Collectors.toSet());
        assert this.monthlyPayments != null : "Monthly payments should not be null after filtering";
    }
    /**
     * Initializes payment history from join date to current month.
     * All months are initially set to unpaid.
     *
     * @param joinDate the join date to initialize from
     * @return a set of monthly payments from join date to current month
     */
    private Set<MonthlyPayment> initializePaymentHistory(LocalDate joinDate) {
        assert joinDate != null : "Join date cannot be null";
        Set<MonthlyPayment> payments = new HashSet<>();
        YearMonth currentMonth = YearMonth.now();
        YearMonth joinMonth = YearMonth.from(joinDate);
        YearMonth month = joinMonth;
        while (!month.isAfter(currentMonth)) {
            payments.add(new MonthlyPayment(month, false));
            month = month.plusMonths(1);
        }
        assert !payments.isEmpty() : "Should have at least one month in payment history";
        return payments;
    }

    /**
     * Returns the join date.
     *
     * @return the join date
     */
    public LocalDate getJoinDate() {
        return joinDate;
    }

    /**
     * Returns a defensive copy of the monthly payments set.
     *
     * @return a copy of the monthly payments
     */
    public Set<MonthlyPayment> getMonthlyPayments() {
        return new HashSet<>(monthlyPayments);
    }

    public List<MonthlyPayment> getLatestPayments() {
        List<MonthlyPayment> sorted = monthlyPayments.stream()
            .sorted((a, b) -> b.getMonth().compareTo(a.getMonth()))
            .limit(6)
            .toList();

        return sorted;
    }

    /**
     * Marks a specific month as unpaid.
     *
     * @param month The month to mark as unpaid (must not be null)
     * @return A new PaymentHistory with the updated payment status
     * @throws IllegalArgumentException if the month is before join date or in the future
     */
    public PaymentHistory markMonthAsUnpaid(YearMonth month) {
        assert month != null : "Month cannot be null";
        validateMonth(month);
        Set<MonthlyPayment> newPayments = new HashSet<>(monthlyPayments);
        // Remove existing payment for this month if it exists
        newPayments.removeIf(payment -> payment.getMonth().equals(month));
        // Add the updated payment as unpaid
        newPayments.add(new MonthlyPayment(month, false));
        return new PaymentHistory(joinDate, newPayments);
    }

    /**
     * Marks a specific month as paid.
     *
     * @param month The month to mark as paid (must not be null)
     * @return A new PaymentHistory with the updated payment status
     * @throws IllegalArgumentException if the month is before join date or in the future
     */
    public PaymentHistory markMonthAsPaid(YearMonth month) {
        assert month != null : "Month cannot be null";
        validateMonth(month);
        Set<MonthlyPayment> newPayments = new HashSet<>(monthlyPayments);
        // Remove existing payment for this month if it exists
        newPayments.removeIf(payment -> payment.getMonth().equals(month));
        // Add the updated payment
        newPayments.add(new MonthlyPayment(month, true));
        return new PaymentHistory(joinDate, newPayments);
    }

    /**
     * Validates that a month is within the valid range for payment tracking.
     * The month must be on or after the join date and cannot be in the future.
     *
     * @param month The month to validate
     * @throws IllegalArgumentException if the month violates any constraints
     */
    private void validateMonth(YearMonth month) {
        if (month.isBefore(YearMonth.from(joinDate))) {
            throw new IllegalArgumentException("Cannot mark payment for month before join date");
        }
        if (month.isAfter(YearMonth.now())) {
            throw new IllegalArgumentException("Cannot mark payment for future month");
        }
    }
    /**
     * Returns the overall payment status based on monthly payments.
     * @return the overall payment status
     */
    public String getOverallStatus() {
        YearMonth currentMonth = YearMonth.now();
        YearMonth joinMonth = YearMonth.from(joinDate);
        // Check if any previous month is unpaid (overdue)
        YearMonth month = joinMonth;
        while (month.isBefore(currentMonth)) {
            if (!isMonthPaid(month)) {
                return "overdue";
            }
            month = month.plusMonths(1);
        }
        // Check current month
        boolean currentMonthPaid = isMonthPaid(currentMonth);
        return currentMonthPaid ? "paid" : "unpaid";
    }

    /**
     * Returns whether a specific month is paid.
     *
     * @param month The month to check (must not be null)
     * @return true if the month is marked as paid, false otherwise
     */
    public boolean isMonthPaid(YearMonth month) {
        assert month != null : "Month cannot be null";
        return monthlyPayments.stream()
            .filter(payment -> payment.getMonth().equals(month))
            .findFirst()
            .map(MonthlyPayment::isPaid)
            .orElse(false);
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
        return "PaymentHistory{"
            + "joinDate=" + joinDate
            + ", monthlyPayments="
            + monthlyPayments
            + ", status="
            + getOverallStatus()
            + '}';
    }
}
