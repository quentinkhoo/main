package seedu.investigapptor.model.person.investigator.exceptions;

/**
 * Signals that the operation is unable to find the specified person.
 */
public class PromoteExceedException extends Exception {
    public PromoteExceedException() {
        super("Unable to promote investigator any higher");
    }
}
