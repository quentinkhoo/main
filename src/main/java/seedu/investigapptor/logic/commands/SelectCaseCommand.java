package seedu.investigapptor.logic.commands;

import java.util.List;

import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.core.Messages;
import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.commons.events.ui.JumpToCrimeCaseListRequestEvent;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.model.crimecase.CrimeCase;

//@@author leowweiching
/**
 * Selects a person identified using it's last displayed index from the investigapptor book.
 */
public class SelectCaseCommand extends Command {

    public static final String COMMAND_WORD = "selectcase";
    public static final String COMMAND_ALIAS = "sc";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the case identified by the index number used in the last listing of cases.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_CASE_SUCCESS = "Selected Case: %1$s";

    private final Index targetIndex;

    public SelectCaseCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<CrimeCase> lastShownList = model.getFilteredCrimeCaseList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
        }

        EventsCenter.getInstance().post(new JumpToCrimeCaseListRequestEvent(targetIndex));
        return new CommandResult(String.format(MESSAGE_SELECT_CASE_SUCCESS, targetIndex.getOneBased()));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectCaseCommand // instanceof handles nulls
                && this.targetIndex.equals(((SelectCaseCommand) other).targetIndex)); // state check
    }
}
