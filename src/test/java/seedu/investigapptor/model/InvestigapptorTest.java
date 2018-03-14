package seedu.investigapptor.model;

import static org.junit.Assert.assertEquals;
import static seedu.investigapptor.testutil.TypicalPersons.ALICE;
import static seedu.investigapptor.testutil.TypicalPersons.AMY;
import static seedu.investigapptor.testutil.TypicalPersons.BOB;
import static seedu.investigapptor.testutil.TypicalPersons.getTypicalInvestigapptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.person.Investigator;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.tag.Tag;
import seedu.investigapptor.testutil.CrimeCaseBuilder;
import seedu.investigapptor.testutil.InvestigapptorBuilder;
import seedu.investigapptor.testutil.InvestigatorBuilder;
import seedu.investigapptor.testutil.PersonBuilder;

public class InvestigapptorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    private final Investigapptor investigapptor = new Investigapptor();



    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), investigapptor.getPersonList());
        assertEquals(Collections.emptyList(), investigapptor.getTagList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        investigapptor.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyInvestigapptor_replacesData() {
        Investigapptor newData = getTypicalInvestigapptor();
        investigapptor.resetData(newData);
        assertEquals(newData, investigapptor);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsAssertionError() {
        // Repeat ALICE twice
        List<Person> newPersons = Arrays.asList(ALICE, ALICE);
        List<Tag> newTags = new ArrayList<>(ALICE.getTags());
        InvestigapptorStub newData = new InvestigapptorStub(newPersons, newTags);

        thrown.expect(AssertionError.class);
        investigapptor.resetData(newData);
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        investigapptor.getPersonList().remove(0);
    }

    @Test
    public void getTagList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        investigapptor.getTagList().remove(0);
    }

    @Test
    public void getPersonList_addInvestigator_addSuccessful() {
        Investigator investigatorAlice = new InvestigatorBuilder().build();
        Investigapptor investigapptor = new InvestigapptorBuilder().withPerson(investigatorAlice)
                .build();
        ObservableList<Person> testList = FXCollections.observableArrayList();
        testList.add(investigatorAlice);
        assertEquals(investigapptor.getPersonList(), testList);
    }

    @Test
    public void getCrimeCase_checkAddAndReturn_addCase() throws Exception {
        Investigator investigatorAlice = new InvestigatorBuilder().build();
        CrimeCase testCase = new CrimeCaseBuilder().build();
        investigatorAlice.addCrimeCase(testCase);
        ObservableList<CrimeCase> testList = FXCollections.observableArrayList();
        testList.add(testCase);
        assertEquals(investigatorAlice.getCrimeCases(), testList);
    }

    @Test
    public void deleteTag_usedByMultiplePersons_tagDeleted() throws Exception {
        Person amyWithFriendTag = new PersonBuilder(AMY).withTags("Friend").build();
        Person bobWithFriendTag = new PersonBuilder(BOB).withTags("Friend").build();

        Investigapptor investigapptor = new InvestigapptorBuilder().withPerson(amyWithFriendTag)
                .withPerson(bobWithFriendTag).build();
        UserPrefs userPrefs = new UserPrefs();

        ModelManager modelManager = new ModelManager(investigapptor, userPrefs);
        modelManager.deleteTag(new Tag("Friend"));

        Person amyNoFriendTag = new PersonBuilder(AMY).withTags().build();
        Person bobNoFriendTag = new PersonBuilder(BOB).withTags().build();

        Investigapptor expectedInvestigapptor = new InvestigapptorBuilder().withPerson(amyNoFriendTag)
                .withPerson(bobNoFriendTag).build();

        assertEquals(new ModelManager(expectedInvestigapptor, userPrefs), modelManager);
    }

    /**
     * A stub ReadOnlyInvestigapptor whose persons and tags lists can violate interface constraints.
     */
    private static class InvestigapptorStub implements ReadOnlyInvestigapptor {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Tag> tags = FXCollections.observableArrayList();

        InvestigapptorStub(Collection<Person> persons, Collection<? extends Tag> tags) {
            this.persons.setAll(persons);
            this.tags.setAll(tags);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Tag> getTagList() {
            return tags;
        }
    }

}
