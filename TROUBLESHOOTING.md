# Online Food Delivery System — Troubleshooting Guide

> Use this guide to diagnose and fix common issues after building or deploying the application.

---

## Issue 1 — `ClassNotFoundException: com.mysql.cj.jdbc.Driver`

| Field | Detail |
|-------|--------|
| **Symptom** | Tomcat logs: `java.lang.ClassNotFoundException: com.mysql.cj.jdbc.Driver` on first DB call |
| **Cause** | MySQL Connector/J JAR is not bundled in the WAR's `WEB-INF/lib` |
| **Fix** | Verify `pom.xml` has the MySQL dependency with `<scope>` set to `compile` (not `provided`): |

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.0.33</version>
    <!-- Do NOT use scope=provided — must be bundled in WEB-INF/lib -->
</dependency>
```

Then run `mvn clean package` and redeploy.

---

## Issue 2 — `NoClassDefFoundError: org/mindrot/jbcrypt/BCrypt`

| Field | Detail |
|-------|--------|
| **Symptom** | `java.lang.NoClassDefFoundError: org/mindrot/jbcrypt/BCrypt` at login or registration |
| **Cause** | The `jbcrypt` library is missing from the WAR's `WEB-INF/lib` |
| **Fix** | Ensure `pom.xml` contains: |

```xml
<dependency>
    <groupId>org.mindrot</groupId>
    <artifactId>jbcrypt</artifactId>
    <version>0.4</version>
</dependency>
```

Rebuild with `mvn clean package`. Verify `WEB-INF/lib/jbcrypt-0.4.jar` is in the expanded WAR.

---

## Issue 3 — `jakarta.servlet.http.HttpServlet` Class Not Found (Tomcat 9 / javax)

| Field | Detail |
|-------|--------|
| **Symptom** | `java.lang.ClassNotFoundException: jakarta.servlet.http.HttpServlet` or Tomcat refuses to load servlets |
| **Cause** | Deploying to Tomcat 9 (which uses `javax.servlet`) instead of Tomcat 10 (`jakarta.servlet`) |
| **Fix** | Use **Apache Tomcat 10.x** — download from https://tomcat.apache.org/download-10.cgi |

> All servlet imports in this project are `jakarta.servlet.*`. Tomcat 9 uses `javax.servlet.*` and is **incompatible**.

Also ensure `pom.xml` declares:
```xml
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.0.0</version>
    <scope>provided</scope>
</dependency>
```

---

## Issue 4 — JSTL Tags Not Rendering (Raw `<c:forEach>` Text Displayed)

| Field | Detail |
|-------|--------|
| **Symptom** | JSP pages show literal text like `<c:forEach var="r" items="${restaurants}">` instead of rendered output |
| **Cause A** | Missing `<%@ taglib uri="jakarta.tags.core" prefix="c" %>` directive at top of JSP |
| **Cause B** | JSTL JAR not present in WAR's `WEB-INF/lib` |
| **Fix** | Verify `pom.xml` has the correct JSTL dependency for Jakarta EE: |

```xml
<dependency>
    <groupId>org.glassfish.web</groupId>
    <artifactId>jakarta.servlet.jsp.jstl</artifactId>
    <version>3.0.1</version>
</dependency>
<dependency>
    <groupId>jakarta.servlet.jsp.jstl</groupId>
    <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
    <version>3.0.0</version>
</dependency>
```

> **Do NOT** use `javax.servlet.jsp.jstl` — that targets Tomcat 9.

---

## Issue 5 — 404 on All URLs Except `index.jsp`

| Field | Detail |
|-------|--------|
| **Symptom** | `/login`, `/home`, `/cart` etc. all return 404; only root URL works |
| **Cause** | `web.xml` servlet mappings not loaded, or WAR deployed to wrong path |
| **Fix A** | Confirm WAR was renamed correctly: file must be `OnlineFoodDelivery.war` in `webapps/` |
| **Fix B** | Check Tomcat log for any XML parsing errors in `web.xml` |
| **Fix C** | Ensure `<web-app version="6.0">` in `web.xml` matches Tomcat 10 (Jakarta EE 10) schema |

```bash
tail -100 $TOMCAT/logs/catalina.out | grep -i "error\|exception\|OnlineFoodDelivery"
```

---

## Issue 6 — `NullPointerException` in Servlet (`getParameter` Returns `null`)

| Field | Detail |
|-------|--------|
| **Symptom** | `NullPointerException` on line like `String email = request.getParameter("email").trim()` |
| **Cause** | Form field name in HTML does not match `getParameter("fieldName")` — typo or missing `name` attribute |
| **Fix** | Every `getParameter()` call in this project includes a null guard: |

```java
String email = request.getParameter("email");
email = (email != null) ? email.trim() : "";
```

Check the corresponding JSP `<input name="...">` attribute matches exactly.

---

## Issue 7 — SQL `Connection Refused` (MySQL Not Running or Wrong Port)

| Field | Detail |
|-------|--------|
| **Symptom** | `java.sql.SQLException: Communications link failure` or `Connection refused to host: localhost:3306` |
| **Cause A** | MySQL server is not running |
| **Cause B** | MySQL is running on a non-default port |
| **Fix A** | Start MySQL: `sudo systemctl start mysql` (Linux) or start via Services (Windows) |
| **Fix B** | Update `DB_URL` in `DBConnection.java` to the correct port, e.g. `localhost:3307` |

```java
private static final String DB_URL =
    "jdbc:mysql://localhost:3306/food_delivery_db?useSSL=false&serverTimezone=UTC";
```

---

## Issue 8 — BCrypt Hash Mismatch (Login Always Fails)

| Field | Detail |
|-------|--------|
| **Symptom** | Login always returns "Invalid email or password" even with correct credentials |
| **Cause A** | Plain-text passwords stored in DB instead of BCrypt hashes (SQL script not run properly) |
| **Cause B** | `PasswordUtil.checkPassword(plaintext, hash)` argument order reversed |
| **Fix A** | Verify DB passwords: `SELECT password FROM users LIMIT 1;` — must start with `$2a$12$` |
| **Fix B** | Verify `PasswordUtil` call order — plaintext must be FIRST argument: |

```java
// CORRECT:
BCrypt.checkpw(plainTextPassword, storedHash);

// WRONG (reversed):
BCrypt.checkpw(storedHash, plainTextPassword);
```

---

## Issue 9 — Session Lost Between Requests (User Logged Out Unexpectedly)

| Field | Detail |
|-------|--------|
| **Symptom** | User is logged in but immediately redirected to `/login` on next request |
| **Cause A** | Browser has cookies disabled — session cannot persist without the `JSESSIONID` cookie |
| **Cause B** | Session timeout is too short (`web.xml` `<session-timeout>` value) |
| **Fix A** | Enable cookies in the browser |
| **Fix B** | Increase timeout in `web.xml`: |

```xml
<session-config>
    <session-timeout>60</session-timeout>  <!-- minutes -->
</session-config>
```

---

## Issue 10 — Cart Always Shows Empty

| Field | Detail |
|-------|--------|
| **Symptom** | Items added to cart but `/cart` always shows "Your cart is empty" |
| **Cause A** | `cart` table row not created for user before `cart_items` INSERT |
| **Cause B** | `CartDAOImpl.addItemToCart()` fails silently (FK constraint on `cart_id`) |
| **Fix** | `CartServiceImpl.addToCart()` auto-creates the cart if none exists: |

```java
Cart cart = cartDAO.getCartByUserId(userId);
int cartId;
if (cart == null) {
    cartId = cartDAO.createCart(userId);   // <-- creates the row first
} else {
    cartId = cart.getCartId();
}
cartDAO.addItemToCart(cartId, foodId, quantity);
```

Check `System.err` in Tomcat logs for any `SQLException` being swallowed.

---

## Issue 11 — WAR Not Deploying (Tomcat Log Shows Exception on Startup)

| Field | Detail |
|-------|--------|
| **Symptom** | `catalina.out` shows `SEVERE: Context [/OnlineFoodDelivery] startup failed` |
| **Cause A** | Syntax error in `web.xml` |
| **Cause B** | Duplicate `<servlet-name>` in `web.xml` |
| **Cause C** | A Servlet class not found (class name typo in `web.xml`) |
| **Fix** | Read the full stack trace in `catalina.out` for the exact root cause: |

```bash
grep -A 20 "SEVERE" $TOMCAT/logs/catalina.out
```

Ensure all 17 servlet class names in `web.xml` exactly match the Java class names in `com.fooddelivery.controller`.

---

## Issue 12 — CSS / JS Not Loading (404 on Assets)

| Field | Detail |
|-------|--------|
| **Symptom** | Page loads without styles; browser console shows `404` for `/OnlineFoodDelivery/assets/css/style.css` |
| **Cause** | Asset files not in the correct webapp path or `${pageContext.request.contextPath}` not used |
| **Fix A** | Confirm files exist at: `src/main/webapp/assets/css/style.css` and `src/main/webapp/assets/js/main.js` |
| **Fix B** | All JSP links use context path: |

```jsp
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
<script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
```

> Never use relative paths like `../assets/css/style.css` in JSPs — they break with URL rewriting.

---

## Issue 13 — Admin Cannot Login (Role Check Failing)

| Field | Detail |
|-------|--------|
| **Symptom** | Admin credentials correct but shown "Invalid admin credentials" |
| **Cause A** | User's `role` column in DB is `'admin'` (lowercase) instead of `'ADMIN'` |
| **Cause B** | `User.isAdmin()` method checks wrong casing |
| **Fix A** | Verify the DB seed value: |

```sql
SELECT email, role FROM users WHERE email = 'admin@food.com';
-- role must be: ADMIN (uppercase)
```

| **Fix B** | Verify `User.isAdmin()` method: |

```java
public boolean isAdmin() {
    return "ADMIN".equals(this.role);   // must match DB value exactly
}
```

---

## Issue 14 — UPI Payment Always Fails (transactionInput Empty)

| Field | Detail |
|-------|--------|
| **Symptom** | UPI payment always redirects to `/paymentFailure` even with a valid UPI ID entered |
| **Cause** | `payment.jsp` UPI field `name="transactionInput"` is missing or mistyped, so `request.getParameter("transactionInput")` returns `null` |
| **Fix** | Verify the UPI input field in `payment.jsp`: |

```jsp
<input type="text" class="form-control" 
       name="transactionInput"          <%-- exact name required --%>
       id="transactionInput"
       placeholder="yourname@upi"
       required pattern=".*@.*">
```

And verify `PaymentServlet.doPost()` reads:
```java
String txnInput = request.getParameter("transactionInput");
```

Also verify `PaymentServiceImpl` branches on `"UPI"` (uppercase) — method parameter `method` must be trimmed and uppercased before the switch.
