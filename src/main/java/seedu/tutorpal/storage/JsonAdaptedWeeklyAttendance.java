package seedu.tutorpal.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.tutorpal.commons.exceptions.IllegalValueException;
import seedu.tutorpal.model.person.WeeklyAttendance;

/**
 * Jackson-friendly version of {@link WeeklyAttendance}.
 */
public class JsonAdaptedWeeklyAttendance {

    private final String weeklyAttendance;

    /**
     * Constructs a {@code JsonAdaptedWeeklyAttendance} with the given weekly
     * attendance string.
     */
    @JsonCreator
    public JsonAdaptedWeeklyAttendance(String weeklyAttendance) {
        this.weeklyAttendance = weeklyAttendance;
    }

    /**
     * Converts a given {@code WeeklyAttendance} into this class for Jackson use.
     */
    public JsonAdaptedWeeklyAttendance(WeeklyAttendance source) {
        weeklyAttendance = source.toString();
    }

    @JsonValue
    public String getWeeklyAttendance() {
        return weeklyAttendance;
    }

    /**
     * Converts this Jackson-friendly adapted weekly attendance object into the
     * model's {@code WeeklyAttendance} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in
     *                               the adapted weekly attendance.
     */
    public WeeklyAttendance toModelType() throws IllegalValueException {
        if (!WeeklyAttendance.isValidWeeklyAttendance(weeklyAttendance)) {
            throw new IllegalValueException(WeeklyAttendance.MESSAGE_CONSTRAINTS);
        }
        return new WeeklyAttendance(weeklyAttendance);
    }
}
