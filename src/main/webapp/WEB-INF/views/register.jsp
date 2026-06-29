<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Register — FoodExpress</title>
  <meta name="description" content="Create your FoodExpress account and start ordering delicious food.">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
  <style>
    body {
      background: linear-gradient(135deg, #2C3E50 0%, #1a252f 100%);
      min-height: 100vh;
    }
    .card-register { border: none; border-radius: 1.2rem; box-shadow: 0 20px 60px rgba(0,0,0,0.25); }
    .btn-register {
      background: linear-gradient(135deg, #FF6B35, #e05520);
      border: none; color: #fff; font-weight: 600;
      transition: transform .15s, box-shadow .15s;
    }
    .btn-register:hover { transform: translateY(-2px); box-shadow: 0 6px 18px rgba(255,107,53,.4); color:#fff; }
    .section-header { color: #FF6B35; font-size: 1.1rem; font-weight: 600; border-bottom: 2px solid #FF6B35; padding-bottom: 4px; margin: 1rem 0 .8rem; }
  </style>
</head>
<body class="py-4">
  <nav class="navbar navbar-dark px-4 py-2" style="background:transparent; position:absolute; top:0; width:100%;">
    <span class="navbar-brand fw-bold fs-4" style="color:#FF6B35;">🍔 FoodExpress</span>
    <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-light btn-sm">Login</a>
  </nav>

  <div class="container py-5 mt-4">
    <div class="row justify-content-center">
      <div class="col-md-6 col-sm-10">

        <div class="text-center mb-4">
          <div style="font-size:2.5rem;">🍔</div>
          <h1 class="text-white fw-bold fs-2">Create Account</h1>
          <p class="text-white-50">Join FoodExpress and enjoy great food!</p>
        </div>

        <div class="card card-register p-4">

          <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
              <strong>⚠️</strong> <c:out value="${error}"/>
              <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
          </c:if>
          <c:if test="${not empty success}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
              <strong>✅</strong> <c:out value="${success}"/>
              <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
          </c:if>

          <form method="post" action="${pageContext.request.contextPath}/register"
                id="registerForm" novalidate>

            <div class="section-header">Account Details</div>

            <div class="form-floating mb-3">
              <input type="text" class="form-control" id="fullName" name="fullName"
                     placeholder="Full Name" required>
              <label for="fullName">👤 Full Name</label>
            </div>

            <div class="form-floating mb-3">
              <input type="email" class="form-control" id="email" name="email"
                     placeholder="Email" required>
              <label for="email">📧 Email Address</label>
            </div>

            <div class="row g-2 mb-3">
              <div class="col-6">
                <div class="form-floating">
                  <input type="password" class="form-control" id="password" name="password"
                         placeholder="Password" required minlength="6">
                  <label for="password">🔒 Password</label>
                </div>
              </div>
              <div class="col-6">
                <div class="form-floating">
                  <input type="password" class="form-control" id="confirmPassword"
                         name="confirmPassword" placeholder="Confirm" required>
                  <label for="confirmPassword">🔒 Confirm</label>
                </div>
              </div>
            </div>
            <div class="form-text text-muted mb-3">Minimum 6 characters.</div>

            <div class="section-header">Contact Details</div>

            <div class="form-floating mb-3">
              <input type="tel" class="form-control" id="phone" name="phone"
                     placeholder="Phone">
              <label for="phone">📞 Phone Number</label>
            </div>

            <div class="form-floating mb-4">
              <textarea class="form-control" id="address" name="address"
                        placeholder="Address" style="height:90px;"></textarea>
              <label for="address">📍 Delivery Address</label>
            </div>

            <button type="submit" class="btn btn-register w-100 py-2 fs-5">
              Create Account 🚀
            </button>
          </form>

          <hr class="my-3">
          <p class="text-center text-muted mb-0">
            Already have an account?
            <a href="${pageContext.request.contextPath}/login" class="fw-semibold"
               style="color:#FF6B35;">Login here</a>
          </p>
        </div>

      </div>
    </div>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>
