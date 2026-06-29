package com.fooddelivery.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * PasswordUtil — BCrypt Password Hashing Utility
 *
 * <p>Provides static methods to hash plain-text passwords using BCrypt
 * with a work factor of 12, and to verify passwords against their
 * stored BCrypt hash. This class cannot be instantiated.</p>
 */
public class PasswordUtil {

    // ===== Private Constructor — Prevent Instantiation =====
    private PasswordUtil() {
        throw new UnsupportedOperationException(
                "PasswordUtil is a utility class and cannot be instantiated.");
    }

    /**
     * Hashes a plain-text password using BCrypt with work factor 12.
     *
     * @param plainPassword the plain-text password to hash; must not be null
     * @return the BCrypt-hashed password string
     * @throws IllegalArgumentException if {@code plainPassword} is null or empty
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException(
                    "Password must not be null or empty.");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    /**
     * Verifies a plain-text password against a stored BCrypt hash.
     *
     * @param plainPassword  the plain-text password candidate; may be null
     * @param hashedPassword the stored BCrypt hash; may be null
     * @return {@code true} if the plain-text password matches the hash;
     *         {@code false} if either argument is null or they do not match
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
