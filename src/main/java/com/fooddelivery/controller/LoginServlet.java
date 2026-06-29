package com.fooddelivery.controller;

import com.fooddelivery.model.User;
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
 * LoginServlet — Handles customer and admin login.
 * URL: /login
 */
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Redirect already-authenticated users
        if (SessionUtil.isAdmin(session)) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }
        if (SessionUtil.isLoggedIn(session)) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Show success message if redirected from registration
        String success = request.getParameter("success");
        if ("1".equals(success)) {
            request.setAttribute("success", "Registration successful! Please login.");
        }

        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email    = request.getParameter("email");
        String password = request.getParameter("password");

        // Null-safe trim
        email    = (email    != null) ? email.trim()    : "";
        password = (password != null) ? password.trim() : "";

        // Validate presence
        if (email.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "Email and password are required.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                   .forward(request, response);
            return;
        }

        // Authenticate
        User user = userService.loginUser(email, password);
        if (user == null) {
            request.setAttribute("error", "Invalid email or password.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                   .forward(request, response);
            return;
        }

        // Store in session
        HttpSession session = request.getSession();
        session.setAttribute("loggedUser", user);

        // Route based on role
        if (user.isAdmin()) {
            session.setAttribute("loggedAdmin", true);
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}
