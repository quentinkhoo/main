# quentinkhoo
###### \ListCommand.java
``` java
/**
 * Lists all persons in the investigapptor book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String COMMAND_ALIAS = "l";
    public static final String[] TYPE_INVESTIGATOR_ALIASES = {"investigators", "investigator", "invest", "inv", "i"};
    public static final String[] TYPE_COMMAND_CASE_ALIASES = {"cases", "case", "cas", "c"};
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists the specified type\n"
            + "Parameters: TYPE (must be either investigators or cases)\n"
            + "Example: " + COMMAND_WORD + " cases";
    public static final String MESSAGE_ALIASES =
            "investigators: Can either be investigators, investigator, invest, inv or i\n"
            + "Example: " + COMMAND_WORD + " invest\n"
            + "cases: Can either be cases, case, cas or c\n"
            + "Example: " + COMMAND_WORD + " cas";

    public static final String MESSAGE_SUCCESS = "Listed all %1$s";

    private final String listType;

    public ListCommand(String listType) {
        this.listType = listType;
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (isValidInvestigatorAlias(listType)) {
            return new ListInvestigatorCommand().execute();

        } else if (isValidCaseAlias(listType)) {
            return new ListCaseCommand().execute();

        } else {
            throw new CommandException(MESSAGE_INVALID_COMMAND_ALIAS);
        }

    }

    /**
     * Checks and returns an input listing type is a valid investigator alias
     * @param type
     */
    public static boolean isValidInvestigatorAlias(String type) {
        for (String alias : TYPE_INVESTIGATOR_ALIASES) {
            if (type.equals(alias)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks and returns an input listing type is a valid case alias
     * @param type
     */
    public static boolean isValidCaseAlias(String type) {
        for (String alias : TYPE_COMMAND_CASE_ALIASES) {
            if (type.equals(alias)) {
                return true;
            }
        }
        return false;
    }
}
```
###### \ListCommandParser.java
``` java
/**
 + * Parses input arguments and creates a new ListCommand object
 + */
public class ListCommandParser implements Parser<ListCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListCommand
     * and returns an ListCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */

    public ListCommand parse(String args) throws ParseException {
        try {
            String type = ParserUtil.parseListType(args);
            return new ListCommand(type);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_ALIAS, ListCommand.MESSAGE_ALIASES));
        }
    }
}
```
###### \RemovePasswordCommandParser.java
``` java
/**
 * Parses input arguments and creates a new PasswordCommand object
 */
public class RemovePasswordCommandParser implements Parser<RemovePasswordCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the PasswordCommand
     * and returns an PasswordCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RemovePasswordCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PASSWORD);

        if (!arePrefixesPresent(argMultimap, PREFIX_PASSWORD)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    RemovePasswordCommand.MESSAGE_USAGE));
        }

        String inputPassword = args.substring(4);
        return new RemovePasswordCommand();
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### \SetCommand.java
``` java
/**
 * Sets a specific settings for the application
 */
public class SetCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "set";
    public static final String COMMAND_ALIAS = "s";
    public static final String[] TYPE_PASSWORD_ALIASES = {"password", "pass", "pw", "p"};
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets the specified type\n"
            + "Parameters: TYPE (must be password)\n"
            + "Example: " + COMMAND_WORD + " cases";
    public static final String MESSAGE_ALIASES =
            "password: Can either be password, pass, pw or p\n"
                    + "Example: " + COMMAND_WORD + " pw\n";

    public static final String MESSAGE_SUCCESS = "Succesfully set %1$s";

    private final String setType;
    private String args;
    private String[] argsArray;

    public SetCommand(String args) {
        this.args = args.trim();
        argsArray = args.split("\\s+");
        this.setType = argsArray[0];
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        if (isValidPasswordAlias(setType)) {
            try {
                if (args.contains(" ")) {
                    args = args.substring(args.indexOf(" "));
                    return new SetPasswordCommandParser().parse(args).executeUndoableCommand();
                } else {
                    return new CommandResult(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            SetPasswordCommand.MESSAGE_USAGE));
                }
            } catch (ParseException pe) {
                return new CommandResult(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        SetPasswordCommand.MESSAGE_USAGE));
            }
        } else {
            throw new CommandException(MESSAGE_INVALID_COMMAND_ALIAS);
        }
    }

    /**
     * Checks and returns an input listing type is a valid investigator alias
     * @param type
     */
    public static boolean isValidPasswordAlias(String type) {
        for (String alias : TYPE_PASSWORD_ALIASES) {
            if (type.equals(alias)) {
                return true;
            }
        }
        return false;
    }

}
```
###### \SetCommandParser.java
``` java
/**
 + * Parses input arguments and creates a new ListCommand object
 + */
public class SetCommandParser implements Parser<Command> {

    private String inputType;
    private String actualType;

    /**
     * Parses the given {@code String} of arguments in the context of the SetCommand
     * and returns an SetCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parse(String args) throws ParseException {
        args = args.trim();
        String[] argsArray = args.split("\\s+");
        try {
            inputType = argsArray[0];
            actualType = ParserUtil.parseSetType(inputType);
            return new SetCommand(args);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_ALIAS, SetCommand.MESSAGE_ALIASES));
        }
    }
}
```
###### \SetCommandParserTest.java
``` java
public class SetCommandParserTest {

    private SetCommandParser parser;

    @Before
    public void setUp() {
        parser = new SetCommandParser();
    }

    @Test
    public void parse_invalidAlias_failure() {
        assertParseFailure(parser, "set p@ssword", String.format(Messages.MESSAGE_INVALID_COMMAND_ALIAS,
                SetCommand.MESSAGE_ALIASES));
    }
}
```
###### \SetCommandTest.java
``` java
public class SetCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final SetCommand setCommand = new SetCommand("p@ssword p/password");

    @Test
    public void executeUndoableCommand_invalidAlias_throwsCommandException()
            throws CommandException {
        thrown.expect(CommandException.class);
        setCommand.executeUndoableCommand();
    }
}
```
###### \SetPasswordCommandParser.java
``` java
/**
 * Parses input arguments and creates a new PasswordCommand object
 */
public class SetPasswordCommandParser implements Parser<SetPasswordCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the PasswordCommand
     * and returns an PasswordCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SetPasswordCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PASSWORD);

        if (!arePrefixesPresent(argMultimap, PREFIX_PASSWORD)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetPasswordCommand.MESSAGE_USAGE));
        }

        try {
            String inputPassword = args.substring(4);
            Password newPassword = ParserUtil.parsePassword(inputPassword);
            return new SetPasswordCommand(newPassword);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage());
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
