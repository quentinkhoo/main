package seedu.investigapptor.ui;

import static org.junit.Assert.assertEquals;
import static seedu.investigapptor.testutil.EventsUtil.postNow;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.investigapptor.testutil.TypicalPersons.getTypicalPersons;
import static seedu.investigapptor.ui.testutil.GuiTestAssert.assertCardDisplaysPerson;
import static seedu.investigapptor.ui.testutil.GuiTestAssert.assertCardEquals;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.PersonCardHandle;
import guitests.guihandles.PersonListPanelHandle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.investigapptor.commons.events.ui.JumpToPersonListRequestEvent;
import seedu.investigapptor.model.person.Person;

public class PersonListPanelTest extends GuiUnitTest {
    private static final ObservableList<Person> TYPICAL_PERSONS =
            FXCollections.observableList(getTypicalPersons());

    private static final JumpToPersonListRequestEvent JUMP_TO_SECOND_EVENT =
            new JumpToPersonListRequestEvent(INDEX_SECOND_PERSON);

    private PersonListPanelHandle personListPanelHandle;

    @Before
    public void setUp() {
        PersonListPanel personListPanel = new PersonListPanel(TYPICAL_PERSONS);
        uiPartRule.setUiPart(personListPanel);

        personListPanelHandle = new PersonListPanelHandle(getChildNode(personListPanel.getRoot(),
                PersonListPanelHandle.PERSON_LIST_VIEW_ID));
    }

    @Test
    public void display() {
        for (int i = 0; i < TYPICAL_PERSONS.size(); i++) {
            personListPanelHandle.navigateToCard(TYPICAL_PERSONS.get(i));
            Person expectedPerson = TYPICAL_PERSONS.get(i);
            PersonCardHandle actualCard = personListPanelHandle.getPersonCardHandle(i);

            assertCardDisplaysPerson(expectedPerson, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void handleJumpToListRequestEvent() {
        postNow(JUMP_TO_SECOND_EVENT);
        guiRobot.pauseForHuman();

        PersonCardHandle expectedCard = personListPanelHandle.getPersonCardHandle(INDEX_SECOND_PERSON.getZeroBased());
        PersonCardHandle selectedCard = personListPanelHandle.getHandleToSelectedCard();
        assertCardEquals(expectedCard, selectedCard);
    }
}
