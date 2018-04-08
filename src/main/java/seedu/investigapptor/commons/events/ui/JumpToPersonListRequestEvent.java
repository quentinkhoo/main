package seedu.investigapptor.commons.events.ui;

import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.commons.events.BaseEvent;

/**
 * Indicates a request to jump to the list of persons
 */
public class JumpToPersonListRequestEvent extends BaseEvent {

    public final int targetIndex;

    public JumpToPersonListRequestEvent(Index targetIndex) {
        this.targetIndex = targetIndex.getZeroBased();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
