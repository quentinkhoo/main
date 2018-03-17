package seedu.investigapptor.commons.events.ui;

import seedu.investigapptor.commons.events.BaseEvent;
import seedu.investigapptor.ui.InvestigatorCard;

/**
 * Represents a selection change in the Person List Panel
 */
public class InvestigatorPanelSelectionChangedEvent extends BaseEvent {


    private final InvestigatorCard newSelection;

    public InvestigatorPanelSelectionChangedEvent(InvestigatorCard newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public InvestigatorCard getNewSelection() {
        return newSelection;
    }
}
