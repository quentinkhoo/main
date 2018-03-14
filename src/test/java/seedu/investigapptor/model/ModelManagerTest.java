package seedu.investigapptor.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.investigapptor.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.investigapptor.testutil.TypicalPersons.ALICE;
import static seedu.investigapptor.testutil.TypicalPersons.BENSON;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.investigapptor.model.person.NameContainsKeywordsPredicate;
import seedu.investigapptor.testutil.InvestigapptorBuilder;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        ModelManager modelManager = new ModelManager();
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredPersonList().remove(0);
    }

    @Test
    public void equals() {
        Investigapptor investigapptor = new InvestigapptorBuilder().withPerson(ALICE).withPerson(BENSON).build();
        Investigapptor differentInvestigapptor = new Investigapptor();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        ModelManager modelManager = new ModelManager(investigapptor, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(investigapptor, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different investigapptor -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentInvestigapptor, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(investigapptor, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setInvestigapptorName("differentName");
        assertTrue(modelManager.equals(new ModelManager(investigapptor, differentUserPrefs)));
    }
}
