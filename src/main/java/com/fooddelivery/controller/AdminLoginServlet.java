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
 * AdminLoginServlet — Handles admin authentication separately from customer login.
 * URL: /admin/login
 */
public class AdminLoginServlet extends HttpServlet {

    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Already an admin session → dashboard
        if (SessionUtil.isAdmin(session)) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/admin/adminLogin.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email    = request.getParameter("email");
        String password = request.getParameter("password");

        email    = (email    != null) ? email.trim()    : "";
        password = (password != null) ? password.trim() : "";

        User user = userService.loginUser(email, password);

        // Must be a valid user AND have ADMIN role
        if (user == null || !user.isAdmin()) {
            request.setAttribute("error", "Invalid admin credentials.");
            request.getRequestDispatcher("/WEB-INF/views/admin/adminLogin.jsp")
                   .forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("loggedUser", user);
        session.setAttribute("loggedAdmin", Boolean.TRUE);

        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
    }
}
