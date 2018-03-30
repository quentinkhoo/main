package seedu.investigapptor.model.crimecase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.investigapptor.testutil.Assert;

public class DateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Date(null));
    }

    @Test
    public void constructor_invalidStartDate_throwsIllegalArgumentException() {
        String invalidStartDate = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Description(invalidStartDate));
    }

    @Test
    public void isValidStartDate() {
        // null startdate
        Assert.assertThrows(NullPointerException.class, () -> Date.isValidDate(null));

        // invalid startdate
        assertFalse(Date.isValidDate("")); // empty string
        assertFalse(Date.isValidDate(" ")); // spaces only
        assertFalse(Date.isValidDate("12341231")); // random numbers
        assertFalse(Date.isValidDate("12 05/1994")); // invalid format
        assertFalse(Date.isValidDate("30/02/1994")); // invalid date
        assertFalse(Date.isValidDate("A/random/string")); // invalid date format
        assertFalse(Date.isValidDate("some string"));  // invalid date format

        // valid description
        assertTrue(Date.isValidDate("29/02/2016")); // leap year
        assertTrue(Date.isValidDate("31/05/1994")); // random date
        assertTrue(Date.isValidDate("1/12/2001")); // single digit day
        assertTrue(Date.isValidDate("12/1/2001")); // single digit month
        assertTrue(Date.isValidDate("1/1/2001")); // Single digit day and month
    }
}
