package seedu.investigapptor.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.investigapptor.logic.commands.CommandTestUtil.DESC_APPLE;
import static seedu.investigapptor.logic.commands.CommandTestUtil.DESC_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_CASENAME_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_INVESTIGATOR_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_STARTDATE_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_TAG_MURDER;

import org.junit.Test;

import seedu.investigapptor.logic.commands.EditCaseCommand.EditCrimeCaseDescriptor;
import seedu.investigapptor.testutil.EditCrimeCaseDescriptorBuilder;

public class EditCrimeCaseDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditCrimeCaseDescriptor descriptorWithSameValues = new EditCrimeCaseDescriptor(DESC_APPLE);
        assertTrue(DESC_APPLE.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_APPLE.equals(DESC_APPLE));

        // null -> returns false
        assertFalse(DESC_APPLE.equals(null));

        // different types -> returns false
        assertFalse(DESC_APPLE.equals(5));

        // different values -> returns false
        assertFalse(DESC_APPLE.equals(DESC_BANANA));

        // different case name -> returns false
        EditCrimeCaseDescriptor editedApple = new EditCrimeCaseDescriptorBuilder(DESC_APPLE)
                .withCaseName(VALID_CASENAME_BANANA).build();
        assertFalse(DESC_APPLE.equals(editedApple));

        // different description -> returns false
        editedApple = new EditCrimeCaseDescriptorBuilder(DESC_APPLE)
                .withDescription(VALID_DESCRIPTION_BANANA).build();
        assertFalse(DESC_APPLE.equals(editedApple));

        // different investigator -> returns false
        editedApple = new EditCrimeCaseDescriptorBuilder(DESC_APPLE)
                .withInvestigator(VALID_INVESTIGATOR_BANANA).build();
        assertFalse(DESC_APPLE.equals(editedApple));

        // different start date -> returns false
        editedApple = new EditCrimeCaseDescriptorBuilder(DESC_APPLE)
                .withStartDate(VALID_STARTDATE_BANANA).build();
        assertFalse(DESC_APPLE.equals(editedApple));

        // different tags -> returns false
        editedApple = new EditCrimeCaseDescriptorBuilder(DESC_APPLE).withTags(VALID_TAG_MURDER).build();
        assertFalse(DESC_APPLE.equals(editedApple));
    }
}
