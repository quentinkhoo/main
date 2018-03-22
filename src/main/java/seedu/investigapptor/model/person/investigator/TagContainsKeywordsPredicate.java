package seedu.investigapptor.model.person.investigator;

import java.util.List;
import java.util.function.Predicate;

import seedu.investigapptor.model.person.Person;

/**
 * Tests that a {@code Person}'s {@code Tags} matches any of the keywords given.
 */
public class TagContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public TagContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    /* Returns true if keywords matches with any element in the set of tags of a person
     */
    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(person.getTagsRaw()::contains);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((TagContainsKeywordsPredicate) other).keywords)); // state check
    }
}
