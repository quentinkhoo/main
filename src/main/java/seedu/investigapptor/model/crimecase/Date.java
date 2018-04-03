package seedu.investigapptor.model.crimecase;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.commons.util.AppUtil.checkArgument;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a CrimeCase's Start date in the Investigapptor.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class Date {

    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Input date must follow DD/MM/YYYY or D/M/YYYY format, and it should not be blank";

    public static final String DATE_VALIDATION_REGEX = "([0-9]*)/([0-9]*)/([0-9]*)";
    public static final String LARGEST_DATE = "12/12/3000";

    private static final int DOB_DAY_INDEX = 0;
    private static final int DOB_MONTH_INDEX = 1;
    private static final int DOB_YEAR_INDEX = 2;
    private static String[] dateProperties;
    private int day;
    private int month;
    private int year;

    public final String date;

    /**
     * Constructs a {@code date}.
     *
     * @param date A valid date.
     */
    public Date(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_DATE_CONSTRAINTS);
        this.date = date;
        setDateProperties(date);
    }

    /**
     * Using LocalDate to retrieve the current date according to the format dd/mm/yyyy
     * @return String todayDate
     */
    public static String getTodayDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String todayDate = now.format(formatter);

        return todayDate;
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidDate(String test) {
        if (isEmptyDate(test)) {
            return false;
        }

        if (!test.matches(DATE_VALIDATION_REGEX)) {
            return false;
        }

        String[] dateProperties = test.split("/");
        if (hasDateMonthYear((test))) {
            int testDay = Integer.parseInt(dateProperties[DOB_DAY_INDEX]);
            int testMonth = Integer.parseInt(dateProperties[DOB_MONTH_INDEX]);
            int testYear = Integer.parseInt(dateProperties[DOB_YEAR_INDEX]);

            try {
                LocalDate.of(testYear, testMonth, testDay);
                return true;
            } catch (DateTimeException dte) {
                return false;
            }
        } else {
            return false;
        }

    }

    /**
     * Returns true if a given string is empty or has only whitespaces
     */
    public static boolean isEmptyDate(String str) {
        return str.trim().isEmpty();
    }

    /**
     * Returns true if a given string has a day, month, year input
     */
    public static boolean hasDateMonthYear(String date) {
        String[] dateProperties = date.split("/");
        return dateProperties.length == 3;
    }

    public void setDateProperties(String date) {
        this.dateProperties = date.split("/");
        this.day = Integer.parseInt(dateProperties[DOB_DAY_INDEX]);
        this.month = Integer.parseInt(dateProperties[DOB_MONTH_INDEX]);
        this.year = Integer.parseInt(dateProperties[DOB_YEAR_INDEX]);
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDay() {
        return this.day;
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Date
                && this.date.equals(((Date) other).date));
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.day)
                .append(" ")
                .append(this.month)
                .append(" ")
                .append(this.year);
        return builder.toString();
    }
}
