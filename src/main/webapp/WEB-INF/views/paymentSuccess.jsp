<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Payment Successful — FoodExpress</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
  <style>
    body { background:linear-gradient(135deg,#f0fdf4,#dcfce7); min-height:100vh; }
    .success-card { border:none; border-radius:1.5rem; box-shadow:0 20px 60px rgba(0,0,0,.12); }
    .success-icon { font-size:5rem; animation: bounceIn .6s ease; }
    @keyframes bounceIn {
      0%   { transform:scale(0); opacity:0; }
      60%  { transform:scale(1.15); }
      100% { transform:scale(1); opacity:1; }
    }
    .btn-track { background:linear-gradient(135deg,#FF6B35,#e05520); border:none; color:#fff; font-weight:700; }
    .btn-track:hover { color:#fff; transform:translateY(-2px); }
    .order-badge { background:#2C3E50; color:#fff; border-radius:2rem; padding:.4rem 1.2rem; font-size:1rem; }
  </style>
</head>
<body>
  <div class="container py-5">
    <div class="row justify-content-center mt-4">
      <div class="col-md-5 text-center">
        <div class="card success-card p-5">
          <div class="success-icon mb-3">✅</div>
          <h2 class="fw-bold" style="color:#27ae60;">Payment Successful!</h2>
          <p class="text-muted fs-5 mb-2">Your order has been placed successfully.</p>
          <div class="order-badge d-inline-block mb-3">
            Order #<c:out value="${order.orderId}"/>
          </div>
          <p class="text-muted mb-4">
            We're preparing your food and will deliver it soon! 🚀<br>
            <small>You'll receive your order at the provided address.</small>
          </p>
          <div class="d-grid gap-2">
            <a href="${pageContext.request.contextPath}/trackOrder?orderId=${order.orderId}"
               class="btn btn-track btn-lg">📍 Track My Order</a>
            <a href="${pageContext.request.contextPath}/home"
               class="btn btn-outline-secondary btn-lg">← Continue Shopping</a>
          </div>
        </div>
      </div>
    </div>
  </div>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>
