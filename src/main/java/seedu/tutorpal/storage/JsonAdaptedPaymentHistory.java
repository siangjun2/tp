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
class JsonAdaptedPaymentHistory {

    private final String joinDate;
    private final Set<JsonAdaptedMonthlyPayment> monthlyPayments;

    /**
     * Constructs a {@code JsonAdaptedPaymentHistory} with the given payment history details.
     */
    @JsonCreator
    public JsonAdaptedPaymentHistory(@JsonProperty("joinDate") String joinDate,
                                     @JsonProperty("monthlyPayments") Set<JsonAdaptedMonthlyPayment> monthlyPayments) {
        this.joinDate = joinDate;
        this.monthlyPayments = monthlyPayments;
    }

    /**
     * Converts a given {@code PaymentHistory} into this class for Jackson use.
     */
    public JsonAdaptedPaymentHistory(PaymentHistory source) {
        this.joinDate = source.getJoinDate().toString();
        this.monthlyPayments = new HashSet<>();
        for (MonthlyPayment payment : source.getMonthlyPayments()) {
            this.monthlyPayments.add(new JsonAdaptedMonthlyPayment(payment));
        }
    }

    /**
     * Converts this Jackson-friendly adapted payment history object into the model's {@code PaymentHistory} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted payment history.
     */
    public PaymentHistory toModelType() throws IllegalValueException {
        if (joinDate == null) {
            throw new IllegalValueException("Join date field is missing!");
        }

        LocalDate modelJoinDate;
        try {
            modelJoinDate = LocalDate.parse(joinDate);
        } catch (Exception e) {
            throw new IllegalValueException("Invalid join date format");
        }

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

        return new PaymentHistory(modelJoinDate, modelMonthlyPayments);
    }
}
