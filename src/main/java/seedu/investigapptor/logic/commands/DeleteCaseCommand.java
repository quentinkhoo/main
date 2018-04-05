package seedu.investigapptor.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.core.Messages;
import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.commons.events.ui.SwapTabEvent;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.exceptions.CrimeCaseNotFoundException;
//@@author leowweiching-reused
/**
 * Deletes a case identified using it's last displayed index from the investigapptor book.
 */
public class DeleteCaseCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "deleteCase";
    public static final String COMMAND_ALIAS = "dC";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the case identified by the index number used in the last listing of cases.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_CASE_SUCCESS = "Deleted Case: %1$s";

    private final Index targetIndex;

    private CrimeCase caseToDelete;

    public DeleteCaseCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(caseToDelete);
        try {
            model.deleteCrimeCase(caseToDelete);
            EventsCenter.getInstance().post(new SwapTabEvent(1));
        } catch (CrimeCaseNotFoundException pnfe) {
            throw new AssertionError("The target case cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_CASE_SUCCESS, caseToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<CrimeCase> lastShownList = model.getFilteredCrimeCaseList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
        }

        caseToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCaseCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteCaseCommand) other).targetIndex) // state check
                && Objects.equals(this.caseToDelete, ((DeleteCaseCommand) other).caseToDelete));
    }
}
