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
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final String[] LABEL_COLOR = {"red", "yellow", "blue", "orange", "pink", "olive", "black",
        "brown", "gray", "green", "beige", "lightblue", "golden", "purple"};

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on Investigapptor level 4</a>
     */

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
    private Label rank;
    @FXML
    private FlowPane tags;

    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        if (person.isInvestigator()) {
            rank.setText(((Investigator) person).getRank().toString());

        } else {
            rank.setText("No Rank");
        }
        colorTag(person);
    }

    //@@author pkaijun
    /**
     *
     * Creates tag labels for person
     */
    private void colorTag(Person person) {
        person.getTags().forEach(tag -> {
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
    //@@author

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && person.equals(card.person);
    }
}
