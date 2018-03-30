package seedu.investigapptor.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_INVESTIGATOR;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_STARTDATE;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.investigapptor.commons.core.Messages;
import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.model.crimecase.CaseName;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.Description;
import seedu.investigapptor.model.crimecase.Date;
import seedu.investigapptor.model.crimecase.Status;
import seedu.investigapptor.model.crimecase.exceptions.DuplicateCrimeCaseException;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.person.investigator.Investigator;
import seedu.investigapptor.model.tag.Tag;

/**
 * Adds a case to the investigapptor book.
 */
public class AddCaseCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "add";
    public static final String COMMAND_ALIAS = "a";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a case to the investigapptor book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_DESCRIPTION + "DESCRIPTION "
            + PREFIX_INVESTIGATOR + "INDEX (must be a positive integer) "
            + PREFIX_STARTDATE + "START DATE "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Project Magic "
            + PREFIX_DESCRIPTION + "Kidnapping of 6 year-old John Doe "
            + PREFIX_INVESTIGATOR + "1 "
            + PREFIX_STARTDATE + "25/12/2017 "
            + PREFIX_TAG + "Homicide "
            + PREFIX_TAG + "Missing Persons";

    public static final String MESSAGE_SUCCESS = "New case added: %1$s";
    public static final String MESSAGE_DUPLICATE_CASE = "This case already exists in the investigapptor book";

    private CaseName name;
    private Description description;
    private Index investigatorIndex;
    private Date startDate;
    private Set<Tag> tagList;

    private CrimeCase toAdd;

    /**
     * Creates an AddCaseCommand to add the specified {@code CrimeCase}
     */
    public AddCaseCommand(CrimeCase crimeCase) {
        requireNonNull(crimeCase);
        toAdd = crimeCase;
    }

    /**
     * @param name of the case to be added
     * @param description of the case to be added
     * @param investigatorIndex of the investigator to be added
     * @param startDate of the case to be added
     * @param tagList of the case to be added
     */
    public AddCaseCommand(CaseName name, Description description, Index investigatorIndex,
                          Date startDate, Set<Tag> tagList) {
        requireNonNull(name);
        requireNonNull(description);
        requireNonNull(investigatorIndex);
        requireNonNull(startDate);

        this.name = name;
        this.description = description;
        this.investigatorIndex = investigatorIndex;
        this.startDate = startDate;
        this.tagList = tagList;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.addCrimeCase(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateCrimeCaseException e) {
            throw new CommandException(MESSAGE_DUPLICATE_CASE);
        }

    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        if (investigatorIndex != null) {
            List<Person> lastShownList = model.getFilteredPersonList();

            if (investigatorIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_INVESTIGATOR_DISPLAYED_INDEX);
            }
            Person investigatorToAdd = lastShownList.get(investigatorIndex.getZeroBased());
            if (investigatorToAdd instanceof Investigator) {
                toAdd = createCrimeCase((Investigator) investigatorToAdd);
            } else {
                throw new CommandException("Selected personal is not an investigator");
            }
        }
    }

    /**
     * Creates and returns a {@code CrimeCase} with the details of {@code investigatorToAdd}
     */
    private CrimeCase createCrimeCase(Investigator investigatorToAdd) {
        assert investigatorToAdd != null;

        return new CrimeCase(this.name, this.description, investigatorToAdd,
                this.startDate, new Status(), this.tagList);
    }

    @Override
    public boolean equals(Object other) {

        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCaseCommand)) {
            return false;
        }

        // state check
        return Objects.equals(toAdd, ((AddCaseCommand) other).toAdd);
    }
}
