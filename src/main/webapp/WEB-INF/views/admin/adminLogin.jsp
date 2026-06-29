<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin Login — FoodExpress</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
  <style>
    body { background:#1a252f; min-height:100vh; display:flex; align-items:center; justify-content:center; }
    .admin-card { border:none; border-radius:1.2rem; box-shadow:0 20px 60px rgba(0,0,0,.5); max-width:400px; width:100%; }
    .shield-icon { font-size:3.5rem; }
    .btn-admin { background:linear-gradient(135deg,#e74c3c,#c0392b); border:none; color:#fff; font-weight:700; }
    .btn-admin:hover { background:#c0392b; color:#fff; }
  </style>
</head>
<body>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-md-5">
        <div class="card admin-card p-5">
          <div class="text-center mb-4">
            <div class="shield-icon">🛡️</div>
            <h2 class="fw-bold mt-2" style="color:#2C3E50;">Admin Login</h2>
            <p class="text-muted">Restricted access — authorized personnel only</p>
          </div>

          <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show">
              <strong>⚠️</strong> <c:out value="${error}"/>
              <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
          </c:if>

          <form method="post" action="${pageContext.request.contextPath}/admin/login">
            <div class="form-floating mb-3">
              <input type="email" class="form-control" id="email" name="email"
                     placeholder="admin@example.com" required>
              <label for="email">📧 Admin Email</label>
            </div>
            <div class="form-floating mb-4">
              <input type="password" class="form-control" id="password" name="password"
                     placeholder="Password" required>
              <label for="password">🔒 Password</label>
            </div>
            <button type="submit" class="btn btn-admin w-100 py-2 fs-5">
              Login as Admin →
            </button>
          </form>

          <hr class="my-4">
          <div class="text-center">
            <a href="${pageContext.request.contextPath}/login" class="text-muted small">
              ← Back to main site
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>
