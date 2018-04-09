package seedu.investigapptor.commons.events.ui;

import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.commons.events.BaseEvent;

/**
 * Indicates a request to jump to the list of persons
 */
public class JumpToCrimeCaseListRequestEvent extends BaseEvent {

    public final int targetIndex;

    public JumpToCrimeCaseListRequestEvent(Index targetIndex) {
        this.targetIndex = targetIndex.getZeroBased();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
