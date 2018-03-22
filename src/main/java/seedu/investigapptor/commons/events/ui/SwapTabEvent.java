package seedu.investigapptor.commons.events.ui;

import seedu.investigapptor.commons.events.BaseEvent;

/**
 * Represents swapping of tabs
 */
public class SwapTabEvent extends BaseEvent {

    public final int targetIndex;

    public SwapTabEvent(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
