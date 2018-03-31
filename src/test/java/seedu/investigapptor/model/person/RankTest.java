package seedu.investigapptor.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.investigapptor.model.person.investigator.Rank;
import seedu.investigapptor.testutil.Assert;

public class RankTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Rank(null));
    }

    @Test
    public void constructor_invalidRank_throwsIllegalArgumentException() {
        String invalidRank = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Rank(invalidRank));
    }

    @Test
    public void isValidRank() {
        // null rank
        Assert.assertThrows(NullPointerException.class, () -> Rank.isValidRank(null));

        // blank rank
        assertFalse(Rank.isValidRank("")); // empty string
        assertFalse(Rank.isValidRank(" ")); // spaces only

        // invalid parts
        assertFalse(Rank.isValidRank("-5")); // negative value
        assertFalse(Rank.isValidRank("6")); // greater than 5
        assertFalse(Rank.isValidRank("0")); // less than 1
        // valid rank
        assertTrue(Rank.isValidRank("1"));
        assertTrue(Rank.isValidRank("2"));
        assertTrue(Rank.isValidRank("3"));
        assertTrue(Rank.isValidRank("4"));
        assertTrue(Rank.isValidRank("5"));

    }
}
