<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>My Cart — FoodExpress</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="d-flex flex-column min-vh-100 bg-light">
  <%@ include file="/WEB-INF/views/common/navbar.jsp" %>

  <div class="page-header text-center py-5">
    <div class="container">
      <h1 class="fw-bold display-6 mb-1">My Food Cart 🛒</h1>
      <p class="lead opacity-90 mb-0">Review your delicacies and offers before checkout</p>
    </div>
  </div>

  <div class="container py-5 flex-grow-1">

    <c:if test="${not empty error}">
      <div class="alert alert-danger alert-dismissible fade show shadow-sm rounded-4">
        <c:out value="${error}"/>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
      </div>
    </c:if>

    <c:choose>
      <c:when test="${empty cartItems}">
        <div class="empty-state-box my-4 p-5 bg-white rounded-5 shadow-sm text-center mx-auto" style="max-width: 600px;">
          <div class="display-1 mb-3">🛒</div>
          <h3 class="fw-bold text-dark mt-2">Your Cart Feels Super Light</h3>
          <p class="text-muted mb-4">There is nothing in your cart yet. Explore our restaurant outlets and add some yummy dishes!</p>
          <a href="${pageContext.request.contextPath}/restaurants" class="btn btn-primary px-5 py-3 rounded-pill fw-bold fs-5 shadow">🍕 Browse Restaurants & Order</a>
        </div>
      </c:when>
      <c:otherwise>
        <div class="row g-4">
          <%-- Left Column: Items --%>
          <div class="col-lg-7">
            <div class="card bg-white rounded-4 shadow-sm border-0 p-4 mb-4">
              <div class="d-flex justify-content-between align-items-center mb-3 pb-2 border-bottom">
                <h5 class="fw-bold mb-0">Order Items (${cartCount})</h5>
                <form method="post" action="${pageContext.request.contextPath}/cart" class="m-0" onsubmit="return confirm('Clear entire cart?');">
                  <input type="hidden" name="action" value="clearAndAdd">
                  <input type="hidden" name="foodId" value="-1">
                  <input type="hidden" name="quantity" value="0">
                </form>
              </div>

              <div class="d-flex flex-column gap-3">
                <c:forEach var="item" items="${cartItems}">
                  <div class="d-flex align-items-center justify-content-between p-3 rounded-3 bg-light border">
                    <div class="d-flex align-items-center gap-3">
                      <span class="fs-4">${item.food.veg ? '🟢' : '🔴'}</span>
                      <div>
                        <h6 class="fw-bold mb-1 text-dark">${item.food.name}</h6>
                        <span class="text-muted small">₹<fmt:formatNumber value="${item.food.price}" pattern="#,##0.00"/> each</span>
                      </div>
                    </div>

                    <div class="d-flex align-items-center gap-4">
                      <%-- Qty + / - --%>
                      <div class="d-flex align-items-center bg-white border rounded-pill shadow-sm px-1 py-1">
                        <form method="post" action="${pageContext.request.contextPath}/cart" class="m-0">
                          <input type="hidden" name="action" value="update">
                          <input type="hidden" name="cartItemId" value="${item.cartItemId}">
                          <input type="hidden" name="quantity" value="${item.quantity - 1}">
                          <button type="submit" class="btn btn-sm btn-link text-dark text-decoration-none fw-bold p-0 px-2">-</button>
                        </form>
                        <span class="fw-bold px-2 small">${item.quantity}</span>
                        <form method="post" action="${pageContext.request.contextPath}/cart" class="m-0">
                          <input type="hidden" name="action" value="update">
                          <input type="hidden" name="cartItemId" value="${item.cartItemId}">
                          <input type="hidden" name="quantity" value="${item.quantity + 1}">
                          <button type="submit" class="btn btn-sm btn-link text-dark text-decoration-none fw-bold p-0 px-2">+</button>
                        </form>
                      </div>

                      <span class="fw-bold fs-6 text-dark" style="width: 80px; text-align: right;">₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></span>

                      <form method="post" action="${pageContext.request.contextPath}/cart" class="m-0">
                        <input type="hidden" name="action" value="remove">
                        <input type="hidden" name="cartItemId" value="${item.cartItemId}">
                        <button type="submit" class="btn btn-sm text-danger border-0 p-1" title="Remove Item">🗑️</button>
                      </form>
                    </div>
                  </div>
                </c:forEach>
              </div>
            </div>

            <div class="d-flex justify-content-start">
              <a href="${pageContext.request.contextPath}/restaurants" class="btn btn-outline-secondary rounded-pill px-4 py-2 fw-semibold">← Add More Items</a>
            </div>
          </div>

          <%-- Right Column: Offers & Summary --%>
          <div class="col-lg-5">
            <%-- Coupons --%>
            <div class="card border-0 shadow-sm rounded-4 mb-4 p-4 bg-white">
              <h6 class="fw-bold mb-3 d-flex align-items-center gap-2"><span>🎁</span> Offers & Coupons</h6>
              <c:if test="${not empty appliedCoupon}">
                <div class="alert alert-success d-flex justify-content-between align-items-center py-2 px-3 mb-3 rounded-pill small fw-semibold border border-success border-opacity-50">
                  <span>✨ Applied <strong>${appliedCoupon}</strong> (-₹<fmt:formatNumber value="${cartDiscount}" pattern="#,##0"/>)</span>
                  <form method="post" action="${pageContext.request.contextPath}/cart" class="m-0">
                    <input type="hidden" name="action" value="removeCoupon">
                    <button type="submit" class="btn btn-sm btn-link text-danger text-decoration-none fw-bold p-0">REMOVE</button>
                  </form>
                </div>
              </c:if>
              <form method="post" action="${pageContext.request.contextPath}/cart" class="d-flex gap-2 m-0 mb-3">
                <input type="hidden" name="action" value="applyCoupon">
                <input type="text" name="code" class="form-control rounded-pill px-3 text-uppercase shadow-none small border-secondary" placeholder="Enter promo code" required>
                <button type="submit" class="btn btn-dark rounded-pill px-4 fw-bold small">APPLY</button>
              </form>
              <div class="d-flex flex-column gap-2 pt-2 border-top">
                <span class="text-muted small fw-semibold">Available Promos:</span>
                <div class="d-flex gap-2 flex-wrap">
                  <c:forEach var="cp" items="${availableCoupons}">
                    <span class="badge bg-light text-dark border p-2 rounded-3 text-start cursor-pointer" onclick="document.querySelector('input[name=code]').value='${cp.code}';" style="cursor:pointer;">
                      <strong>🏷️ ${cp.code}</strong><br><small class="text-muted fw-normal">${cp.description}</small>
                    </span>
                  </c:forEach>
                </div>
              </div>
            </div>

            <%-- Summary Breakdown --%>
            <div class="card border-0 shadow-sm rounded-4 p-4 bg-white">
              <h5 class="fw-bold border-bottom pb-3 mb-3">Order Summary</h5>
              <div class="d-flex justify-content-between mb-2 text-secondary">
                <span>Item Total</span>
                <span class="fw-semibold text-dark">₹<fmt:formatNumber value="${cartSubtotal}" pattern="#,##0.00"/></span>
              </div>
              <div class="d-flex justify-content-between mb-2 text-secondary">
                <span>Delivery Partner Fee</span>
                <span class="fw-semibold">${cartDeliveryFee == 0 ? '<span class=\"text-success fw-bold\">FREE</span>' : '₹'.concat(cartDeliveryFee)}</span>
              </div>
              <div class="d-flex justify-content-between mb-2 text-secondary">
                <span>Platform Fee</span>
                <span class="fw-semibold text-dark">₹<fmt:formatNumber value="${cartPlatformFee}" pattern="#,##0.00"/></span>
              </div>
              <div class="d-flex justify-content-between mb-2 text-secondary">
                <span>GST & Taxes (5%)</span>
                <span class="fw-semibold text-dark">₹<fmt:formatNumber value="${cartGst}" pattern="#,##0.00"/></span>
              </div>
              <c:if test="${cartDiscount > 0}">
                <div class="d-flex justify-content-between mb-2 text-success fw-bold">
                  <span>Promo Discount (${appliedCoupon})</span>
                  <span>- ₹<fmt:formatNumber value="${cartDiscount}" pattern="#,##0.00"/></span>
                </div>
              </c:if>
              <hr class="my-3">
              <div class="d-flex justify-content-between align-items-center fs-4 fw-bolder text-dark mb-4">
                <span>To Pay</span>
                <span class="text-primary">₹<fmt:formatNumber value="${cartTotal}" pattern="#,##0.00"/></span>
              </div>

              <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary w-100 rounded-pill py-3 fw-bold fs-5 shadow-sm d-flex align-items-center justify-content-center gap-2">
                <span>Proceed to Checkout</span> <span>→</span>
              </a>
            </div>
          </div>
        </div>
      </c:otherwise>
    </c:choose>
  </div>

  <footer class="footer mt-auto py-4">
    <div class="container text-center"><p class="mb-0 text-white-50">© <%= java.time.Year.now() %> FoodExpress</p></div>
  </footer>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>
