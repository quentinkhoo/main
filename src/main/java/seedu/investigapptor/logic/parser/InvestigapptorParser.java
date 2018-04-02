package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.investigapptor.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.investigapptor.logic.commands.AddCaseCommand;
import seedu.investigapptor.logic.commands.BackupCommand;
import seedu.investigapptor.logic.commands.ClearCommand;
import seedu.investigapptor.logic.commands.CloseCaseCommand;
import seedu.investigapptor.logic.commands.Command;
import seedu.investigapptor.logic.commands.DeleteCaseCommand;
import seedu.investigapptor.logic.commands.DeleteInvestigatorCommand;
import seedu.investigapptor.logic.commands.EditCaseCommand;
import seedu.investigapptor.logic.commands.EditInvestigatorCommand;
import seedu.investigapptor.logic.commands.ExitCommand;
import seedu.investigapptor.logic.commands.FindCaseCommand;
import seedu.investigapptor.logic.commands.FindCaseTagsCommand;
import seedu.investigapptor.logic.commands.FindInvestTagsCommand;
import seedu.investigapptor.logic.commands.FindInvestigatorCommand;
import seedu.investigapptor.logic.commands.HelpCommand;
import seedu.investigapptor.logic.commands.HistoryCommand;
import seedu.investigapptor.logic.commands.ListCommand;
import seedu.investigapptor.logic.commands.RedoCommand;
import seedu.investigapptor.logic.commands.RegisterInvestigatorCommand;
import seedu.investigapptor.logic.commands.SelectInvestigatorCommand;
import seedu.investigapptor.logic.commands.SetCommand;
import seedu.investigapptor.logic.commands.UndoCommand;
import seedu.investigapptor.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class InvestigapptorParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCaseCommand.COMMAND_WORD:
        case AddCaseCommand.COMMAND_ALIAS:
            return new AddCaseCommandParser().parse(arguments);

        case RegisterInvestigatorCommand.COMMAND_WORD:
        case RegisterInvestigatorCommand.COMMAND_ALIAS:
            return new RegisterInvestigatorCommandParser().parse(arguments);

        case EditCaseCommand.COMMAND_WORD:
        case EditCaseCommand.COMMAND_ALIAS:
            return new EditCaseCommandParser().parse(arguments);

        case EditInvestigatorCommand.COMMAND_WORD:
        case EditInvestigatorCommand.COMMAND_ALIAS:
            return new EditInvestigatorCommandParser().parse(arguments);

        case SelectInvestigatorCommand.COMMAND_WORD:
        case SelectInvestigatorCommand.COMMAND_ALIAS:
            return new SelectInvestigatorCommandParser().parse(arguments);

        case DeleteCaseCommand.COMMAND_WORD:
        case DeleteCaseCommand.COMMAND_ALIAS:
            return new DeleteCaseCommandParser().parse(arguments);

        case DeleteInvestigatorCommand.COMMAND_WORD:
        case DeleteInvestigatorCommand.COMMAND_ALIAS:
            return new DeleteInvestigatorCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
        case ClearCommand.COMMAND_ALIAS:
            return new ClearCommand();

        case CloseCaseCommand.COMMAND_WORD:
        case CloseCaseCommand.COMMAND_ALIAS:
            return new CloseCaseCommandParser().parse(arguments);

        case FindCaseCommand.COMMAND_WORD:
        case FindCaseCommand.COMMAND_ALIAS:
            return new FindCaseCommandParser().parse(arguments);

        case FindInvestigatorCommand.COMMAND_WORD:
        case FindInvestigatorCommand.COMMAND_ALIAS:
            return new FindInvestigatorCommandParser().parse(arguments);

        case FindInvestTagsCommand.COMMAND_WORD:
        case FindInvestTagsCommand.COMMAND_ALIAS:
            return new FindInvestTagsCommandParser().parse(arguments);

        case FindCaseTagsCommand.COMMAND_WORD:
        case FindCaseTagsCommand.COMMAND_ALIAS:
            return new FindCaseTagsCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
        case ListCommand.COMMAND_ALIAS:
            return new ListCommandParser().parse(arguments);

        case SetCommand.COMMAND_WORD:
        case SetCommand.COMMAND_ALIAS:
            return new SetCommandParser().parse(arguments);

        case HistoryCommand.COMMAND_WORD:
        case HistoryCommand.COMMAND_ALIAS:
            return new HistoryCommand();

        case ExitCommand.COMMAND_WORD:
        case ExitCommand.COMMAND_ALIAS:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
        case HelpCommand.COMMAND_ALIAS:
            return new HelpCommand();

        case UndoCommand.COMMAND_WORD:
        case UndoCommand.COMMAND_ALIAS:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
        case RedoCommand.COMMAND_ALIAS:
            return new RedoCommand();

        case BackupCommand.COMMAND_WORD:
        case BackupCommand.COMMAND_ALIAS:
            return new BackupCommandParser().parse(arguments);

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
