package seedu.investigapptor.commons.events.model;

import seedu.investigapptor.commons.events.BaseEvent;
import seedu.investigapptor.model.ReadOnlyInvestigapptor;
//@@author Marcus-cxc
/** Indicates the Investigapptor in the model has changed*/
public class InvestigapptorBackupEvent extends BaseEvent {

    public final ReadOnlyInvestigapptor data;
    public final String fileName;

    public InvestigapptorBackupEvent(ReadOnlyInvestigapptor data, String fileName) {
        this.data = data;
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "number of persons " + data.getPersonList().size() + ", number of tags " + data.getTagList().size();
    }
}
