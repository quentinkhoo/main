package seedu.investigapptor.commons.events.ui;

import javafx.collections.ObservableList;
import seedu.investigapptor.commons.events.BaseEvent;
import seedu.investigapptor.model.crimecase.CrimeCase;

//@@author pkaijun
/**
 * Indicates a request to change the cases displayed on the calendar panel
 */
public class FilteredCrimeCaseListChangedEvent extends BaseEvent {

    public final ObservableList<CrimeCase> crimecases;

    public FilteredCrimeCaseListChangedEvent(ObservableList<CrimeCase> crimecases) {
        this.crimecases = crimecases;
    }

    public ObservableList<CrimeCase> getFilteredCrimeCaseList() {
        return crimecases;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
