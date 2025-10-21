package seedu.tutorpal.storage;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.tutorpal.commons.exceptions.IllegalValueException;
import seedu.tutorpal.model.person.PaymentHistory;

/**
 * Jackson-friendly version of {@link PaymentHistory}.
 */
class JsonAdaptedPaymentHistory {

    private final String joinDate;
    private final Map<String, Boolean> monthlyPayments;

    /**
     * Constructs a {@code JsonAdaptedPaymentHistory} with the given payment history details.
     */
    @JsonCreator
    public JsonAdaptedPaymentHistory(@JsonProperty("joinDate") String joinDate,
                                     @JsonProperty("monthlyPayments") Map<String, Boolean> monthlyPayments) {
        this.joinDate = joinDate;
        this.monthlyPayments = monthlyPayments;
    }

    /**
     * Converts a given {@code PaymentHistory} into this class for Jackson use.
     */
    public JsonAdaptedPaymentHistory(PaymentHistory source) {
        this.joinDate = source.getJoinDate().toString();
        this.monthlyPayments = new HashMap<>();
        for (Map.Entry<YearMonth, Boolean> entry : source.getMonthlyPayments().entrySet()) {
            this.monthlyPayments.put(entry.getKey().toString(), entry.getValue());
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

        Map<YearMonth, Boolean> modelMonthlyPayments = new HashMap<>();
        if (monthlyPayments != null) {
            for (Map.Entry<String, Boolean> entry : monthlyPayments.entrySet()) {
                try {
                    YearMonth month = YearMonth.parse(entry.getKey());
                    modelMonthlyPayments.put(month, entry.getValue());
                } catch (Exception e) {
                    throw new IllegalValueException("Invalid month format: " + entry.getKey());
                }
            }
        }

        return new PaymentHistory(modelJoinDate, modelMonthlyPayments);
    }
}
