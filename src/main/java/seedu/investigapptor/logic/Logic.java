package seedu.investigapptor.logic;

import javafx.collections.ObservableList;
import seedu.investigapptor.logic.commands.CommandResult;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.logic.parser.exceptions.ParseException;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.person.investigator.Investigator;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<Investigator> getFilteredInvestigatorList();

    /** Returns an unmodifiable view of the filtered list of investigators */
    //ObservableList<Investigator> getFilteredInvestigatorList();

    /** Returns the list of input entered by the user, encapsulated in a {@code ListElementPointer} object */
    ListElementPointer getHistorySnapshot();
}
