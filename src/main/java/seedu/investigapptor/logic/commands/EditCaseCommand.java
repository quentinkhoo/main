package seedu.investigapptor.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_INVESTIGATOR;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_STARTDATE;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.investigapptor.model.Model.PREDICATE_SHOW_ALL_CASES;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.core.Messages;
import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.commons.events.ui.SwapTabEvent;
import seedu.investigapptor.commons.util.CollectionUtil;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.model.crimecase.CaseName;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.Date;
import seedu.investigapptor.model.crimecase.Description;
import seedu.investigapptor.model.crimecase.exceptions.CrimeCaseNotFoundException;
import seedu.investigapptor.model.crimecase.exceptions.DuplicateCrimeCaseException;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.person.investigator.Investigator;
import seedu.investigapptor.model.tag.Tag;
//@@author leowweiching
/**
 * Edits the details of an existing case in the investigapptor book.
 */
public class EditCaseCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "editCase";
    public static final String COMMAND_ALIAS = "eC";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the case identified "
            + "by the index number used in the last listing of crimeCases. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_DESCRIPTION + "DESCRIPTION] "
            + "[" + PREFIX_INVESTIGATOR + "INVESTIGATOR INDEX (must be a positive integer)] "
            + "[" + PREFIX_STARTDATE + "STARTDATE] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DESCRIPTION + "91234567 "
            + PREFIX_INVESTIGATOR + "johndoe@example.com";

    public static final String MESSAGE_EDIT_CASE_SUCCESS = "Edited Case: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_CASE = "This case already exists in investigapptor.";

    private final Index caseIndex;
    private final EditCrimeCaseDescriptor editCrimeCaseDescriptor;

    private CrimeCase crimeCaseToEdit;
    private CrimeCase editedCrimeCase;

    /**
     * @param caseIndex of the case in the filtered case list to edit
     * @param editCrimeCaseDescriptor details to edit the case with
     */
    public EditCaseCommand(Index caseIndex, EditCrimeCaseDescriptor editCrimeCaseDescriptor) {
        requireNonNull(caseIndex);
        requireNonNull(editCrimeCaseDescriptor);

        this.caseIndex = caseIndex;
        this.editCrimeCaseDescriptor = new EditCrimeCaseDescriptor(editCrimeCaseDescriptor);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.updateCrimeCase(crimeCaseToEdit, editedCrimeCase);
            EventsCenter.getInstance().post(new SwapTabEvent(1));
        } catch (DuplicateCrimeCaseException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_CASE);
        } catch (CrimeCaseNotFoundException pnfe) {
            throw new AssertionError("The target case cannot be missing");
        }
        model.updateFilteredCrimeCaseList(PREDICATE_SHOW_ALL_CASES);
        return new CommandResult(String.format(MESSAGE_EDIT_CASE_SUCCESS, editedCrimeCase));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<CrimeCase> lastShownCrimeCaseList = model.getFilteredCrimeCaseList();
        List<Person> lastShownPersonList = model.getFilteredPersonList();

        Index investigatorIndex = editCrimeCaseDescriptor.getCurrentInvestigatorIndex()
                .orElse(null);

        if (caseIndex.getZeroBased() >= lastShownCrimeCaseList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
        }

        crimeCaseToEdit = lastShownCrimeCaseList.get(caseIndex.getZeroBased());

        // An investigator index was parsed
        if (investigatorIndex != null) {
            if (investigatorIndex.getZeroBased() >= lastShownPersonList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_INVESTIGATOR_DISPLAYED_INDEX);
            }
            editCrimeCaseDescriptor
                    .setCurrentInvestigator((Investigator) lastShownPersonList
                            .get(investigatorIndex.getZeroBased()));
        } else {
            editCrimeCaseDescriptor.setCurrentInvestigator(crimeCaseToEdit.getCurrentInvestigator());
        }

        editedCrimeCase = createEditedCrimeCase(crimeCaseToEdit, editCrimeCaseDescriptor);
    }

    /**
     * Creates and returns a {@code CrimeCase} with the details of {@code crimeCaseToEdit}
     * edited with {@code editCrimeCaseDescriptor}.
     */
    private static CrimeCase createEditedCrimeCase(CrimeCase crimeCaseToEdit,
                                                   EditCrimeCaseDescriptor editCrimeCaseDescriptor) {
        assert crimeCaseToEdit != null;

        CaseName updatedCaseName = editCrimeCaseDescriptor.getCaseName().orElse(crimeCaseToEdit.getCaseName());
        Description updatedDescription = editCrimeCaseDescriptor.getDescription()
                .orElse(crimeCaseToEdit.getDescription());
        Investigator updatedInvestigator = editCrimeCaseDescriptor.getCurrentInvestigator()
                .orElse(crimeCaseToEdit.getCurrentInvestigator());
        Date updatedStartDate = editCrimeCaseDescriptor.getStartDate().orElse(crimeCaseToEdit.getStartDate());
        Set<Tag> updatedTags = editCrimeCaseDescriptor.getTags().orElse(crimeCaseToEdit.getTags());

        return new CrimeCase(updatedCaseName, updatedDescription, updatedInvestigator,
                updatedStartDate, crimeCaseToEdit.getEndDate(), crimeCaseToEdit.getStatus(), updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCaseCommand)) {
            return false;
        }

        // state check
        EditCaseCommand e = (EditCaseCommand) other;
        return caseIndex.equals(e.caseIndex)
                && editCrimeCaseDescriptor.equals(e.editCrimeCaseDescriptor)
                && Objects.equals(crimeCaseToEdit, e.crimeCaseToEdit);
    }

    /**
     * Stores the details to edit the case with. Each non-empty field value will replace the
     * corresponding field value of the crimeCase.
     */
    public static class EditCrimeCaseDescriptor {
        private CaseName name;
        private Description description;
        private Index currentInvestigatorIndex;
        private Investigator currentInvestigator;
        private Date startDate;
        private Set<Tag> tags;

        public EditCrimeCaseDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditCrimeCaseDescriptor(EditCrimeCaseDescriptor toCopy) {
            setCaseName(toCopy.name);
            setDescription(toCopy.description);
            setCurrentInvestigatorIndex(toCopy.currentInvestigatorIndex);
            setCurrentInvestigator(toCopy.currentInvestigator);
            setStartDate(toCopy.startDate);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.name, this.description, this.currentInvestigatorIndex,
                    this.startDate, this.tags);
        }

        public void setCaseName(CaseName name) {
            this.name = name;
        }

        public Optional<CaseName> getCaseName() {
            return Optional.ofNullable(name);
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }

        public void setCurrentInvestigatorIndex(Index currentInvestigatorIndex) {
            this.currentInvestigatorIndex = currentInvestigatorIndex;
        }

        public Optional<Index> getCurrentInvestigatorIndex() {
            return Optional.ofNullable(currentInvestigatorIndex);
        }

        public void setCurrentInvestigator(Investigator currentInvestigator) {
            this.currentInvestigator = currentInvestigator;
        }

        public Optional<Investigator> getCurrentInvestigator() {
            return Optional.ofNullable(currentInvestigator);
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Optional<Date> getStartDate() {
            return Optional.ofNullable(startDate);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditCrimeCaseDescriptor)) {
                return false;
            }

            // state check
            EditCrimeCaseDescriptor e = (EditCrimeCaseDescriptor) other;

            return getCaseName().equals(e.getCaseName())
                    && getDescription().equals(e.getDescription())
                    && getCurrentInvestigator().equals(e.getCurrentInvestigator())
                    && getStartDate().equals(e.getStartDate())
                    && getTags().equals(e.getTags());
        }
    }
}
