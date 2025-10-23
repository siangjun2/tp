package seedu.tutorpal.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.tutorpal.commons.exceptions.IllegalValueException;
import seedu.tutorpal.model.person.JoinMonth;

/**
 * Jackson-friendly version of {@link JoinMonth}.
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
     * Converts a given {@code JoinMonth} into this class for Jackson use.
     */
    public JsonAdaptedJoinMonth(JoinMonth source) {
        joinMonth = source.toString();
    }

    @JsonValue
    public String getJoinMonth() {
        return joinMonth;
    }

    /**
     * Converts this Jackson-friendly adapted join month object into the model's
     * {@code JoinMonth} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in
     *                               the adapted join month.
     */
    public JoinMonth toModelType() throws IllegalValueException {
        if (!JoinMonth.isValidJoinMonth(joinMonth)) {
            throw new IllegalValueException(JoinMonth.MESSAGE_CONSTRAINTS);
        }
        return new JoinMonth(joinMonth);
    }
}
