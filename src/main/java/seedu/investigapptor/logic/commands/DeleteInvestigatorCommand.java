package seedu.investigapptor.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import seedu.investigapptor.commons.core.Messages;
import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.model.person.exceptions.PersonNotFoundException;
import seedu.investigapptor.model.person.investigator.Investigator;

/**
 * Deletes a person identified using it's last displayed index from the investigapptor book.
 */
public class DeleteInvestigatorCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "deleteInvestigator";
    public static final String COMMAND_ALIAS = "dI";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the investigator identified by the index number used in the last listing of investigators.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Investigator: %1$s";

    private final Index targetIndex;

    private Investigator investigatorToDelete;

    public DeleteInvestigatorCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(investigatorToDelete);
        try {
            model.deleteInvestigator(investigatorToDelete);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target investigator cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, investigatorToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Investigator> lastShownList = model.getFilteredInvestigatorList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_INVESTIGATOR_DISPLAYED_INDEX);
        }

        investigatorToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteInvestigatorCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteInvestigatorCommand) other).targetIndex) // state check
                && Objects.equals(this.investigatorToDelete, ((DeleteInvestigatorCommand) other).investigatorToDelete));
    }
}
