package seedu.tutorpal.model.person;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a person's payment history across multiple months.
 * Guarantees: immutable; manages monthly payment statuses.
 */
public class PaymentHistory {
    private final LocalDate joinDate;
    private final Set<MonthlyPayment> monthlyPayments;
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
    public PaymentHistory(LocalDate joinDate, Set<MonthlyPayment> monthlyPayments) {
        requireNonNull(joinDate);
        requireNonNull(monthlyPayments);
        this.joinDate = joinDate;
        YearMonth joinMonth = YearMonth.from(joinDate);
        // Filter out payments before join date to maintain data validity
        this.monthlyPayments = monthlyPayments.stream()
                .filter(payment -> !payment.getMonth().isBefore(joinMonth))
                .collect(Collectors.toSet());
    }
    /**
     * Initializes payment history from join date to current month.
     * All months are initially set to unpaid.
     */
    private Set<MonthlyPayment> initializePaymentHistory(LocalDate joinDate) {
        Set<MonthlyPayment> payments = new HashSet<>();
        YearMonth currentMonth = YearMonth.now();
        YearMonth joinMonth = YearMonth.from(joinDate);
        YearMonth month = joinMonth;
        while (!month.isAfter(currentMonth)) {
            payments.add(new MonthlyPayment(month, false));
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
     * Returns a copy of the monthly payments set.
     */
    public Set<MonthlyPayment> getMonthlyPayments() {
        return new HashSet<>(monthlyPayments);
    }
    /**
     * Marks a specific month as unpaid.
     *
     * @param month The month to mark as unpaid.
     * @return A new PaymentHistory with the updated payment status.
     */
    public PaymentHistory markMonthAsUnpaid(YearMonth month) {
        requireNonNull(month);
        if (month.isBefore(YearMonth.from(joinDate))) {
            throw new IllegalArgumentException("Cannot mark payment for month before join date");
        }
        if (month.isAfter(YearMonth.now())) {
            throw new IllegalArgumentException("Cannot mark payment for future month");
        }
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
        Set<MonthlyPayment> newPayments = new HashSet<>(monthlyPayments);
        // Remove existing payment for this month if it exists
        newPayments.removeIf(payment -> payment.getMonth().equals(month));
        // Add the updated payment
        newPayments.add(new MonthlyPayment(month, true));
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
     */
    public boolean isMonthPaid(YearMonth month) {
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
