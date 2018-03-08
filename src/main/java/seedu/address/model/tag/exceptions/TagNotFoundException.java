package seedu.address.model.tag.exceptions;

import seedu.address.commons.exceptions.DuplicateDataException;

/**
 * Signals that the operation will result in duplicate Person objects.
 */
public class TagNotFoundException extends DuplicateDataException {
    public TagNotFoundException() {
        super("Tag does not exist");
    }
}
