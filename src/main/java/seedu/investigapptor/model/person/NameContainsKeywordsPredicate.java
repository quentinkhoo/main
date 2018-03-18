package seedu.investigapptor.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.investigapptor.commons.util.StringUtil;
import seedu.investigapptor.model.person.investigator.Investigator;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate implements Predicate<Investigator> {
    private final List<String> keywords;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Investigator investigator) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(investigator.getName().fullName, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((NameContainsKeywordsPredicate) other).keywords)); // state check
    }

}
