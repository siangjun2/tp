package seedu.tutorpal.ui;

import java.util.List;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.tutorpal.commons.core.LogsCenter;
import seedu.tutorpal.logic.commands.AddCommand;
import seedu.tutorpal.logic.commands.ClearCommand;
import seedu.tutorpal.logic.commands.Command;
import seedu.tutorpal.logic.commands.DeleteCommand;
import seedu.tutorpal.logic.commands.ExitCommand;
import seedu.tutorpal.logic.commands.FindCommand;
import seedu.tutorpal.logic.commands.ListCommand;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2526s1-cs2103t-f11-2.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "Refer to the user guide: " + USERGUIDE_URL;

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);

        String allCommandDescription = "";

        List<Class<? extends Command>> commands = List.of(
            AddCommand.class,
            ClearCommand.class,
            DeleteCommand.class,
            ExitCommand.class,
            FindCommand.class,
            ListCommand.class
        );

        for (Class<? extends Command> cls : commands) {
            try {
                // Static field access → use getField() and pass null to get()
                String usage = (String) cls.getField("MESSAGE_USAGE_SHORTENED").get(null);
                allCommandDescription += "\n" + usage;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Ignore classes without MESSAGE_USAGE
            }
        }

        helpMessage.setText(HELP_MESSAGE + "\n" + allCommandDescription);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
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

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }
}
