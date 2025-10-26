package seedu.tutorpal.storage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.tutorpal.commons.exceptions.IllegalValueException;
import seedu.tutorpal.model.person.AttendanceHistory;
import seedu.tutorpal.model.person.JoinDate;
import seedu.tutorpal.model.person.WeeklyAttendance;

/**
 * Jackson-friendly version of {@link AttendanceHistory}.
 */
public class JsonAdaptedAttendanceHistory {

    private final String joinDate;
    private final List<JsonAdaptedWeeklyAttendance> weeklyAttendances = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedAttendanceHistory} with the given attendance
     * history details.
     */
    @JsonCreator
    public JsonAdaptedAttendanceHistory(@JsonProperty("joinDate") String joinDate,
            @JsonProperty("weeklyAttendances") List<JsonAdaptedWeeklyAttendance> weeklyAttendances) {
        this.joinDate = joinDate;
        if (weeklyAttendances != null) {
            this.weeklyAttendances.addAll(weeklyAttendances);
        }
    }

    /**
     * Converts a given {@code AttendanceHistory} into this class for Jackson use.
     */
    public JsonAdaptedAttendanceHistory(AttendanceHistory source) {
        joinDate = source.getJoinDate().toString();
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
        if (joinDate == null) {
            throw new IllegalValueException("Join date cannot be null");
        }

        if (!JoinDate.isValidJoinDate(joinDate)) {
            throw new IllegalValueException(JoinDate.MESSAGE_CONSTRAINTS);
        }

        JoinDate modelJoinDate = new JoinDate(joinDate);
        AttendanceHistory attendanceHistory = new AttendanceHistory(modelJoinDate);

        for (JsonAdaptedWeeklyAttendance jsonWeeklyAttendance : weeklyAttendances) {
            WeeklyAttendance weeklyAttendance = jsonWeeklyAttendance.toModelType();
            attendanceHistory = attendanceHistory.markAttendance(weeklyAttendance);
        }

        return attendanceHistory;
    }
}
