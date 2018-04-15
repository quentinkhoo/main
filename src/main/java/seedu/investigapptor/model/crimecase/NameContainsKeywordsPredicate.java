package seedu.investigapptor.model.crimecase;

import java.util.List;
import java.util.function.Predicate;

import seedu.investigapptor.commons.util.StringUtil;
//@@author leowweiching
/**
 * Tests that a {@code CrimeCase}'s {@code CaseName} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate implements Predicate<CrimeCase> {
    private final List<String> keywords;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(CrimeCase crimeCase) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(crimeCase.getCaseName().crimeCaseName, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((NameContainsKeywordsPredicate) other).keywords)); // state check
    }

}
