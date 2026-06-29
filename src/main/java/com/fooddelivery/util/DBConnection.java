package com.fooddelivery.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection — Database Connection Utility
 *
 * <p>Provides a centralized, static factory method for obtaining JDBC
 * connections to the food_delivery_db MySQL database. Each call to
 * {@link #getConnection()} returns a fresh {@link Connection} object.
 * Callers are responsible for closing the connection (preferably via
 * try-with-resources) after use.</p>
 *
 * <p>This class is not meant to be instantiated. Use only the static method.</p>
 */
public class DBConnection {

    // ===== Connection Constants =====
    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/food_delivery_db"
            + "?useSSL=false&allowPublicKeyRetrieval=true";

    private static final String DB_USERNAME = "root";

    private static final String DB_PASSWORD = "Mysql@123";

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    // ===== Driver Registration (Static Initializer) =====
    static {
        try {
            Class.forName(DB_DRIVER);
            System.out.println("Database Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("FATAL: MySQL JDBC Driver not found — "
                    + e.getMessage());
        }
    }

    // ===== Private Constructor — Prevent Instantiation =====
    private DBConnection() {
        throw new UnsupportedOperationException(
                "DBConnection is a utility class and cannot be instantiated.");
    }

    /**
     * Returns a fresh JDBC {@link Connection} to food_delivery_db.
     *
     * <p>Always use this inside a try-with-resources block to ensure
     * the connection is properly closed after use.</p>
     *
     * @return a new {@link Connection} instance
     * @throws SQLException if a database access error occurs or the URL is invalid
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }
}
