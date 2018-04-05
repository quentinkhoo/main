package seedu.investigapptor.logic.commands;

import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.events.ui.SwapTabEvent;
import seedu.investigapptor.model.person.investigator.TagContainsKeywordsPredicate;

//@@author pkaijun
/**
 * Finds and lists all investigators in investigapptor whose tags contains any of the argument keywords.
 * Keyword matching is not case-sensitive.
 */
public class FindInvestTagsCommand extends Command {

    public static final String COMMAND_WORD = "findinvestigatortags";
    public static final String COMMAND_ALIAS = "fit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds investigators whose tags contain any of "
            + "the specified keywords and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " teamA";

    private final TagContainsKeywordsPredicate predicate;

    public FindInvestTagsCommand(TagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(predicate);
        EventsCenter.getInstance().post(new SwapTabEvent(0));   // List results toggles to investigators tab
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindInvestTagsCommand // instanceof handles nulls
                && this.predicate.equals(((FindInvestTagsCommand) other).predicate)); // state check
    }
}
