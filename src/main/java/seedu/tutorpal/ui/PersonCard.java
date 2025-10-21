package seedu.tutorpal.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.tutorpal.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    private static final String COLOR_STUDENT = "#f48771"; // Orange-red
    private static final String COLOR_TUTOR = "#4fc3f7"; // Light blue
    private static final String COLOR_CLASS = "#4ec9b0"; // Cyan-green
    private static final String COLOR_PAID = "#6a9955"; // Green
    private static final String COLOR_UNPAID = "#ce9178"; // Orange
    private static final String COLOR_OVERDUE = "#f48771"; // Red
    private static final String COLOR_MARKED = "#6a9955"; // Green (attended)
    private static final String COLOR_UNMARKED = "#858585"; // Gray (not attended)
    private static final String COLOR_TAG = "#cccccc"; // Light gray
    private static final String COLOR_TEXT = "#cccccc"; // Default text
    private static final String COLOR_ID = "#858585"; // Dim gray

    public final Person person;

    @FXML
    private HBox cardPane;
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
    @FXML
    private FlowPane tags;

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

        // Set name - prevent text overflow
        name.setText(person.getName().fullName);
        name.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 13px; "
                + "-fx-font-weight: bold; -fx-text-fill: #ffffff;");

        // Phone
        phone.setText(person.getPhone().value);
        phone.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 11px; "
                + "-fx-text-fill: " + COLOR_TEXT + ";");

        // Email
        email.setText(person.getEmail().value);
        email.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 11px; "
                + "-fx-text-fill: " + COLOR_TEXT + ";");

        // Address
        address.setText(person.getAddress().value);
        address.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 11px; "
                + "-fx-text-fill: " + COLOR_TEXT + ";");

        // Role
        String roleValue = person.getRole().value;
        role.setText(roleValue.toUpperCase());

        if ("tutor".equalsIgnoreCase(roleValue)) {
            role.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 11px; "
                    + "-fx-font-weight: bold; -fx-text-fill: " + COLOR_TUTOR + ";");
        } else if ("student".equalsIgnoreCase(roleValue)) {
            role.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 11px; "
                    + "-fx-font-weight: bold; -fx-text-fill: " + COLOR_STUDENT + ";");
        }

        // Classes
        person.getClasses().stream()
                .sorted(Comparator.comparing(course -> course.value))
                .forEach(course -> {
                    Label classLabel = new Label(course.value);
                    classLabel.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 10px; "
                            + "-fx-text-fill: " + COLOR_CLASS + ";");
                    classes.getChildren().add(classLabel);
                });

        // Attendance status
        if (person.isMarked()) {
            attendance.setText("✓");
            attendance.setStyle("-fx-font-family: 'Segoe UI Symbol'; -fx-font-size: 16px; "
                    + "-fx-font-weight: bold; -fx-text-fill: " + COLOR_MARKED + ";");
        } else {
            attendance.setText("○");
            attendance.setStyle("-fx-font-family: 'Segoe UI Symbol'; -fx-font-size: 14px; "
                    + "-fx-text-fill: " + COLOR_UNMARKED + ";");
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

        // Tags
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> {
                    Label tagLabel = new Label(tag.tagName);
                    tagLabel.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 9px; "
                            + "-fx-text-fill: " + COLOR_TAG + "; -fx-background-color: #3c3c3c; "
                            + "-fx-padding: 1 4 1 4; -fx-background-radius: 2;");
                    tags.getChildren().add(tagLabel);
                });
    }
}
