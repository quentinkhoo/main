package seedu.investigapptor.model.crimecase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.investigapptor.testutil.Assert;

public class StartDateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new StartDate(null));
    }

    @Test
    public void constructor_invalidStartDate_throwsIllegalArgumentException() {
        String invalidStartDate = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Description(invalidStartDate));
    }

    @Test
    public void isValidStartDate() {
        // null startdate
        Assert.assertThrows(NullPointerException.class, () -> StartDate.isValidDate(null));

        // invalid startdate
        assertFalse(StartDate.isValidDate("")); // empty string
        assertFalse(StartDate.isValidDate(" ")); // spaces only
        assertFalse(StartDate.isValidDate("12341231")); // random numbers
        assertFalse(StartDate.isValidDate("12 05/1994")); // invalid format
        assertFalse(StartDate.isValidDate("30/02/1994")); // invalid date
        assertFalse(StartDate.isValidDate("A/random/string")); // invalid date format
        assertFalse(StartDate.isValidDate("some string"));  // invalid date format

        // valid description
        assertTrue(StartDate.isValidDate("29/02/2016")); // leap year
        assertTrue(StartDate.isValidDate("31/05/1994")); // random date
        assertTrue(StartDate.isValidDate("1/12/2001")); // single digit day
        assertTrue(StartDate.isValidDate("12/1/2001")); // single digit month
        assertTrue(StartDate.isValidDate("1/1/2001")); // Single digit day and month
    }
}
