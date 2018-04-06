package seedu.investigapptor.logic.commands;

import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.events.ui.SwapTabEvent;
import seedu.investigapptor.model.crimecase.TagContainsKeywordsPredicate;

//@@author pkaijun
/**
 * Finds and lists all investigators in investigapptor whose tags contains any of the argument keywords.
 * Keyword matching is not case-sensitive.
 */
public class FindCaseTagsCommand extends Command {

    public static final String COMMAND_WORD = "findcasetags";
    public static final String COMMAND_ALIAS = "fct";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds cases whose tags contain any of "
            + "the specified keywords and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " Homicide";

    private final TagContainsKeywordsPredicate predicate;

    public FindCaseTagsCommand(TagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredCrimeCaseList(predicate);
        EventsCenter.getInstance().post(new SwapTabEvent(1));   // List results toggles to case tab
        return new CommandResult(getMessageForCrimeListShownSummary(model.getFilteredCrimeCaseList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCaseTagsCommand // instanceof handles nulls
                && this.predicate.equals(((FindCaseTagsCommand) other).predicate)); // state check
    }
}
