package seedu.investigapptor.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.investigapptor.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.investigapptor.logic.commands.AddCaseCommand;
import seedu.investigapptor.logic.commands.AddInvestigatorCommand;
import seedu.investigapptor.logic.commands.ClearCommand;
import seedu.investigapptor.logic.commands.DeleteInvestigatorCommand;
import seedu.investigapptor.logic.commands.EditInvestigatorCommand;
import seedu.investigapptor.logic.commands.EditInvestigatorCommand.EditPersonDescriptor;
import seedu.investigapptor.logic.commands.ExitCommand;
import seedu.investigapptor.logic.commands.FindCaseCommand;
import seedu.investigapptor.logic.commands.FindInvestigatorCommand;
import seedu.investigapptor.logic.commands.HelpCommand;
import seedu.investigapptor.logic.commands.HistoryCommand;
import seedu.investigapptor.logic.commands.ListCaseCommand;
import seedu.investigapptor.logic.commands.ListInvestigatorCommand;
import seedu.investigapptor.logic.commands.RedoCommand;
import seedu.investigapptor.logic.commands.RemovePasswordCommand;
import seedu.investigapptor.logic.commands.SelectInvestigatorCommand;
import seedu.investigapptor.logic.commands.SetPasswordCommand;
import seedu.investigapptor.logic.commands.UndoCommand;
import seedu.investigapptor.logic.parser.exceptions.ParseException;
import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ModelManager;
import seedu.investigapptor.model.Password;
import seedu.investigapptor.model.UserPrefs;
import seedu.investigapptor.model.crimecase.CaseNameContainsKeywordsPredicate;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.person.NameContainsKeywordsPredicate;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.person.investigator.Investigator;
import seedu.investigapptor.testutil.CrimeCaseBuilder;
import seedu.investigapptor.testutil.CrimeCaseUtil;
import seedu.investigapptor.testutil.EditPersonDescriptorBuilder;
import seedu.investigapptor.testutil.InvestigatorBuilder;
import seedu.investigapptor.testutil.InvestigatorUtil;
import seedu.investigapptor.testutil.PersonBuilder;
import seedu.investigapptor.testutil.PersonUtil;

public class InvestigapptorParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final InvestigapptorParser parser = new InvestigapptorParser();
    private Model model = new ModelManager(new Investigapptor(), new UserPrefs());

    //@@author leowweiching
    @Test
    public void parseCommand_add() throws Exception {
        Investigator investigator = new InvestigatorBuilder().build();
        CrimeCase crimeCase = new CrimeCaseBuilder().withInvestigator(investigator).build();
        AddCaseCommand command = (AddCaseCommand)
                parser.parseCommand(CrimeCaseUtil.getAddCommand(crimeCase) + "i/" + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new AddCaseCommand(crimeCase.getCaseName(), crimeCase.getDescription(),
                INDEX_FIRST_PERSON, crimeCase.getStartDate(), crimeCase.getTags()), command);
    }

    //@@author leowweiching
    @Test
    public void parseCommand_addAlias() throws Exception {
        Investigator investigator = new InvestigatorBuilder().build();
        CrimeCase crimeCase = new CrimeCaseBuilder().withInvestigator(investigator).build();
        AddCaseCommand command = (AddCaseCommand)
                parser.parseCommand(CrimeCaseUtil.getAliasAddCommand(crimeCase)
                        + "i/" + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new AddCaseCommand(crimeCase.getCaseName(), crimeCase.getDescription(),
                INDEX_FIRST_PERSON, crimeCase.getStartDate(), crimeCase.getTags()), command);
    }

    //@@author
    @Test
    public void parseCommand_reg() throws Exception {
        Investigator investigator = new InvestigatorBuilder().build();
        AddInvestigatorCommand command = (AddInvestigatorCommand)
                parser.parseCommand(InvestigatorUtil.getRegCommand(investigator));
        assertEquals(new AddInvestigatorCommand(investigator), command);
    }

    @Test
    public void parseCommand_regAlias() throws Exception {
        Investigator investigator = new InvestigatorBuilder().build();
        AddInvestigatorCommand command = (AddInvestigatorCommand)
                parser.parseCommand(InvestigatorUtil.getAliasRegCommand(investigator));
        assertEquals(new AddInvestigatorCommand(investigator), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_clearAlias() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_ALIAS) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_ALIAS + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteInvestigatorCommand command = (DeleteInvestigatorCommand) parser.parseCommand(
                DeleteInvestigatorCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteInvestigatorCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_deleteAlias() throws Exception {
        DeleteInvestigatorCommand command = (DeleteInvestigatorCommand) parser.parseCommand(
                DeleteInvestigatorCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteInvestigatorCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditInvestigatorCommand command = (EditInvestigatorCommand)
                parser.parseCommand(EditInvestigatorCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getPersonDetails(person));
        assertEquals(new EditInvestigatorCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_editAlias() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditInvestigatorCommand command = (EditInvestigatorCommand)
                parser.parseCommand(EditInvestigatorCommand.COMMAND_ALIAS + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getPersonDetails(person));
        assertEquals(new EditInvestigatorCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_exitAlias() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_ALIAS) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_ALIAS + " 3") instanceof ExitCommand);
    }

    //@@author leowweiching
    @Test
    public void parseCommand_findCase() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCaseCommand command = (FindCaseCommand) parser.parseCommand(
                FindCaseCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCaseCommand(new CaseNameContainsKeywordsPredicate(keywords)), command);
    }

    //@@author leowweiching
    @Test
    public void parseCommand_findCaseAlias() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCaseCommand command = (FindCaseCommand) parser.parseCommand(
                FindCaseCommand.COMMAND_ALIAS + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCaseCommand(new CaseNameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findInvestigator() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindInvestigatorCommand command = (FindInvestigatorCommand) parser.parseCommand(
                FindInvestigatorCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindInvestigatorCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findInvestigatorAlias() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindInvestigatorCommand command = (FindInvestigatorCommand) parser.parseCommand(
                FindInvestigatorCommand.COMMAND_ALIAS + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindInvestigatorCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_helpAlias() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_ALIAS) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_ALIAS + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_history() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommand_historyAlias() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommand_listInvestigators() throws Exception {
        assertTrue(parser.parseCommand("listinvestigators") instanceof ListInvestigatorCommand);
        assertTrue(parser.parseCommand(ListInvestigatorCommand.COMMAND_WORD) instanceof ListInvestigatorCommand);
    }

    @Test
    public void parseCommand_listInvestigatorsAlias() throws Exception {
        assertTrue(parser.parseCommand("li") instanceof ListInvestigatorCommand);
        assertTrue(parser.parseCommand(ListInvestigatorCommand.COMMAND_ALIAS) instanceof ListInvestigatorCommand);
    }

    @Test
    public void parseCommand_listCases() throws Exception {
        assertTrue(parser.parseCommand("listcases") instanceof ListCaseCommand);
        assertTrue(parser.parseCommand(ListCaseCommand.COMMAND_WORD) instanceof ListCaseCommand);
    }

    @Test
    public void parseCommand_listCasesAlias() throws Exception {
        assertTrue(parser.parseCommand("lc") instanceof ListCaseCommand);
        assertTrue(parser.parseCommand(ListCaseCommand.COMMAND_ALIAS) instanceof ListCaseCommand);
    }

    @Test
    public void parseCommand_select() throws Exception {
        SelectInvestigatorCommand command = (SelectInvestigatorCommand) parser.parseCommand(
                SelectInvestigatorCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectInvestigatorCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_selectAlias() throws Exception {
        SelectInvestigatorCommand command = (SelectInvestigatorCommand) parser.parseCommand(
                SelectInvestigatorCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectInvestigatorCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_redoCommandWord_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_redoCommandAlias_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_ALIAS) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_undoCommandWord_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_undoCommandAlias_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_ALIAS) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.parseCommand("unknownCommand");
    }

    //@@author quentinkhoo
    @Test
    public void parseCommand_setPassword() throws Exception {
        SetPasswordCommand command = (SetPasswordCommand) parser
                .parseCommand(SetPasswordCommand.COMMAND_WORD + " pw/password");
        assertEquals(new SetPasswordCommand(new Password("password")), command);
    }

    @Test
    public void parserCommand_removePassword() throws Exception {
        assertTrue(parser.parseCommand(RemovePasswordCommand.COMMAND_WORD) instanceof RemovePasswordCommand);
    }
    //@@author
}
