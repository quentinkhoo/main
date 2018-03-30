//@@author pkaijun
package seedu.investigapptor.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.model.Model.PREDICATE_SHOW_ALL_CASES;
import static seedu.investigapptor.model.crimecase.Status.CASE_CLOSE;

import java.util.List;
import java.util.Set;

import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.core.Messages;
import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.commons.events.ui.SwapTabEvent;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.model.crimecase.CaseName;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.Date;
import seedu.investigapptor.model.crimecase.Description;
import seedu.investigapptor.model.crimecase.Status;
import seedu.investigapptor.model.crimecase.exceptions.CrimeCaseNotFoundException;
import seedu.investigapptor.model.crimecase.exceptions.DuplicateCrimeCaseException;
import seedu.investigapptor.model.person.investigator.Investigator;
import seedu.investigapptor.model.tag.Tag;

/**
 * Update the status of a case from open to close and update the EndDate field
 */
public class CloseCaseCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "close";
    public static final String COMMAND_ALIAS = "cl";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the status from open to close "
            + "and updates the end date accordingly.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "Example: " + COMMAND_WORD + " 1 ";

    public static final String MESSAGE_CLOSE_CASE_SUCCESS = "Case status updated: %1$s";
    public static final String MESSAGE_DUPLICATE_CASE = "This case already exists in investigapptor.";
    public static final String MESSAGE_CASE_ALREADY_CLOSE = "Case is already closed.";

    private final Index index;

    private CrimeCase caseToEdit;
    private CrimeCase editedCase;

    /**
     * @param index of the crimecase in the filtered crimecase list to close
     */
    public CloseCaseCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.updateCrimeCase(caseToEdit, editedCase);
        } catch (DuplicateCrimeCaseException dce) {
            throw new CommandException(MESSAGE_DUPLICATE_CASE);
        } catch (CrimeCaseNotFoundException cnfe) {
            throw new AssertionError("The target case cannot be missing");
        }
        model.updateFilteredCrimeCaseList(PREDICATE_SHOW_ALL_CASES);
        return new CommandResult(String.format(MESSAGE_CLOSE_CASE_SUCCESS, editedCase.getStatus()));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        EventsCenter.getInstance().post(new SwapTabEvent(1));   // List results toggles to case tab

        List<CrimeCase> lastShownList = model.getFilteredCrimeCaseList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
        }

        caseToEdit = lastShownList.get(index.getZeroBased());

        if (caseToEdit.getStatus().toString().equals(CASE_CLOSE)) {
            throw new CommandException(MESSAGE_CASE_ALREADY_CLOSE);
        }

        editedCase = createEditedCase(caseToEdit);
    }

    /**
     * Creates and returns a {@code CrimeCase} with the details of {@code caseToEdit}
     * Updates status to "close" with the other fields remaining the same
     */
    private static CrimeCase createEditedCase(CrimeCase caseToEdit) {
        assert caseToEdit != null;

        CaseName name = caseToEdit.getCaseName();
        Description desc = caseToEdit.getDescription();
        Date date = caseToEdit.getStartDate();
        Set<Tag> tags = caseToEdit.getTags();
        Investigator investigator = caseToEdit.getCurrentInvestigator();
        Status status = caseToEdit.getStatus();
        status.closeCase();    // Close case status only

        return new CrimeCase(name, desc, investigator, date, status, tags);
    }
}
