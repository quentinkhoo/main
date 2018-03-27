package seedu.investigapptor.commons.events.ui;

import seedu.investigapptor.commons.events.BaseEvent;

public class InvalidFileFormatEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
