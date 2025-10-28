package seedu.tutorpal.storage;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.tutorpal.commons.exceptions.IllegalValueException;
import seedu.tutorpal.model.person.MonthlyPayment;
import seedu.tutorpal.model.person.PaymentHistory;

/**
 * Jackson-friendly version of {@link PaymentHistory}.
 */
public class JsonAdaptedPaymentHistory {
    private final String joinDate;
    private final Set<JsonAdaptedMonthlyPayment> monthlyPayments;

    /**
     * Constructs a {@code JsonAdaptedPaymentHistory} with the given payment history details.
     * This constructor is used by Jackson for deserialization from JSON.
     *
     * @param joinDate The join date as a string
     * @param monthlyPayments The set of monthly payments
     */
    @JsonCreator
    public JsonAdaptedPaymentHistory(@JsonProperty("joinDate") String joinDate,
                                     @JsonProperty("monthlyPayments") Set<JsonAdaptedMonthlyPayment> monthlyPayments) {
        this.joinDate = joinDate;
        this.monthlyPayments = monthlyPayments;
    }

    /**
     * Converts a given {@code PaymentHistory} into this class for Jackson use.
     * This is used for serialization (model to JSON).
     *
     * @param source The PaymentHistory to convert
     */
    public JsonAdaptedPaymentHistory(PaymentHistory source) {
        assert source != null : "Source PaymentHistory cannot be null";
        this.joinDate = source.getJoinDate().toString();
        this.monthlyPayments = new HashSet<>();
        for (MonthlyPayment payment : source.getMonthlyPayments()) {
            this.monthlyPayments.add(new JsonAdaptedMonthlyPayment(payment));
        }
    }

    /**
     * Converts this Jackson-friendly adapted payment history object into the model's {@code PaymentHistory} object.
     * This is used for deserialization (JSON to model).
     *
     * @return The PaymentHistory object
     * @throws IllegalValueException if there are any data constraints violated
     */
    public PaymentHistory toModelType() throws IllegalValueException {
        LocalDate modelJoinDate = parseJoinDate();
        Set<MonthlyPayment> modelMonthlyPayments = parseMonthlyPayments(modelJoinDate);

        assert modelJoinDate != null : "Parsed join date should not be null";
        assert modelMonthlyPayments != null : "Parsed monthly payments should not be null";
        return new PaymentHistory(modelJoinDate, modelMonthlyPayments);
    }

    /**
     * Parses the join date string into a LocalDate.
     *
     * @return The parsed LocalDate
     * @throws IllegalValueException if the join date is missing or has invalid format
     */
    private LocalDate parseJoinDate() throws IllegalValueException {
        if (joinDate == null) {
            throw new IllegalValueException("Join date field is missing!");
        }

        try {
            return LocalDate.parse(joinDate);
        } catch (Exception e) {
            throw new IllegalValueException("Invalid join date format");
        }
    }

    /**
     * Parses the monthly payments, filtering out any payments before the join date.
     *
     * @param modelJoinDate The parsed join date
     * @return A set of valid MonthlyPayment objects
     * @throws IllegalValueException if any payment is invalid
     */
    private Set<MonthlyPayment> parseMonthlyPayments(LocalDate modelJoinDate) throws IllegalValueException {
        Set<MonthlyPayment> modelMonthlyPayments = new HashSet<>();
        YearMonth joinMonth = YearMonth.from(modelJoinDate);

        if (monthlyPayments != null) {
            for (JsonAdaptedMonthlyPayment adaptedPayment : monthlyPayments) {
                try {
                    MonthlyPayment payment = adaptedPayment.toModelType();
                    // Filter out payments before join date to maintain data validity
                    if (!payment.getMonth().isBefore(joinMonth)) {
                        modelMonthlyPayments.add(payment);
                    }
                } catch (Exception e) {
                    throw new IllegalValueException("Invalid monthly payment: " + e.getMessage());
                }
            }
        }

        return modelMonthlyPayments;
    }
}
