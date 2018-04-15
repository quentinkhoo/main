package seedu.investigapptor.model.crimecase;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.commons.util.AppUtil.checkArgument;
//@@author leowweiching
/**
 * Represents a Crime Case's description in the Investigator Application.
 * Guarantees: immutable; is valid as declared in {@link #isValidDescription(String)}
 */
public class Description {

    public static final String MESSAGE_DESCRIPTION_CONSTRAINTS =
            "Crime case descriptions can take any values, and it should not be blank";

    public static final String CASE_NAME_VALIDATION_REGEX = "[\\p{Graph}][\\p{Graph}]*";

    public final String description;

    /**
     * Constructs a {@code Description}.
     *
     * @param description A valid description.
     */
    public Description(String description) {
        requireNonNull(description);
        checkArgument(isValidDescription(description), MESSAGE_DESCRIPTION_CONSTRAINTS);
        this.description = description;
    }

    /**
     * Returns true if a given string is a valid case description.
     */
    public static boolean isValidDescription(String test) {
        return test.trim() != null && !test.trim().isEmpty();
    }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Description // instanceof handles nulls
                && this.description.equals(((Description) other).description)); // state check
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }
}
