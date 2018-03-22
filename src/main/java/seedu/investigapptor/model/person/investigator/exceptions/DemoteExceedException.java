package seedu.investigapptor.model.person.investigator.exceptions;

/**
 * Signals that the operation is unable to find the specified person.
 */
public class DemoteExceedException extends Exception {
    public DemoteExceedException() {
        super("Unable to demote investigator any lower");
    }
}
