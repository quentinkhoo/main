package seedu.investigapptor.commons.events.ui;

import seedu.investigapptor.commons.events.BaseEvent;

//@@author quentinkhoo
/**
 * Indicates a request for Starting of Investigapptor
 */
public class ValidPasswordEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
