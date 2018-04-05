package seedu.investigapptor.model.crimecase;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.commons.util.AppUtil.checkArgument;
//@@author leowweiching
/**
 * Represents a CrimeCase's status in the Investigapptor.
 */
public class Status {

    public static final String MESSAGE_STATUS_CONSTRAINTS =
            "Crime case status should not be blank";

    public static final String CASE_CLOSE = "close";
    public static final String CASE_OPEN = "open";

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
        if (this.status.equals(CASE_OPEN)) {
            this.status = CASE_CLOSE;
        } else {
            this.status = CASE_OPEN;
        }
    }

    /**
     * Close the case by updating the case status to close
     */
    public void closeCase() {
        this.status = CASE_CLOSE;
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
