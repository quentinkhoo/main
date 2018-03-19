package seedu.investigapptor.commons.events.ui;

import seedu.investigapptor.commons.events.BaseEvent;
import seedu.investigapptor.ui.CrimeCaseCard;
import seedu.investigapptor.ui.PersonCard;

/**
 * Represents a selection change in the Person List Panel
 */
public class CrimeCasePanelSelectionChangedEvent extends BaseEvent {


    private final CrimeCaseCard newSelection;

    public CrimeCasePanelSelectionChangedEvent(CrimeCaseCard newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public CrimeCaseCard getNewSelection() {
        return newSelection;
    }
}
