package seedu.investigapptor.logic.commands;

import seedu.investigapptor.model.person.investigator.TagContainsKeywordsPredicate;

/**
 * Finds and lists all investigators in investigapptor whose tags contains any of the argument keywords.
 * Keyword matching is not case-sensitive.
 */
public class FindTagsCommand extends Command {

    public static final String COMMAND_WORD = "findtags";
    public static final String COMMAND_ALIAS = "ft";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds investigators whose tags contain any of "
            + "the specified keywords and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " teamA";

    private final TagContainsKeywordsPredicate predicate;

    public FindTagsCommand(TagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(predicate);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindTagsCommand // instanceof handles nulls
                && this.predicate.equals(((FindTagsCommand) other).predicate)); // state check
    }
}
