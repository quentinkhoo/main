package seedu.investigapptor.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.investigapptor.model.crimecase.CrimeCase;

/**
 * An UI component that displays information of a {@code CaseCard}.
 */
public class CrimeCaseCard extends UiPart<Region> {

    private static final String FXML = "CrimeCaseListCard.fxml";
    private static final String[] LABEL_COLOR = {"red", "yellow", "blue", "orange", "pink", "olive", "black",
        "brown", "gray", "green", "beige", "lightblue", "golden", "purple"};

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on Investigapptor level 4</a>
     */

    public final CrimeCase crimeCase;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label description;
    @FXML
    private Label startDate;
    @FXML
    private Label status;
    @FXML
    private Label currentInvestigator;
    @FXML
    private FlowPane tags;

    public CrimeCaseCard(CrimeCase crimeCase, int displayedIndex) {
        super(FXML);
        this.crimeCase = crimeCase;
        id.setText(displayedIndex + ". ");
        name.setText(crimeCase.getCaseName().toString());
        description.setText(crimeCase.getDescription().toString());
        startDate.setText(crimeCase.getStartDate().date);
        status.setText(crimeCase.getStatus().toString());
        currentInvestigator.setText(crimeCase.getCurrentInvestigator().getName().fullName);
        colorTag(crimeCase);
    }

    /**
     *
     * Creates tag labels for investigator
     */
    private void colorTag(CrimeCase crimeCase) {
        crimeCase.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColorStyle(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }

    /**
     *
     * @param tagName
     * @return Colour in the array
     */
    private String getTagColorStyle(String tagName) {
        // Hash the tag name to get the corresponding colour
        return LABEL_COLOR[Math.abs(tagName.hashCode()) % LABEL_COLOR.length];
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CrimeCaseCard)) {
            return false;
        }

        // state check
        CrimeCaseCard card = (CrimeCaseCard) other;
        return id.getText().equals(card.id.getText())
                && crimeCase.equals(card.crimeCase);
    }
}
