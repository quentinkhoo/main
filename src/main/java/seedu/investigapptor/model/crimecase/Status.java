package seedu.investigapptor.model.crimecase;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.commons.util.AppUtil.checkArgument;

/**
 * Represents a CrimeCase's status in the Investigapptor.
 */
public class Status {

    public static final String MESSAGE_STATUS_CONSTRAINTS =
            "Crime case status should not be blank";

    private String status;

    /**
     * Constructs a {@code Status}.
     *
     */
    public Status() {
        this.status = "open";
    }

    /**
     * Constructs a {@code Status}.
     *
     * @param status A valid status.
     */
    public Status(String status) {
        requireNonNull(status);
        checkArgument(isValidStatus(status), MESSAGE_STATUS_CONSTRAINTS);
        this.status = status;
    }

    /**
     * Toggles status depending on current status
     *
     */
    public void toggleCase() {
        if (this.status.equals("open")) {
            this.status = "close";
        } else {
            this.status = "open";
        }
    }

    /**
     * Returns true if a given string is a valid case status.
     */
    public static boolean isValidStatus(String test) {
        return !test.trim().isEmpty() && (test.trim().equals("open") || test.trim().equals("close"));
    }

    @Override
    public String toString() {
        return this.status;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Status // instanceof handles nulls
                && this.status.equals(((Status) other).status)); // state check
    }

    @Override
    public int hashCode() {
        return status.hashCode();
    }
}
