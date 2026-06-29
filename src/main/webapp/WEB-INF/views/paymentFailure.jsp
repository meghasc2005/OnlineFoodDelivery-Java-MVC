<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Payment Failed — FoodExpress</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
  <style>
    body { background:linear-gradient(135deg,#fef2f2,#fee2e2); min-height:100vh; }
    .fail-card { border:none; border-radius:1.5rem; box-shadow:0 20px 60px rgba(0,0,0,.12); }
    .fail-icon { font-size:5rem; animation:shake .5s ease; }
    @keyframes shake {
      0%,100%{ transform:translateX(0); }
      20%    { transform:translateX(-10px); }
      40%    { transform:translateX(10px); }
      60%    { transform:translateX(-6px); }
      80%    { transform:translateX(6px); }
    }
    .btn-retry { background:linear-gradient(135deg,#FF6B35,#e05520); border:none; color:#fff; font-weight:700; }
    .btn-retry:hover { color:#fff; transform:translateY(-2px); }
  </style>
</head>
<body>
  <div class="container py-5">
    <div class="row justify-content-center mt-4">
      <div class="col-md-5 text-center">
        <div class="card fail-card p-5">
          <div class="fail-icon mb-3">❌</div>
          <h2 class="fw-bold text-danger">Payment Failed</h2>
          <p class="text-muted fs-5 mb-2">Something went wrong with your payment.</p>
          <p class="text-muted mb-4">Please check your payment details and try again.</p>
          <div class="d-grid gap-2">
            <a href="${pageContext.request.contextPath}/payment?orderId=${order.orderId}"
               class="btn btn-retry btn-lg">🔄 Retry Payment</a>
            <a href="${pageContext.request.contextPath}/orders"
               class="btn btn-outline-secondary btn-lg">📦 Go to My Orders</a>
          </div>
        </div>
      </div>
    </div>
  </div>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>
