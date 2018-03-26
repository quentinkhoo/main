package seedu.investigapptor.commons.events.ui;

import seedu.investigapptor.commons.events.BaseEvent;

/**
 * Indicates a request for App termination
 */
public class ValidPasswordEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
