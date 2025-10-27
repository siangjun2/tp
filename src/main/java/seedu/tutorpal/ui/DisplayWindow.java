package seedu.tutorpal.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import seedu.tutorpal.commons.core.LogsCenter;

/**
 * Controller for a display page
 */
public class DisplayWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(DisplayWindow.class);
    private static final String FXML = "DisplayWindow.fxml";

    @FXML
    private Label helpMessage;

    /**
     * Creates a new DisplayWindow.
     *
     * @param root Stage to use as the root of the DisplayWindow.
     */
    public DisplayWindow(Stage root, String personInfo) {
        super(FXML, root);
        helpMessage.setText(personInfo);
    }

    /**
     * Creates a new DisplayWindow.
     */
    public DisplayWindow(String personInfo) {
        this(new Stage(), personInfo);
    }

    /**
     * Shows the Display window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
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
}
