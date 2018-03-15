package seedu.investigapptor.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.person.investigator.Investigator;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class InvestigatorCard extends UiPart<Region> {

    private static final String FXML = "InvestigatorListCard.fxml";
    private static final String[] LABEL_COLOR = {"red", "yellow", "blue", "orange", "pink", "olive", "black"};

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on Investigapptor level 4</a>
     */

    public final Investigator investigator;

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
    private FlowPane crimeCaseList;
    @FXML
    private FlowPane tags;

    public InvestigatorCard(Investigator investigator, int displayedIndex) {
        super(FXML);
        this.investigator = investigator;
        id.setText(displayedIndex + ". ");
        name.setText(investigator.getName().fullName);
        phone.setText(investigator.getPhone().value);
        address.setText(investigator.getAddress().value);
        email.setText(investigator.getEmail().value);
        colorCase(investigator);
        colorTag(investigator);
    }

    /**
     *
     * Creates tag labels for investigator
     */
    private void colorTag(Investigator investigator) {
        investigator.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColorStyle(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }

    /**
     *
     * Creates case labels for investigator
     */
    private void colorCase(Investigator investigator) {
        investigator.getCrimeCases().forEach(CrimeCase -> {
            Label caseLabel = new Label(CrimeCase.getCaseName().crimeCaseName);
            caseLabel.getStyleClass().add(getTagColorStyle(CrimeCase.getCaseName().crimeCaseName));
            crimeCaseList.getChildren().add(caseLabel);
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

    /**
     *
     * @param caseName
     * @return Colour in the array
     */
    private String getCaseColorStyle(String caseName) {
        // Hash the tag name to get the corresponding colour
        return LABEL_COLOR[Math.abs(caseName.hashCode()) % LABEL_COLOR.length];
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof InvestigatorCard)) {
            return false;
        }

        // state check
        InvestigatorCard card = (InvestigatorCard) other;
        return id.getText().equals(card.id.getText())
                && investigator.equals(card.investigator);
    }
}
