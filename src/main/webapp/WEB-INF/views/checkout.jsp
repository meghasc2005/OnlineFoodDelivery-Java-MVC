<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Secure Checkout — FoodExpress</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="d-flex flex-column min-vh-100 bg-light">
  <%@ include file="/WEB-INF/views/common/navbar.jsp" %>

  <div class="page-header text-center py-5">
    <div class="container">
      <h1 class="fw-bold display-6 mb-1">Secure Checkout 🔐</h1>
      <p class="lead opacity-90 mb-0">Complete your feast order in just a few clicks</p>
    </div>
  </div>

  <div class="container py-5 flex-grow-1">

    <c:if test="${not empty error}">
      <div class="alert alert-danger alert-dismissible fade show shadow-sm rounded-4">
        <c:out value="${error}"/>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
      </div>
    </c:if>

    <div class="row g-4">
      <%-- Left Column: Address, Instructions, Payment --%>
      <div class="col-lg-7">
        <form method="post" action="${pageContext.request.contextPath}/checkout" id="checkoutForm">
          
          <%-- 1. Delivery Address Selection --%>
          <div class="card bg-white rounded-4 shadow-sm border-0 p-4 mb-4">
            <h5 class="fw-bold mb-3 d-flex align-items-center gap-2"><span>📍</span> Select Delivery Address</h5>
            
            <div class="d-flex flex-column gap-3 mb-3">
              <label class="border p-3 rounded-3 d-flex align-items-start gap-3 cursor-pointer bg-light">
                <input type="radio" name="addressSelect" value="saved" class="form-check-input mt-1 shadow-none" checked onclick="document.getElementById('customAddrBox').style.display='none'; document.getElementById('finalAddrInput').value='${userAddress}';">
                <div>
                  <span class="badge bg-dark small mb-1">🏠 Home / Default</span>
                  <p class="mb-0 small text-dark fw-semibold"><c:out value="${userAddress}"/></p>
                </div>
              </label>

              <label class="border p-3 rounded-3 d-flex align-items-start gap-3 cursor-pointer">
                <input type="radio" name="addressSelect" value="work" class="form-check-input mt-1 shadow-none" onclick="document.getElementById('customAddrBox').style.display='none'; document.getElementById('finalAddrInput').value='Tech Park, Tower B, 4th Floor, Bangalore';">
                <div>
                  <span class="badge bg-secondary small mb-1">💼 Work Office</span>
                  <p class="mb-0 small text-muted">Tech Park, Tower B, 4th Floor, Bangalore</p>
                </div>
              </label>

              <label class="border p-3 rounded-3 d-flex align-items-start gap-3 cursor-pointer">
                <input type="radio" name="addressSelect" value="other" class="form-check-input mt-1 shadow-none" onclick="document.getElementById('customAddrBox').style.display='block'; document.getElementById('finalAddrInput').value=document.getElementById('customAddrText').value;">
                <div>
                  <span class="badge bg-primary small mb-1">📍 Add New Address</span>
                  <p class="mb-0 small text-muted">Enter a custom location below</p>
                </div>
              </label>
            </div>

            <div id="customAddrBox" style="display:none;" class="mt-2">
              <textarea id="customAddrText" class="form-control rounded-3 small" rows="2" placeholder="House/Flat No, Landmark, Street Name..." onkeyup="document.getElementById('finalAddrInput').value=this.value;"></textarea>
            </div>

            <input type="hidden" name="deliveryAddress" id="finalAddrInput" value="${userAddress}">
          </div>

          <%-- 2. Delivery Instructions --%>
          <div class="card bg-white rounded-4 shadow-sm border-0 p-4 mb-4">
            <h6 class="fw-bold mb-3 d-flex align-items-center gap-2"><span>🛵</span> Delivery Partner Instructions</h6>
            <div class="d-flex flex-wrap gap-2">
              <label class="btn btn-outline-secondary btn-sm rounded-pill d-flex align-items-center gap-1 px-3">
                <input type="checkbox" name="instructions" value="Leave at door" class="form-check-input m-0 shadow-none"> Leave at door
              </label>
              <label class="btn btn-outline-secondary btn-sm rounded-pill d-flex align-items-center gap-1 px-3">
                <input type="checkbox" name="instructions" value="Avoid calling" class="form-check-input m-0 shadow-none"> Avoid calling
              </label>
              <label class="btn btn-outline-secondary btn-sm rounded-pill d-flex align-items-center gap-1 px-3">
                <input type="checkbox" name="instructions" value="Do not ring doorbell" class="form-check-input m-0 shadow-none"> Don't ring doorbell
              </label>
              <label class="btn btn-outline-secondary btn-sm rounded-pill d-flex align-items-center gap-1 px-3">
                <input type="checkbox" name="instructions" value="Pet at home" class="form-check-input m-0 shadow-none"> 🐕 Pet at home
              </label>
            </div>
          </div>

          <%-- 3. Payment Method --%>
          <div class="card bg-white rounded-4 shadow-sm border-0 p-4 mb-4">
            <h5 class="fw-bold mb-3 d-flex align-items-center gap-2"><span>💳</span> Select Payment Method</h5>
            
            <div class="row g-3">
              <div class="col-md-6">
                <label class="border p-3 rounded-4 d-flex align-items-center gap-3 h-100 cursor-pointer bg-light border-primary border-2">
                  <input type="radio" name="paymentMethod" value="UPI" class="form-check-input shadow-none" checked>
                  <div>
                    <strong class="d-block text-dark">📱 UPI Instant</strong>
                    <small class="text-muted">GPay, PhonePe, Paytm</small>
                  </div>
                </label>
              </div>
              <div class="col-md-6">
                <label class="border p-3 rounded-4 d-flex align-items-center gap-3 h-100 cursor-pointer">
                  <input type="radio" name="paymentMethod" value="CARD" class="form-check-input shadow-none">
                  <div>
                    <strong class="d-block text-dark">💳 Credit / Debit Card</strong>
                    <small class="text-muted">Visa, Mastercard, RuPay</small>
                  </div>
                </label>
              </div>
              <div class="col-md-6">
                <label class="border p-3 rounded-4 d-flex align-items-center gap-3 h-100 cursor-pointer">
                  <input type="radio" name="paymentMethod" value="WALLET" class="form-check-input shadow-none">
                  <div>
                    <strong class="d-block text-dark">💰 Express Wallet</strong>
                    <small class="text-muted">Balance: ₹500.00</small>
                  </div>
                </label>
              </div>
              <div class="col-md-6">
                <label class="border p-3 rounded-4 d-flex align-items-center gap-3 h-100 cursor-pointer">
                  <input type="radio" name="paymentMethod" value="COD" class="form-check-input shadow-none">
                  <div>
                    <strong class="d-block text-dark">💵 Cash on Delivery</strong>
                    <small class="text-muted">Pay cash or UPI at door</small>
                  </div>
                </label>
              </div>
            </div>
          </div>

          <button type="submit" class="btn btn-success w-100 rounded-pill py-3 fw-bold fs-4 shadow d-flex align-items-center justify-content-center gap-2">
            <span>🚀 Place Feast Order (₹<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/>)</span>
          </button>
        </form>
      </div>

      <%-- Right Column: Summary & Coupon --%>
      <div class="col-lg-5">
        <%-- Coupon Box --%>
        <div class="card border-0 shadow-sm rounded-4 mb-4 p-4 bg-white">
          <h6 class="fw-bold mb-3 d-flex align-items-center gap-2"><span>🏷️</span> Promo Coupon</h6>
          <c:if test="${not empty appliedCoupon}">
            <div class="alert alert-success d-flex justify-content-between align-items-center py-2 px-3 mb-2 rounded-pill small fw-semibold">
              <span>✨ Applied <strong>${appliedCoupon}</strong> (-₹<fmt:formatNumber value="${cartDiscount}" pattern="#,##0"/>)</span>
              <form method="post" action="${pageContext.request.contextPath}/cart" class="m-0">
                <input type="hidden" name="action" value="removeCoupon">
                <button type="submit" class="btn btn-sm btn-link text-danger text-decoration-none fw-bold p-0">REMOVE</button>
              </form>
            </div>
          </c:if>
          <form method="post" action="${pageContext.request.contextPath}/cart" class="d-flex gap-2 m-0">
            <input type="hidden" name="action" value="applyCoupon">
            <input type="text" name="code" class="form-control rounded-pill px-3 text-uppercase shadow-none small" placeholder="Have a coupon code?" required>
            <button type="submit" class="btn btn-dark rounded-pill px-4 fw-bold small">APPLY</button>
          </form>
        </div>

        <%-- Summary Card --%>
        <div class="card border-0 shadow-sm rounded-4 p-4 bg-white">
          <h5 class="fw-bold border-bottom pb-3 mb-3">Order Summary (${cartCount} Items)</h5>
          
          <div class="d-flex flex-column gap-2 mb-3 max-h-40 overflow-auto">
            <c:forEach var="item" items="${cartItems}">
              <div class="d-flex justify-content-between small">
                <span class="text-dark"><c:out value="${item.quantity}"/> x <c:out value="${item.food.name}"/></span>
                <span class="fw-semibold">₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></span>
              </div>
            </c:forEach>
          </div>

          <hr class="my-2">

          <div class="d-flex justify-content-between mb-2 text-secondary small">
            <span>Item Total</span>
            <span class="fw-semibold text-dark">₹<fmt:formatNumber value="${cartSubtotal}" pattern="#,##0.00"/></span>
          </div>
          <div class="d-flex justify-content-between mb-2 text-secondary small">
            <span>Delivery Fee</span>
            <span class="fw-semibold">${cartDeliveryFee == 0 ? '<span class=\"text-success fw-bold\">FREE</span>' : '₹'.concat(cartDeliveryFee)}</span>
          </div>
          <div class="d-flex justify-content-between mb-2 text-secondary small">
            <span>Platform Fee</span>
            <span class="fw-semibold text-dark">₹<fmt:formatNumber value="${cartPlatformFee}" pattern="#,##0.00"/></span>
          </div>
          <div class="d-flex justify-content-between mb-2 text-secondary small">
            <span>GST & Taxes (5%)</span>
            <span class="fw-semibold text-dark">₹<fmt:formatNumber value="${cartGst}" pattern="#,##0.00"/></span>
          </div>
          <c:if test="${cartDiscount > 0}">
            <div class="d-flex justify-content-between mb-2 text-success fw-bold small">
              <span>Coupon Discount</span>
              <span>- ₹<fmt:formatNumber value="${cartDiscount}" pattern="#,##0.00"/></span>
            </div>
          </c:if>

          <hr class="my-3">
          <div class="d-flex justify-content-between align-items-center fs-4 fw-bolder text-dark">
            <span>Grand Total</span>
            <span class="text-primary">₹<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/></span>
          </div>
          
          <div class="alert alert-info py-2 px-3 mt-4 mb-0 rounded-3 small text-center">
            🕒 Estimated Delivery Time: <strong>25 - 30 Mins</strong>
          </div>
        </div>
      </div>
    </div>
  </div>

  <footer class="footer mt-auto py-4">
    <div class="container text-center"><p class="mb-0 text-white-50">© <%= java.time.Year.now() %> FoodExpress</p></div>
  </footer>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>
