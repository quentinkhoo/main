package seedu.investigapptor.model.crimecase.exceptions;

import seedu.investigapptor.commons.exceptions.DuplicateDataException;

/**
 * Signals that the operation will result in duplicate CrimeCase objects.
 */
public class DuplicateCrimeCaseException extends DuplicateDataException {
    public DuplicateCrimeCaseException() {
        super("Operation would result in duplicate cases");
    }
}
