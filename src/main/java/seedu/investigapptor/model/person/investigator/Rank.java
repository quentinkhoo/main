package seedu.investigapptor.model.person.investigator;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.commons.util.AppUtil.checkArgument;

//@@author Marcus-cxc
/**
 * Represents an Investigator's rank in the investigapptor book.
 * Guarantees: immutable; is valid as declared in {@link #isValidRank(String)}
 */
public class Rank {


    public static final String MESSAGE_RANK_CONSTRAINTS =
            "Rank can only contain numbers which present their rank as show below\n"
                    + "Constable = 1\n"
                    + "Sergeant = 2\n"
                    + "Inspector = 3\n"
                    + "Detective = 4\n"
                    + "Captain = 5\n";
    public static final String RANK_VALIDATION_REGEX = "\\b[1-5]\\b";
    private final int value;

    /**
     * Constructs a {@code Rank}.
     *
     * @param rank a value representing their rank
     */
    public Rank(String rank) {
        requireNonNull(rank);
        checkArgument(isValidRank(rank), MESSAGE_RANK_CONSTRAINTS);
        this.value = Integer.parseInt(rank);
    }

    /**
     * Returns true if a given string is a valid person phone number.
     */
    public static boolean isValidRank(String test) {
        return test.matches(RANK_VALIDATION_REGEX);
    }
    /**
     * Returns rank's value in string
     * @return
     */
    public String getValue() {
        return String.valueOf(value);
    }
    @Override
    public String toString() {
        switch (value) {
        case 1: return "Constable\n";
        case 2: return "Sergeant\n";
        case 3: return "Inspector\n";
        case 4: return "Detective\n";
        case 5: return "Captain\n";
        default: return "Error\n";
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Rank // instanceof handles nulls
                && this.value == ((Rank) other).value); // state check
    }

    @Override
    public int hashCode() {
        return value;
    }

}
