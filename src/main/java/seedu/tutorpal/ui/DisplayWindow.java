package seedu.tutorpal.ui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;
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

    /** Builds coloured text into the TextFlow.
     *  Example colouring rules: "paid" -> green, "overdue" -> red
     *  You can adjust the sets or make this accept parameters.
     */
    private void setRichMessage(String message) {
        // Example vocabulary to colour
        Set<String> greenWords = new HashSet<>(Arrays.asList("paid", "present"));
        Set<String> redWords   = new HashSet<>(Arrays.asList("unpaid", "absent"));

        messageFlow.getChildren().clear();

        // Split but keep delimiters so punctuation/spaces don’t disappear
        // This splits on word boundaries, preserving non-word chunks.
        String[] parts = message.split("(?<=\\b)|(?=\\b)");

        for (String part : parts) {
            Text t = new Text(part);

            String lower = part.toLowerCase();
            if (greenWords.contains(lower)) {
                t.setStyle("-fx-fill: rgb(57,255,20);");
                // Optional: t.setStyle("-fx-font-weight: bold;");
            } else if (redWords.contains(lower)) {
                t.setStyle("-fx-fill: red;");
                // Optional: t.setStyle("-fx-font-weight: bold;");
            } else {
                // default colour inherited from CSS / theme
            }
            messageFlow.getChildren().add(t);
        }
    }

    /** Shows the window. */
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
}
