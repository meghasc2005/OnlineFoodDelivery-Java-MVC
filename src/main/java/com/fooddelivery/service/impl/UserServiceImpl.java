package com.fooddelivery.service.impl;

import com.fooddelivery.dao.UserDAO;
import com.fooddelivery.dao.impl.UserDAOImpl;
import com.fooddelivery.model.User;
import com.fooddelivery.service.UserService;
import com.fooddelivery.util.PasswordUtil;

import java.sql.SQLException;

/**
 * UserServiceImpl — Business logic implementation of {@link UserService}.
 *
 * <p>Handles user registration (with duplicate-email check and BCrypt hashing),
 * login (BCrypt verification), and profile updates. All {@link SQLException}
 * instances are caught here and logged; callers receive null/false.</p>
 */
public class UserServiceImpl implements UserService {

    // ===== DAO Dependency =====
    private final UserDAO userDAO = new UserDAOImpl();

    // ===== Service Method Implementations =====

    @Override
    public boolean registerUser(String fullName, String email, String password,
                                String phone, String address) {
        // 1. Input validation
        if (fullName == null || fullName.trim().isEmpty()) {
            return false;
        }
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        // 2. Basic email format check
        String trimmedEmail = email.trim();
        if (!trimmedEmail.contains("@") || !trimmedEmail.contains(".")) {
            return false;
        }

        try {
            // 3. Check for duplicate email
            User existing = userDAO.getUserByEmail(trimmedEmail);
            if (existing != null) {
                return false; // email already registered
            }

            // 4. Hash the password
            String hashedPassword = PasswordUtil.hashPassword(password);

            // 5. Build the User object
            User newUser = new User();
            newUser.setFullName(fullName.trim());
            newUser.setEmail(trimmedEmail);
            newUser.setPassword(hashedPassword);
            newUser.setPhone(phone != null ? phone.trim() : "");
            newUser.setAddress(address != null ? address.trim() : "");
            newUser.setRole("CUSTOMER");

            // 6. Persist and return result
            return userDAO.registerUser(newUser);

        } catch (SQLException e) {
            System.err.println("[UserServiceImpl] registerUser error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User loginUser(String email, String password) {
        // 1. Null validation
        if (email == null || email.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return null;
        }

        try {
            // 2. Fetch user by email
            User user = userDAO.getUserByEmail(email.trim());

            // 3. User not found
            if (user == null) {
                return null;
            }

            // 4. BCrypt password verification
            if (PasswordUtil.checkPassword(password, user.getPassword())) {
                return user;
            }

            // 5. Wrong password
            return null;

        } catch (SQLException e) {
            System.err.println("[UserServiceImpl] loginUser error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateProfile(User user) {
        if (user == null || user.getUserId() <= 0) {
            return false;
        }
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            return false;
        }

        try {
            return userDAO.updateUser(user);
        } catch (SQLException e) {
            System.err.println("[UserServiceImpl] updateProfile error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
