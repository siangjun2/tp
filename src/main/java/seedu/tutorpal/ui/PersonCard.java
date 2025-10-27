package seedu.tutorpal.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import seedu.tutorpal.model.person.AttendanceHistory;
import seedu.tutorpal.model.person.Person;
import seedu.tutorpal.model.person.Role;

/**
 * A UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    private static final String COLOR_STUDENT = "#f48771"; // Orange-red
    private static final String COLOR_TUTOR = "#4fc3f7"; // Light blue
    private static final String COLOR_CLASS = "#4ec9b0"; // Cyan-green
    private static final String COLOR_PAID = "#6a9955"; // Green
    private static final String COLOR_UNPAID = "#ce9178"; // Orange
    private static final String COLOR_OVERDUE = "#f48771"; // Red
    private static final String COLOR_ATTENDANCE_HIGH = "#6a9955"; // Green (>=8 weeks)
    private static final String COLOR_ATTENDANCE_MEDIUM = "#dcdcaa"; // Yellow (5-7 weeks)
    private static final String COLOR_ATTENDANCE_LOW = "#f48771"; // Red (<5 weeks)
    private static final String COLOR_TEXT = "#cccccc"; // Default text
    private static final String COLOR_ID = "#858585"; // Dim gray

    public final Person person;

    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label role;
    @FXML
    private FlowPane classes;
    @FXML
    private Label paymentStatus;
    @FXML
    private Label attendance;

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;

        // Set ID with monospace font
        id.setText(String.format("%05d", displayedIndex));
        id.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 9px; -fx-text-fill: "
                + COLOR_ID + ";");

        // Set name
        name.setText(person.getName().fullName);
        name.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 13px; "
                + "-fx-font-weight: bold; -fx-text-fill: #ffffff;");

        // Phone
        phone.setText(person.getPhone().value);
        phone.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 10px; "
                + "-fx-text-fill: " + COLOR_TEXT + ";");

        // Email
        email.setText(person.getEmail().value);
        email.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 10px; "
                + "-fx-text-fill: " + COLOR_TEXT + ";");

        // Address
        address.setText(person.getAddress().value);
        address.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 10px; "
                + "-fx-text-fill: " + COLOR_TEXT + ";");

        // Role
        String roleValue = person.getRole().toString();
        role.setText(roleValue.toUpperCase());

        if ("tutor".equalsIgnoreCase(roleValue)) {
            role.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 11px; "
                    + "-fx-font-weight: bold; -fx-text-fill: " + COLOR_TUTOR + ";");
        } else if ("student".equalsIgnoreCase(roleValue)) {
            role.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 11px; "
                    + "-fx-font-weight: bold; -fx-text-fill: " + COLOR_STUDENT + ";");
        }

        // Classes with background
        person.getClasses().stream()
                .sorted(Comparator.comparing(course -> course.value))
                .forEach(course -> {
                    Label classLabel = new Label(course.value);
                    classLabel.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 10px; "
                            + "-fx-text-fill: " + COLOR_CLASS + "; "
                            + "-fx-background-color: #2d2d30; "
                            + "-fx-padding: 2 6 2 6; "
                            + "-fx-border-radius: 2; "
                            + "-fx-background-radius: 2;");
                    classes.getChildren().add(classLabel);
                });

        // Attendance - show weeks attended for students, N/A for tutors
        if (person.getRole() == Role.STUDENT) {
            AttendanceHistory attendanceHistory = person.getAttendanceHistory();
            if (attendanceHistory != null) {
                int attendedWeeks = attendanceHistory.getWeeklyAttendances().size();
                attendance.setText(attendedWeeks + " wks");

                // Color code based on attendance count
                String attendanceColor;
                if (attendedWeeks >= 8) {
                    attendanceColor = COLOR_ATTENDANCE_HIGH;
                } else if (attendedWeeks >= 5) {
                    attendanceColor = COLOR_ATTENDANCE_MEDIUM;
                } else {
                    attendanceColor = COLOR_ATTENDANCE_LOW;
                }

                attendance.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 10px; "
                        + "-fx-font-weight: bold; -fx-text-fill: " + attendanceColor + ";");
            } else {
                attendance.setText("0 wks");
                attendance.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 10px; "
                        + "-fx-font-weight: bold; -fx-text-fill: " + COLOR_ATTENDANCE_LOW + ";");
            }
        } else {
            // Tutor - show N/A
            attendance.setText("N/A");
            attendance.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 10px; "
                    + "-fx-text-fill: " + COLOR_TEXT + ";");
        }

        // Payment status
        String paymentValue = person.getPaymentStatus().toString();
        paymentStatus.setText(paymentValue.toUpperCase());

        if ("paid".equalsIgnoreCase(paymentValue)) {
            paymentStatus.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 10px; "
                    + "-fx-font-weight: bold; -fx-text-fill: " + COLOR_PAID + ";");
        } else if ("overdue".equalsIgnoreCase(paymentValue)) {
            paymentStatus.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 10px; "
                    + "-fx-font-weight: bold; -fx-text-fill: " + COLOR_OVERDUE + ";");
        } else { // unpaid
            paymentStatus.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 10px; "
                    + "-fx-font-weight: bold; -fx-text-fill: " + COLOR_UNPAID + ";");
        }
    }
}
