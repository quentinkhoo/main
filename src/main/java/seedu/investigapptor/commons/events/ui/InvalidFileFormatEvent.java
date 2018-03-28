package seedu.investigapptor.commons.events.ui;

import seedu.investigapptor.commons.events.BaseEvent;

/**
 * Indicates a request to start with an empty Investigapptor
 */
public class InvalidFileFormatEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
