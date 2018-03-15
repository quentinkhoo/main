package seedu.investigapptor.logic.commands;

import java.util.List;

import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.core.Messages;
import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.commons.events.ui.JumpToListRequestEvent;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.model.person.Person;

/**
 * Selects a person identified using it's last displayed index from the investigapptor book.
 */
public class SelectInvestigatorCommand extends Command {

    public static final String COMMAND_WORD = "selectInvestigator";
    public static final String COMMAND_ALIAS = "selectInvest";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the investigator identified by the index number used in the last listing of investigators.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_PERSON_SUCCESS = "Selected Investigator: %1$s";

    private final Index targetIndex;

    public SelectInvestigatorCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_INVESTIGATOR_DISPLAYED_INDEX);
        }

        EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex));
        return new CommandResult(String.format(MESSAGE_SELECT_PERSON_SUCCESS, targetIndex.getOneBased()));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectInvestigatorCommand // instanceof handles nulls
                && this.targetIndex.equals(((SelectInvestigatorCommand) other).targetIndex)); // state check
    }
}
