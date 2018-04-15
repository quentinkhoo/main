package seedu.investigapptor.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.core.Messages;
import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.commons.events.ui.SwapTabEvent;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.person.exceptions.PersonNotFoundException;
import seedu.investigapptor.model.person.investigator.Investigator;
//@@author Marcus-cxc
/**
 * Deletes a person identified using it's last displayed index from the investigapptor book.
 */
public class DeleteInvestigatorCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "deleteinvestigator";
    public static final String COMMAND_ALIAS = "di";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the investigator identified by the index number used in the last listing of investigators.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Investigator: %1$s";
    public static final String MESSAGE_ACTIVE_INVESTIGATOR = "Investigator is currently in charge of a case.\n"
            + "Please reassign the cases to another investigator to delete the selected investigator";
    private final Index targetIndex;

    private Person personToDelete;

    public DeleteInvestigatorCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    /**
    * Try to call the model to delete Person (@personToDelete)
    *
    */
    private void deletePerson() {
        try {
            model.deletePerson(personToDelete);
            EventsCenter.getInstance().post(new SwapTabEvent(0));
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target investigator cannot be missing");
        }
    }
    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(personToDelete);
        if (personToDelete instanceof Investigator && !((Investigator) personToDelete).isCaseListEmpty()) {
            throw new CommandException(MESSAGE_ACTIVE_INVESTIGATOR);
        } else {
            deletePerson();
        }
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_INVESTIGATOR_DISPLAYED_INDEX);
        }

        personToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteInvestigatorCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteInvestigatorCommand) other).targetIndex) // state check
                && Objects.equals(this.personToDelete, ((DeleteInvestigatorCommand) other).personToDelete));
    }
}
