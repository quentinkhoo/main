package seedu.investigapptor.logic.commands;

import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.events.ui.SwapTabEvent;
import seedu.investigapptor.model.person.NameContainsKeywordsPredicate;

/**
 * Finds and lists all persons in investigapptor book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindInvestigatorCommand extends Command {

    public static final String COMMAND_WORD = "findInv";
    public static final String COMMAND_ALIAS = "fI";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final NameContainsKeywordsPredicate predicate;

    public FindInvestigatorCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(predicate);
        EventsCenter.getInstance().post(new SwapTabEvent(0));
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindInvestigatorCommand // instanceof handles nulls
                && this.predicate.equals(((FindInvestigatorCommand) other).predicate)); // state check
    }
}
