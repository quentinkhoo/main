package seedu.investigapptor.model.crimecase;

import java.util.List;
import java.util.function.Predicate;

//@@author pkaijun
/**
 * Tests that a {@code Person}'s {@code Tags} matches any of the keywords given.
 */
public class TagContainsKeywordsPredicate implements Predicate<CrimeCase> {
    private final List<String> keywords;

    public TagContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    /* Returns true if keywords matches with any element in the set of tags of a person
     */
    @Override
    public boolean test(CrimeCase crimecase) {
        return keywords.stream()
                .anyMatch(crimecase.getTagsRaw()::contains);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((TagContainsKeywordsPredicate) other).keywords)); // state check
    }
}
