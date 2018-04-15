package seedu.investigapptor.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.core.Messages;
import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.commons.events.ui.SwapTabEvent;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.model.crimecase.CaseContainsInvestigatorPredicate;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.person.investigator.Investigator;
//@@author Marcus-cxc
/**
 * Lists all investigators in the investigapptor book to the user.
 */
public class ListInvestigatorCaseCommand extends Command {


    public static final String COMMAND_WORD = "listinvestigatorcases";
    public static final String COMMAND_ALIAS = "lic";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all cases of the selected investigator "
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Listed all %1$s";
    private final Index targetIndex;
    private Investigator investigator;

    private CaseContainsInvestigatorPredicate predicate;

    public ListInvestigatorCaseCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (targetIndex != null) {
            List<Person> lastShownList = model.getFilteredPersonList();

            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_INVESTIGATOR_DISPLAYED_INDEX);
            }
            investigator = (Investigator) lastShownList.get(targetIndex.getZeroBased());
            requireNonNull(investigator);
            predicate = new CaseContainsInvestigatorPredicate(investigator.hashCode());
        }
        model.updateFilteredCrimeCaseList(predicate);
        EventsCenter.getInstance().post(new SwapTabEvent(1));
        return new CommandResult(getMessageForCrimeCaseListShownSummary(model.getFilteredCrimeCaseList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ListInvestigatorCaseCommand // instanceof handles nulls
                && this.predicate.equals(((ListInvestigatorCaseCommand) other).predicate)); // state check
    }

}
