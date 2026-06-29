package com.fooddelivery.controller;

import com.fooddelivery.service.UserService;
import com.fooddelivery.service.impl.UserServiceImpl;
import com.fooddelivery.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * RegisterServlet — Handles new customer registration.
 * URL: /register
 */
public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (SessionUtil.isLoggedIn(session)) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/register.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve and null-safe trim all parameters
        String fullName       = request.getParameter("fullName");
        String email          = request.getParameter("email");
        String password       = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phone          = request.getParameter("phone");
        String address        = request.getParameter("address");

        fullName        = (fullName        != null) ? fullName.trim()        : "";
        email           = (email           != null) ? email.trim()           : "";
        password        = (password        != null) ? password.trim()        : "";
        confirmPassword = (confirmPassword != null) ? confirmPassword.trim() : "";
        phone           = (phone           != null) ? phone.trim()           : "";
        address         = (address         != null) ? address.trim()         : "";

        // Validate required fields
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "Full name, email, and password are required.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                   .forward(request, response);
            return;
        }

        // Password confirmation check
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                   .forward(request, response);
            return;
        }

        // Minimum password length
        if (password.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                   .forward(request, response);
            return;
        }

        // Attempt registration
        boolean success = userService.registerUser(fullName, email, password, phone, address);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/login?success=1");
        } else {
            request.setAttribute("error",
                    "Email already registered or registration failed. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                   .forward(request, response);
        }
    }
}
