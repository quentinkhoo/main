package seedu.investigapptor.logic.commands;

import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.events.ui.SwapTabEvent;
import seedu.investigapptor.model.crimecase.CaseNameContainsKeywordsPredicate;
//@@author leowweiching
/**
 * Finds and lists all cases in investigapptor book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCaseCommand extends Command {

    public static final String COMMAND_WORD = "findcases";
    public static final String COMMAND_ALIAS = "fc";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all cases whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alpha bravo charlie";

    private final CaseNameContainsKeywordsPredicate predicate;

    public FindCaseCommand(CaseNameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredCrimeCaseList(predicate);
        EventsCenter.getInstance().post(new SwapTabEvent(1));
        return new CommandResult(getMessageForCrimeCaseListShownSummary(model.getFilteredCrimeCaseList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCaseCommand // instanceof handles nulls
                && this.predicate.equals(((FindCaseCommand) other).predicate)); // state check
    }
}
