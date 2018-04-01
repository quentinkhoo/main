package seedu.investigapptor.model;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Represents a Password in PartTimeManger
 * Store password as hashCode
 */
public class Password {

    public static final String MESSAGE_PASSWORD_CONSTRAINTS =
            "Password should be at least 8 character and no spaces.";

    public static final String INITIAL_VALUE = "IV";

    /**
     * accept all password that do not have whitespaces and at least 8 characters.
     */
    public static final String PASSWORD_VALIDATION_REGEX = "^(?=\\S+$).{8,}$";

    private String passwordHash;

    /**
     * constructor for default password
     */
    public Password() {

    }

    /**
     * use this if hashcode is known
     * @param password
     */
    public Password(String password) {
        this.passwordHash = password;
    }

    /**
     * @param test
     * @return true if password is of correct format
     */
    public static boolean isValidPassword(String test) {
        return test.matches(PASSWORD_VALIDATION_REGEX);
    }

    /**
     * updates an original password to a new password
     */
    public void updatePassword(Password newPassword) {
        requireNonNull(newPassword);
        this.passwordHash = generatePasswordHash(newPassword.getPassword());
    }

    public String getPassword() {
        return this.passwordHash;
    }

    /**
     * Generate password hash given a password string
     * @param password
     * @return encodedHash in String
     */
    public static String generatePasswordHash(String password) {
        String encodedHash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(INITIAL_VALUE.getBytes());
            if (!isNull(password)) {
                byte[] byteHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                encodedHash = Base64.getEncoder().encodeToString(byteHash);
            }
        } catch (NoSuchAlgorithmException noSuchAlgoException) {
            System.out.println("Cannot generate hash: MessageDigest.getInstance");
        }
        return encodedHash;
    }

    /**
     * Checks if an inputPassword is the currentPassword
     * @param currentPassword
     * @param inputPassword
     */
    public static boolean isCorrectPassword(String currentPassword, String inputPassword) {
        return currentPassword.equals(inputPassword);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Password // instanceof handles nulls
                && this.passwordHash.equals(((Password) other).passwordHash)); // state check
    }

    @Override
    public int hashCode() {
        return passwordHash.hashCode();
    }
}
