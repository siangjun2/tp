package seedu.tutorpal.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import seedu.tutorpal.commons.core.LogsCenter;
import seedu.tutorpal.model.person.Person;

import java.util.logging.Logger;

/**
 * Controller for a help page
 */
public class DisplayWindow extends UiPart<Stage> {

    //public static final String USERGUIDE_URL = "https://ay2526s1-cs2103t-f11-2.github.io/tp/UserGuide.html";
    //public static final String HELP_MESSAGE = "Refer to the user guide: " + USERGUIDE_URL;

    private static final Logger logger = LogsCenter.getLogger(DisplayWindow.class);
    private static final String FXML = "DisplayWindow.fxml";


    @FXML
    private Label helpMessage;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public DisplayWindow(Stage root, String personInfo) {
        super(FXML, root);
        helpMessage.setText(personInfo);
    }

    /**
     * Creates a new HelpWindow.
     */
    public DisplayWindow(String personInfo) {
        this(new Stage(), personInfo);
    }

    /**
     * Shows the help window.
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
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }
}
