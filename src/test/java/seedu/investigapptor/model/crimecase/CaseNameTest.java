package seedu.investigapptor.model.crimecase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.investigapptor.testutil.Assert;

public class CaseNameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new CaseName(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidCaseName = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new CaseName(invalidCaseName));
    }

    @Test
    public void isValidCaseName() {
        // null name
        Assert.assertThrows(NullPointerException.class, () -> CaseName.isValidCaseName(null));

        // invalid name
        assertFalse(CaseName.isValidCaseName("")); // empty string
        assertFalse(CaseName.isValidCaseName(" ")); // spaces only
        assertFalse(CaseName.isValidCaseName("^")); // only non-alphanumeric characters
        assertFalse(CaseName.isValidCaseName("peter*")); // contains non-alphanumeric characters

        // valid name
        assertTrue(CaseName.isValidCaseName("peter jack")); // alphabets only
        assertTrue(CaseName.isValidCaseName("12345")); // numbers only
        assertTrue(CaseName.isValidCaseName("peter the 2nd")); // alphanumeric characters
        assertTrue(CaseName.isValidCaseName("Capital Tan")); // with capital letters
        assertTrue(CaseName.isValidCaseName("David Roger Jackson Ray Jr 2nd")); // long names
    }
}
