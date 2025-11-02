package seedu.tutorpal.ui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import seedu.tutorpal.commons.core.LogsCenter;

/**
 * Controller for a display page
 */
public class DisplayWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(DisplayWindow.class);
    private static final String FXML = "DisplayWindow.fxml";

    @FXML
    private TextFlow messageFlow;

    /**
     * Creates a new DisplayWindow.
     *
     * @param root Stage to use as the root of the DisplayWindow.
     */
    public DisplayWindow(Stage root, String personInfo) {
        super(FXML, root);
        setRichMessage(personInfo);
    }

    /**
     * Creates a new DisplayWindow.
     */
    public DisplayWindow(String personInfo) {
        this(new Stage(), personInfo);
    }

    /**
     * Builds coloured text into the TextFlow.
     */
    private void setRichMessage(String message) {
        assert message != null : "Message should not be null";
        Set<String> greenWords = new HashSet<>(Arrays.asList("paid", "present"));
        Set<String> redWords = new HashSet<>(Arrays.asList("unpaid", "absent", "overdue"));
        Set<String> blueWords = new HashSet<>(Arrays.asList("student", "tutor"));
        Set<String> yellowWords = new HashSet<>(Arrays.asList("pending"));
        messageFlow.getChildren().clear();
        String[] parts = message.split("(?<=\\b)|(?=\\b)");
        for (String part : parts) {
            if (part == null || part.isEmpty()) {
                continue;
            }
            Text t = new Text(part);
            t.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 11pt; -fx-fill: #cccccc;");

            String lower = part.toLowerCase().trim();

            if (greenWords.contains(lower)) {
                t.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 11pt; "
                        + "-fx-fill: #6a9955; -fx-font-weight: bold;");
            } else if (redWords.contains(lower)) {
                t.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 11pt; "
                        + "-fx-fill: #f48771; -fx-font-weight: bold;");
            } else if (blueWords.contains(lower)) {
                t.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 11pt; "
                        + "-fx-fill: #4fc3f7; -fx-font-weight: bold;");
            } else if (yellowWords.contains(lower)) {
                t.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 11pt; "
                        + "-fx-fill: #dcdcaa; -fx-font-weight: bold;");
            }

            messageFlow.getChildren().add(t);
        }
    }

    /**
     * Shows the window.
     */
    public void show() {
        logger.fine("Showing display page about the selected contact.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the display window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the display window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the display window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Handles the close button action.
     */
    @FXML
    private void handleClose() {
        hide();
    }
}
