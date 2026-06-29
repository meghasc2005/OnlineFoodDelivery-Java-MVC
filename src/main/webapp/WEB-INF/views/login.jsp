<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="jakarta.tags.core" prefix="c" %>
    <%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
      <!DOCTYPE html>
      <html lang="en">

      <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login — FoodExpress</title>
        <meta name="description" content="Sign in to FoodExpress — the fastest food delivery platform.">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
        <style>
          body {
            background: linear-gradient(135deg, #2C3E50 0%, #1a252f 100%);
            min-height: 100vh;
            display: flex;
            flex-direction: column;
          }

          .brand-logo {
            color: #FF6B35;
            font-size: 2.5rem;
          }

          .card-login {
            border: none;
            border-radius: 1.2rem;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.25);
          }

          .btn-primary-custom {
            background: linear-gradient(135deg, #FF6B35, #e05520);
            border: none;
            color: #fff;
            font-weight: 600;
            transition: transform .15s, box-shadow .15s;
          }

          .btn-primary-custom:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 18px rgba(255, 107, 53, .4);
            color: #fff;
          }
        </style>
      </head>

      <body>
        <%-- Minimal top nav --%>
          <nav class="navbar navbar-dark px-4 py-2" style="background:transparent;">
            <span class="navbar-brand fw-bold fs-4" style="color:#FF6B35;">🍔 FoodExpress</span>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-outline-light btn-sm">Register</a>
          </nav>

          <div class="container flex-grow-1 d-flex align-items-center justify-content-center py-5">
            <div class="col-md-4 col-sm-10 col-12">

              <div class="text-center mb-4">
                <div class="brand-logo">🍔</div>
                <h1 class="text-white fw-bold fs-2">Welcome Back</h1>
                <p class="text-white-50">Sign in to continue your food journey</p>
              </div>

              <div class="card card-login p-4">

                <%-- Error Alert --%>
                  <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                      <strong>⚠️</strong>
                      <c:out value="${error}" />
                      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                  </c:if>

                  <%-- Success Alert --%>
                    <c:if test="${not empty success}">
                      <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <strong>✅</strong>
                        <c:out value="${success}" />
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                      </div>
                    </c:if>

                    <form method="post" action="${pageContext.request.contextPath}/login" id="loginForm" novalidate>
                      <%-- Email --%>
                        <div class="form-floating mb-3">
                          <input type="email" class="form-control" id="email" name="email"
                            placeholder="name@example.com" required>
                          <label for="email">📧 Email address</label>
                        </div>

                        <%-- Password --%>
                          <div class="form-floating mb-4">
                            <input type="password" class="form-control" id="password" name="password"
                              placeholder="Password" required>
                            <label for="password">🔒 Password</label>
                          </div>

                          <button type="submit" class="btn btn-primary-custom w-100 py-2 fs-5">
                            Sign In →
                          </button>
                    </form>

                    <hr class="my-4">
                    <p class="text-center text-muted mb-0">
                      Don't have an account?
                      <a href="${pageContext.request.contextPath}/register" class="fw-semibold"
                        style="color:#FF6B35;">Register here</a>
                    </p>
              </div>
            </div>
          </div>

          <footer class="text-center text-white-50 py-3">
            © <%= java.time.Year.now() %> FoodExpress
          </footer>

          <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
          <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
      </body>

      </html>