package seedu.investigapptor.model.crimecase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.investigapptor.testutil.Assert;

public class DescriptionTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Description(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidDescription = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Description(invalidDescription));
    }

    @Test
    public void isValidDescription() {
        // null description
        Assert.assertThrows(NullPointerException.class, () -> Description.isValidDescription(null));

        // invalid description
        assertFalse(Description.isValidDescription("")); // empty string
        assertFalse(Description.isValidDescription(" ")); // spaces only
        assertFalse(Description.isValidDescription("^")); // only non-alphanumeric characters
        assertFalse(Description.isValidDescription("peter*")); // contains non acceptable alphanumeric characters

        // valid description
        assertTrue(CaseName.isValidCaseName("jack the ripper")); // alphabets only
        assertTrue(CaseName.isValidCaseName("12345")); // numbers only
        assertTrue(CaseName.isValidCaseName("rape at geylang 8.")); // alphanumeric characters
        assertTrue(CaseName.isValidCaseName("10000 packets of drugs at Geylang")); // with capital letters and dollar sign
        assertTrue(CaseName.isValidCaseName("Jack The Ripper Junior 2nd")); // long names
    }
}
