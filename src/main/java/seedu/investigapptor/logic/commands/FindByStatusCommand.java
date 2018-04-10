package seedu.investigapptor.logic.commands;

import java.util.Arrays;
import java.util.List;

import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.events.ui.FilteredCrimeCaseListChangedEvent;
import seedu.investigapptor.commons.events.ui.SwapTabEvent;
import seedu.investigapptor.model.crimecase.StatusContainsKeywordsPredicate;

//@@author pkaijun
/**
 * Finds and lists all cases in investigapptor according to the status predicate specified
 */
public class FindByStatusCommand extends Command {
    private final StatusContainsKeywordsPredicate predicate;

    public FindByStatusCommand(String caseStatus) {
        List<String> keywords = Arrays.asList(caseStatus);
        this.predicate = new StatusContainsKeywordsPredicate(keywords);
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredCrimeCaseList(predicate);

        EventsCenter.getInstance().post(new SwapTabEvent(1));   // List results toggles to case tab
        EventsCenter.getInstance().post(new FilteredCrimeCaseListChangedEvent(model.getFilteredCrimeCaseList()));

        return new CommandResult(getMessageForCrimeListShownSummary(model.getFilteredCrimeCaseList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindByStatusCommand // instanceof handles nulls
                && this.predicate.equals(((FindByStatusCommand) other).predicate)); // state check
    }
}
