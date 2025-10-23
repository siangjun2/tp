package seedu.tutorpal.storage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.tutorpal.commons.exceptions.IllegalValueException;
import seedu.tutorpal.model.person.AttendanceHistory;
import seedu.tutorpal.model.person.JoinMonth;
import seedu.tutorpal.model.person.WeeklyAttendance;

/**
 * Jackson-friendly version of {@link AttendanceHistory}.
 */
public class JsonAdaptedAttendanceHistory {

    private final JsonAdaptedJoinMonth joinMonth;
    private final List<JsonAdaptedWeeklyAttendance> weeklyAttendances = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedAttendanceHistory} with the given attendance
     * history details.
     */
    @JsonCreator
    public JsonAdaptedAttendanceHistory(@JsonProperty("joinMonth") JsonAdaptedJoinMonth joinMonth,
            @JsonProperty("weeklyAttendances") List<JsonAdaptedWeeklyAttendance> weeklyAttendances) {
        this.joinMonth = joinMonth;
        if (weeklyAttendances != null) {
            this.weeklyAttendances.addAll(weeklyAttendances);
        }
    }

    /**
     * Converts a given {@code AttendanceHistory} into this class for Jackson use.
     */
    public JsonAdaptedAttendanceHistory(AttendanceHistory source) {
        joinMonth = new JsonAdaptedJoinMonth(source.getJoinMonth());
        source.getWeeklyAttendances().stream()
                .map(JsonAdaptedWeeklyAttendance::new)
                .forEach(weeklyAttendances::add);
    }

    /**
     * Converts this Jackson-friendly adapted attendance history object into the
     * model's {@code AttendanceHistory} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in
     *                               the adapted attendance history.
     */
    public AttendanceHistory toModelType() throws IllegalValueException {
        if (joinMonth == null) {
            throw new IllegalValueException("Join month cannot be null");
        }

        JoinMonth modelJoinMonth = joinMonth.toModelType();
        AttendanceHistory attendanceHistory = new AttendanceHistory(modelJoinMonth);

        for (JsonAdaptedWeeklyAttendance jsonWeeklyAttendance : weeklyAttendances) {
            WeeklyAttendance weeklyAttendance = jsonWeeklyAttendance.toModelType();
            attendanceHistory.markAttendance(weeklyAttendance);
        }

        return attendanceHistory;
    }
}
