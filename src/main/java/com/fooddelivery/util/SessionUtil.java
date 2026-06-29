package com.fooddelivery.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.fooddelivery.model.User;
import java.io.IOException;

/**
 * SessionUtil — HTTP Session Helper Utility
 *
 * <p>Provides static convenience methods for reading session attributes,
 * checking authentication state, and enforcing access control by
 * redirecting unauthenticated or unauthorised requests. All servlet
 * handlers should delegate session checks to this class rather than
 * duplicating session-attribute name literals.</p>
 *
 * <p>Session attribute keys used:</p>
 * <ul>
 *   <li>{@code "loggedUser"}  — stores the logged-in {@link User} object</li>
 *   <li>{@code "loggedAdmin"} — stores {@link Boolean#TRUE} for admin sessions</li>
 * </ul>
 */
public class SessionUtil {

    // ===== Private Constructor — Prevent Instantiation =====
    private SessionUtil() {
        throw new UnsupportedOperationException(
                "SessionUtil is a utility class and cannot be instantiated.");
    }

    /**
     * Returns the {@link User} stored in the session, or {@code null} if
     * the session is null or no user attribute is present.
     *
     * @param session the HTTP session; may be null
     * @return the logged-in {@link User}, or {@code null}
     */
    public static User getLoggedUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (User) session.getAttribute("loggedUser");
    }

    /**
     * Returns {@code true} if a user is currently logged in.
     *
     * @param session the HTTP session; may be null
     * @return {@code true} if a valid {@link User} is found in the session
     */
    public static boolean isLoggedIn(HttpSession session) {
        return getLoggedUser(session) != null;
    }

    /**
     * Returns {@code true} if the current session belongs to an admin user.
     *
     * @param session the HTTP session; may be null
     * @return {@code true} if {@code "loggedAdmin"} is {@link Boolean#TRUE}
     */
    public static boolean isAdmin(HttpSession session) {
        if (session == null) {
            return false;
        }
        return Boolean.TRUE.equals(session.getAttribute("loggedAdmin"));
    }

    /**
     * Enforces customer login. If the user is not logged in, redirects to
     * {@code /login} and returns {@code false}. The calling servlet must
     * return immediately when this method returns {@code false}.
     *
     * @param req the HTTP request
     * @param res the HTTP response
     * @return {@code true} if the user is logged in; {@code false} after redirect
     * @throws IOException if the redirect fails
     */
    public static boolean requireLogin(HttpServletRequest req,
                                       HttpServletResponse res)
            throws IOException {
        HttpSession session = req.getSession(false);
        if (!isLoggedIn(session)) {
            res.sendRedirect(req.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    /**
     * Enforces admin login. If the current session does not have the admin
     * flag, redirects to {@code /admin/login} and returns {@code false}.
     * The calling servlet must return immediately when this method returns
     * {@code false}.
     *
     * @param req the HTTP request
     * @param res the HTTP response
     * @return {@code true} if the session is an admin session; {@code false} after redirect
     * @throws IOException if the redirect fails
     */
    public static boolean requireAdmin(HttpServletRequest req,
                                       HttpServletResponse res)
            throws IOException {
        HttpSession session = req.getSession(false);
        if (!isAdmin(session)) {
            res.sendRedirect(req.getContextPath() + "/admin/login");
            return false;
        }
        return true;
    }
}
