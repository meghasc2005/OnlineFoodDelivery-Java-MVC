# Online Food Delivery System — Deployment Checklist

> **Project:** OnlineFoodDelivery  
> **Stack:** Java 17 · Jakarta Servlets 6 · JSP/JSTL · JDBC · MySQL 8 · Apache Tomcat 10  
> **WAR Name:** `OnlineFoodDelivery.war`  
> **Context Root:** `/OnlineFoodDelivery`

---

## ✅ Pre-Build Requirements

- [ ] **Java 17** installed and on PATH
  ```bash
  java -version
  # Expected: openjdk version "17.x.x" or similar
  ```
- [ ] **Maven 3.8+** installed
  ```bash
  mvn -version
  # Expected: Apache Maven 3.8.x or higher
  ```
- [ ] **MySQL 8.x** server is running
  ```bash
  mysql -u root -p
  # Expected: MySQL prompt — Welcome to the MySQL monitor...
  ```
- [ ] **Apache Tomcat 10.x** downloaded and extracted
  - Download from: https://tomcat.apache.org/download-10.cgi
  - Extract to a known path (e.g. `/opt/tomcat10`)
- [ ] **VS Code** with **Extension Pack for Java** installed (optional but recommended)

---

## ✅ Database Setup

```bash
# Open MySQL CLI
mysql -u root -p
```

- [ ] Run the SQL script:
  ```sql
  source /path/to/OnlineFoodDelivery/food_delivery_db.sql;
  ```
- [ ] Verify database created:
  ```sql
  USE food_delivery_db;
  SHOW TABLES;
  -- Expected: 8 tables
  -- users, restaurants, food_items, cart, cart_items,
  -- orders, order_items, payments
  ```
- [ ] Verify seed data — users:
  ```sql
  SELECT COUNT(*) FROM users;
  -- Expected: 6
  ```
- [ ] Verify seed data — food items:
  ```sql
  SELECT COUNT(*) FROM food_items;
  -- Expected: 24
  ```
- [ ] Verify admin account exists:
  ```sql
  SELECT email, role FROM users WHERE role = 'ADMIN';
  -- Expected: admin@food.com | ADMIN
  -- Admin password: admin123
  ```
- [ ] Verify BCrypt hashed passwords:
  ```sql
  SELECT email, password FROM users LIMIT 3;
  -- All passwords should start with '$2a$12$'
  ```

---

## ✅ Project Configuration

- [ ] Open [`DBConnection.java`](src/main/java/com/fooddelivery/util/DBConnection.java)
- [ ] Confirm `DB_URL` value:
  ```java
  private static final String DB_URL =
      "jdbc:mysql://localhost:3306/food_delivery_db?useSSL=false&serverTimezone=UTC";
  ```
- [ ] Confirm `DB_USERNAME` is `"root"` (or your MySQL username)
- [ ] Confirm `DB_PASSWORD` matches your MySQL root password
- [ ] Confirm driver class:
  ```java
  Class.forName("com.mysql.cj.jdbc.Driver");
  ```

> **Note:** Change credentials before pushing to any public/shared environment.

---

## ✅ Maven Build

```bash
# Navigate to project root
cd "/path/to/MVC architecture/OnlineFoodDelivery"

# Clean and package
mvn clean package
```

- [ ] Build output ends with: `BUILD SUCCESS`
- [ ] WAR file generated at:
  ```
  target/OnlineFoodDelivery-1.0-SNAPSHOT.war
  ```
- [ ] If build fails, run with verbose logging:
  ```bash
  mvn clean package -X 2>&1 | tee build.log
  ```
- [ ] Common build errors:
  - `package jakarta.servlet does not exist` → Check `pom.xml` uses `jakarta.servlet-api` NOT `javax.servlet-api`
  - `cannot find symbol: BCrypt` → Ensure `jbcrypt` dependency is in `pom.xml`
  - Compilation errors → Check Java 17 source/target in `pom.xml` maven-compiler-plugin

---

## ✅ Tomcat Deployment

```bash
# Set TOMCAT variable (adjust path to your installation)
TOMCAT=/opt/tomcat10

# Copy the WAR file
cp target/OnlineFoodDelivery-1.0-SNAPSHOT.war $TOMCAT/webapps/OnlineFoodDelivery.war

# Start Tomcat
$TOMCAT/bin/startup.sh        # Linux / macOS
# OR
$TOMCAT\bin\startup.bat       # Windows
```

- [ ] Monitor Tomcat startup log:
  ```bash
  tail -f $TOMCAT/logs/catalina.out
  ```
- [ ] Look for success message:
  ```
  Deployment of web application archive [.../OnlineFoodDelivery.war] has finished in [NNNms]
  ```
- [ ] Open browser and navigate to:
  ```
  http://localhost:8080/OnlineFoodDelivery/
  ```
- [ ] Landing page (`index.jsp`) loads correctly
- [ ] No `ClassNotFoundException` or `NoClassDefFoundError` in logs

> **Tip:** If port 8080 is in use, edit `$TOMCAT/conf/server.xml` and change `Connector port="8080"` to another port.

---

## ✅ Functional Test Sequence

### Customer Flow

- [ ] Open `http://localhost:8080/OnlineFoodDelivery/` — landing page loads with hero section
- [ ] Click **Register** → fill all fields → submit → redirected to `/login?success=1`
- [ ] Login with new credentials → redirected to `/home`
- [ ] Home page shows list of 4 restaurants with images, cuisine badges, ratings
- [ ] Click **View Menu** on any restaurant → menu page loads with items grouped by category
- [ ] Add 2–3 items to cart — navbar cart badge updates to show count
- [ ] Navigate to `/cart` — items displayed with unit price, quantity, subtotal columns
- [ ] Update quantity on one item → quantity changes, subtotal recalculates
- [ ] Remove one item → item disappears from table
- [ ] Click **Proceed to Checkout** → checkout form appears
- [ ] Checkout shows: pre-filled delivery address, payment method selector, order summary
- [ ] Select **COD** → click **Place Order** → redirected to `/payment`
- [ ] Payment page shows COD info box → click **Confirm Order** → `/paymentSuccess`
- [ ] Success page shows animated checkmark + Order # + Track My Order button
- [ ] Navigate to `/orders` → new order shows with status **CONFIRMED**
- [ ] Click **Track** → order stepper highlights correct step
- [ ] Logout → redirected to `/login`

**Payment method tests:**
- [ ] Repeat order with **UPI**: enter any valid UPI ID (e.g. `test@paytm`) → success
- [ ] Repeat order with **CARD**: enter any 16-digit card number → success
- [ ] Test empty **UPI ID**: leave UPI field blank → HTML5 validation prevents submit
- [ ] Search test: type a food name in search bar → search results shown as food cards

### Admin Flow

- [ ] Navigate to `http://localhost:8080/OnlineFoodDelivery/admin/login`
- [ ] Login with: **Email:** `admin@food.com` / **Password:** `admin123`
- [ ] Admin dashboard shows correct counts: restaurants (≥4), food items (≥24), orders (≥0)
- [ ] Navigate to **Restaurants** → table lists all 4+ restaurants
- [ ] Add new restaurant → form submits → new row appears in table
- [ ] Click **Edit** on a restaurant → form pre-fills → save → change reflected in table
- [ ] Delete the newly added restaurant → row removed from table
- [ ] Navigate to **Foods** → table lists 24+ food items
- [ ] Add new food item to a restaurant → appears in table
- [ ] Edit the food item → price/name updated in table
- [ ] Delete the food item → removed from table
- [ ] Navigate to **Orders** → all placed orders listed
- [ ] Select a different status from the dropdown for an order → click ✓ → status badge updates
- [ ] Logout → redirected to `/login` (customer login, not admin login)

---

## ✅ Post-Deployment Verification

- [ ] No `500 Internal Server Error` pages during any flow
- [ ] All CSS loaded (colors, cards, navbar styled correctly)
- [ ] All JavaScript working (alerts auto-dismiss, card number formats, stepper animates)
- [ ] Cart badge count updates after adding/removing items
- [ ] Payment success/failure pages animate correctly (bounce / shake)
- [ ] Order stepper on trackOrder page correctly highlights the current step
- [ ] Admin delete confirms via dialog before proceeding
