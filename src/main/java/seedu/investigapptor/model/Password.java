package seedu.investigapptor.model;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import seedu.investigapptor.logic.commands.exceptions.InvalidPasswordException;

/**
 * Represents a Password in PartTimeManger
 * Store password as hashCode
 */
public class Password {

    public static final String MESSAGE_PASSWORD_CONSTRAINTS =
            "Password should be at least 8 character and no spaces.";

    public static final String DEFAULT_PASSWORD = "password";
    public static final String initialValue = "IV";

    /**
     * accept all password that do not have whitespaces and at least 8 characters.
     */
    public static final String PASSWORD_VALIDATION_REGEX = "^(?=\\S+$).{8,}$";

    private String passwordHash;

    /**
     * constructor for default password
     */
    public Password() {
        createPasswordHash(DEFAULT_PASSWORD);
    }

    /**
     * use this if hashcode is known
     * @param passwordHash
     */
    public Password(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * @param test
     * @return true if password is of correct format
     */
    public static boolean isValidPassword(String test) {
        return test.matches(PASSWORD_VALIDATION_REGEX);
    }

    /**
     * check if passwordHash generated from the string is same as current passwordHash
     * @param inputPassword
     * @return true if same
     */
    public boolean isCorrectPassword(String inputPassword) {
        return passwordHash.equals(generatePasswordHash(inputPassword));
    }

    /**
     * Change password given a password
     * @return true if password is changed
     */
    public void updatePassword(String newPassword) throws InvalidPasswordException {
        if (isValidPassword(newPassword)) {
            this.passwordHash = generatePasswordHash(newPassword);
        } else {
            throw new InvalidPasswordException(MESSAGE_PASSWORD_CONSTRAINTS);
        }
    }

    /**
     * Change password given a password
     * @return true if password is changed
     */
    public void updatePassword(Password newPassword) {
        requireNonNull(newPassword);
        this.passwordHash = generatePasswordHash(newPassword.getPassword());
    }

    /**
     * Create passwordHash when password is entered in plain text
     * @param password
     */
    public void createPasswordHash(String password) {
        requireNonNull(password);
        this.passwordHash = generatePasswordHash(password);
    }

    public String getPassword() {
        return this.passwordHash;
    }

    /**
     * Generate passwordHash given a string password
     * @param password
     * @return passwordHash in String
     */
    public static String generatePasswordHash(String password) {
        String encodedHash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(initialValue.getBytes());
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