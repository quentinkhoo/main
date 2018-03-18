package seedu.investigapptor.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.investigapptor.model.Model.PREDICATE_SHOW_ALL_INVESTIGATORS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.investigapptor.commons.core.Messages;
import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.commons.util.CollectionUtil;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.model.person.Address;
import seedu.investigapptor.model.person.Email;
import seedu.investigapptor.model.person.Name;
import seedu.investigapptor.model.person.Phone;
import seedu.investigapptor.model.person.exceptions.DuplicatePersonException;
import seedu.investigapptor.model.person.exceptions.PersonNotFoundException;
import seedu.investigapptor.model.person.investigator.Investigator;
import seedu.investigapptor.model.tag.Tag;

/**
 * Edits the details of an existing person in the investigapptor book.
 */
public class EditInvestigatorCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "editInvestigator";
    public static final String COMMAND_ALIAS = "eI";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the investigator identified "
            + "by the index number used in the last listing of investigators. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Investigator: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This investigator already exists in investigapptor.";

    private final Index index;
    private final EditInvestigatorDescriptor editInvestigatorDescriptor;

    private Investigator investigatorToEdit;
    private Investigator editedInvestigator;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editInvestigatorDescriptor details to edit the person with
     */
    public EditInvestigatorCommand(Index index, EditInvestigatorDescriptor editInvestigatorDescriptor) {
        requireNonNull(index);
        requireNonNull(editInvestigatorDescriptor);

        this.index = index;
        this.editInvestigatorDescriptor = new EditInvestigatorDescriptor(editInvestigatorDescriptor);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.updateInvestigator(investigatorToEdit, editedInvestigator);
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target investigator cannot be missing");
        }
        model.updateFilteredInvestigatorList(PREDICATE_SHOW_ALL_INVESTIGATORS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedInvestigator));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Investigator> lastShownList = model.getFilteredInvestigatorList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_INVESTIGATOR_DISPLAYED_INDEX);
        }

        investigatorToEdit = lastShownList.get(index.getZeroBased());
        editedInvestigator = createEditedPerson(investigatorToEdit, editInvestigatorDescriptor);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code investigatorToEdit}
     * edited with {@code editInvestigatorDescriptor}.
     */
    private static Investigator createEditedPerson(Investigator personToEdit, EditInvestigatorDescriptor editInvestigatorDescriptor) {
        assert personToEdit != null;

        Name updatedName = editInvestigatorDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editInvestigatorDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editInvestigatorDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editInvestigatorDescriptor.getAddress().orElse(personToEdit.getAddress());
        Set<Tag> updatedTags = editInvestigatorDescriptor.getTags().orElse(personToEdit.getTags());

        return new Investigator(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditInvestigatorCommand)) {
            return false;
        }

        // state check
        EditInvestigatorCommand e = (EditInvestigatorCommand) other;
        return index.equals(e.index)
                && editInvestigatorDescriptor.equals(e.editInvestigatorDescriptor)
                && Objects.equals(investigatorToEdit, e.investigatorToEdit);
    }

    /**
     * Stores the details to edit the investigator with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditInvestigatorDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;

        public EditInvestigatorDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditInvestigatorDescriptor(EditInvestigatorDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.name, this.phone, this.email, this.address, this.tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
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
            if (!(other instanceof EditInvestigatorDescriptor)) {
                return false;
            }

            // state check
            EditInvestigatorDescriptor e = (EditInvestigatorDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getTags().equals(e.getTags());
        }
    }
}
