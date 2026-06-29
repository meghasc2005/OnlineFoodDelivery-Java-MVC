<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Complete Payment — FoodExpress</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
  <style>
    body { background:linear-gradient(135deg,#f8f9fa,#e9ecef); min-height:100vh; }
    .payment-card { border:none; border-radius:1.2rem; box-shadow:0 8px 32px rgba(0,0,0,.12); }
    .order-info-card { background:linear-gradient(135deg,#2C3E50,#34495e); color:#fff; border-radius:1rem; padding:1.5rem; }
    .btn-pay { background:linear-gradient(135deg,#FF6B35,#e05520); border:none; color:#fff; font-weight:700; font-size:1.1rem; transition:transform .15s; }
    .btn-pay:hover { transform:translateY(-2px); color:#fff; }
    .btn-confirm { background:linear-gradient(135deg,#27ae60,#1e8449); border:none; color:#fff; font-weight:700; font-size:1.1rem; }
    .btn-confirm:hover { background:#1e8449; color:#fff; }
    .field-label { font-weight:600; color:#2C3E50; margin-bottom:.3rem; }
  </style>
</head>
<body>
  <%@ include file="/WEB-INF/views/common/navbar.jsp" %>

  <div class="container py-5">
    <div class="row justify-content-center">
      <div class="col-md-6">

        <%-- Order Summary --%>
        <div class="order-info-card mb-4">
          <h5 class="fw-bold mb-2">📦 Order Summary</h5>
          <div class="d-flex justify-content-between mb-1">
            <span>Order ID</span>
            <strong>#<c:out value="${order.orderId}"/></strong>
          </div>
          <div class="d-flex justify-content-between mb-1">
            <span>Payment Method</span>
            <strong><c:out value="${order.paymentMethod}"/></strong>
          </div>
          <hr style="border-color:rgba(255,255,255,.3);">
          <div class="d-flex justify-content-between fs-5">
            <span>Total Amount</span>
            <strong style="color:#F7C948;">
              ₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/>
            </strong>
          </div>
        </div>

        <%-- Payment Form --%>
        <div class="card payment-card p-4">
          <h5 class="fw-bold mb-4" style="color:#2C3E50;">🔐 Complete Payment</h5>

          <form method="post" action="${pageContext.request.contextPath}/payment"
                id="paymentForm" class="needs-validation" novalidate>
            <input type="hidden" name="orderId" value="${order.orderId}">
            <input type="hidden" name="paymentMethod" value="${order.paymentMethod}">

            <c:choose>
              <%-- COD --%>
              <c:when test="${order.paymentMethod == 'COD'}">
                <div class="alert alert-info d-flex align-items-center gap-2">
                  <span style="font-size:1.5rem;">💵</span>
                  <div>
                    You will pay
                    <strong>₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></strong>
                    in cash at the time of delivery.
                  </div>
                </div>
                <input type="hidden" name="transactionInput" value="COD">
                <button type="submit" class="btn btn-confirm w-100 py-3">
                  ✅ Confirm Order
                </button>
              </c:when>

              <%-- UPI --%>
              <c:when test="${order.paymentMethod == 'UPI'}">
                <div id="upi-field" class="mb-4">
                  <label class="field-label">📱 Enter Your UPI ID</label>
                  <input type="text" class="form-control form-control-lg" id="transactionInput"
                         name="transactionInput" placeholder="yourname@upi" required
                         pattern=".*@.*">
                  <div class="form-text">Example: yourname@okaxis or yourname@paytm</div>
                </div>
                <button type="submit" class="btn btn-pay w-100 py-3">
                  Pay ₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/> via UPI →
                </button>
              </c:when>

              <%-- CARD --%>
              <c:when test="${order.paymentMethod == 'CARD'}">
                <div id="card-fields">
                <div class="mb-3">
                  <label class="field-label">💳 Card Number</label>
                  <input type="text" class="form-control" id="cardNumber" name="cardNumber"
                         maxlength="19" placeholder="1234 5678 9012 3456"
                         oninput="formatCardNumber(this)">
                </div>
                <div class="row g-3 mb-4">
                  <div class="col-6">
                    <label class="field-label">Expiry Date</label>
                    <input type="text" class="form-control" name="expiry"
                           placeholder="MM/YY" maxlength="5">
                  </div>
                  <div class="col-6">
                    <label class="field-label">CVV</label>
                    <input type="password" class="form-control" name="cvv"
                           maxlength="3" placeholder="•••">
                  </div>
                </div>
                <input type="hidden" name="transactionInput" value="DUMMY_CARD">
                <button type="submit" class="btn btn-pay w-100 py-3">
                  Pay ₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/> →
                </button>
                </div><%-- end #card-fields --%>
              </c:when>

              <c:otherwise>
                <div class="alert alert-warning">Invalid payment method. Please go back.</div>
                <a href="${pageContext.request.contextPath}/orders" class="btn btn-secondary">My Orders</a>
              </c:otherwise>
            </c:choose>
          </form>
        </div>

        <div class="text-center mt-3">
          <small class="text-muted">🔒 Secure & Encrypted Payment</small>
        </div>
      </div>
    </div>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
  <script>
    function formatCardNumber(input) {
      let val = input.value.replace(/\D/g,'').substring(0,16);
      input.value = val.replace(/(.{4})/g,'$1 ').trim();
    }
  </script>
</body>
</html>
