package seedu.investigapptor.model.crimecase;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.commons.util.AppUtil.checkArgument;

/**
 * Represents a CrimeCase's name in the Investigapptor.
 * Guarantees: immutable; is valid as declared in {@link #isValidCaseName(String)}
 */
public class CaseName {

    public static final String MESSAGE_CASE_NAME_CONSTRAINTS =
            "Crime case names should be alphanumeric, and not be blank";

    public static final String CASE_NAME_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String crimeCaseName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public CaseName(String name) {
        requireNonNull(name);
        checkArgument(isValidCaseName(name), MESSAGE_CASE_NAME_CONSTRAINTS);
        this.crimeCaseName = name;
    }

    /**
     * Returns true if a given string is a valid person name.
     */
    public static boolean isValidCaseName(String test) {
        return test.matches(CASE_NAME_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return crimeCaseName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CaseName // instanceof handles nulls
                && this.crimeCaseName.equals(((CaseName) other).crimeCaseName)); // state check
    }

    @Override
    public int hashCode() {
        return crimeCaseName.hashCode();
    }
}
