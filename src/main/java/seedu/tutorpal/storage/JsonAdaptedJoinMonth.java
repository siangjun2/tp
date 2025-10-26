package seedu.tutorpal.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.tutorpal.commons.exceptions.IllegalValueException;
import seedu.tutorpal.model.person.JoinDate;

/**
 * Jackson-friendly version of {@link JoinDate}.
 * Note: This class is kept for backward compatibility with old JSON files that used JoinMonth.
 * It converts month-based dates to JoinDate format.
 */
public class JsonAdaptedJoinMonth {

    private final String joinMonth;

    /**
     * Constructs a {@code JsonAdaptedJoinMonth} with the given join month string.
     */
    @JsonCreator
    public JsonAdaptedJoinMonth(String joinMonth) {
        this.joinMonth = joinMonth;
    }

    /**
     * Converts a given {@code JoinDate} into this class for Jackson use.
     */
    public JsonAdaptedJoinMonth(JoinDate source) {
        joinMonth = source.toString();
    }

    @JsonValue
    public String getJoinMonth() {
        return joinMonth;
    }

    /**
     * Converts this Jackson-friendly adapted join month object into the model's
     * {@code JoinDate} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in
     *                               the adapted join month.
     */
    public JoinDate toModelType() throws IllegalValueException {
        if (!JoinDate.isValidJoinDate(joinMonth)) {
            throw new IllegalValueException(JoinDate.MESSAGE_CONSTRAINTS);
        }
        return new JoinDate(joinMonth);
    }
}
