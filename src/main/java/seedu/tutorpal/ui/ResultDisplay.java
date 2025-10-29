package seedu.tutorpal.ui;

import static java.util.Objects.requireNonNull;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart<Region> {

    private static final String FXML = "ResultDisplay.fxml";
    private static final double MIN_HEIGHT = 60;
    private static final double MAX_HEIGHT = 200;
    private static final double LINE_HEIGHT = 20; // Approximate height per line

    @FXML
    private TextArea resultDisplay;

    /**
     * While constructing status bar, auto set min/max height.
     */
    public ResultDisplay() {
        super(FXML);

        // Enable text wrapping
        resultDisplay.setWrapText(true);

        // Set initial height constraints
        resultDisplay.setMinHeight(MIN_HEIGHT);
        resultDisplay.setMaxHeight(MAX_HEIGHT);

        // Add listener to adjust height when text changes
        resultDisplay.textProperty().addListener((observable, oldValue, newValue) -> {
            adjustHeight();
        });
    }

    /**
     * Adjusts the height of the result display based on content.
     */
    private void adjustHeight() {
        String text = resultDisplay.getText();

        if (text == null || text.isEmpty()) {
            resultDisplay.setPrefHeight(MIN_HEIGHT);
            if (getRoot() != null) {
                getRoot().setPrefHeight(MIN_HEIGHT);
            }
            return;
        }

        // Count lines (both explicit \n and wrapped lines)
        int lineCount = text.split("\n", -1).length;

        // Calculate height: line count * line height + padding
        double calculatedHeight = lineCount * LINE_HEIGHT + 20;

        // Clamp between MIN and MAX
        double finalHeight = Math.max(MIN_HEIGHT, Math.min(MAX_HEIGHT, calculatedHeight));

        resultDisplay.setPrefHeight(finalHeight);
        if (getRoot() != null) {
            getRoot().setPrefHeight(finalHeight);
        }
    }

    public void setFeedbackToUser(String feedbackToUser) {
        requireNonNull(feedbackToUser);
        resultDisplay.setText(feedbackToUser);
    }
}
