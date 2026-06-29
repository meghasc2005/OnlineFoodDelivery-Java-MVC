<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Track Order #${order.orderId} — FoodExpress</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="d-flex flex-column min-vh-100 bg-light">
  <%@ include file="/WEB-INF/views/common/navbar.jsp" %>

  <div class="page-header text-center py-5">
    <div class="container d-flex flex-column flex-md-row align-items-center justify-content-between gap-3">
      <div>
        <h1 class="fw-bold display-6 mb-1 text-start">Order Tracking #${order.orderId}</h1>
        <p class="lead opacity-90 mb-0 text-start">Real-time status updates & delivery estimate</p>
      </div>
      <div class="d-flex gap-2 flex-wrap">
        <a href="${pageContext.request.contextPath}/invoice?orderId=${order.orderId}" target="_blank" class="btn btn-warning text-dark rounded-pill px-4 py-2 fw-bold shadow-sm">🖨️ Print Invoice</a>
        <a href="${pageContext.request.contextPath}/orders" class="btn btn-outline-light rounded-pill px-4 py-2 fw-semibold">← My Orders</a>
      </div>
    </div>
  </div>

  <div class="container py-5 flex-grow-1" style="max-width: 950px;">

    <%-- ===== LIVE TOAST NOTIFICATION ===== --%>
    <div id="liveToastBox" class="alert alert-success d-none shadow-sm rounded-4 mb-4 fw-semibold d-flex align-items-center justify-content-between">
      <span id="liveToastMsg">🔔 Status Updated!</span>
      <button type="button" class="btn-close" onclick="document.getElementById('liveToastBox').classList.add('d-none');"></button>
    </div>

    <%-- ===== ESTIMATED DELIVERY TIMER BANNER ===== --%>
    <div class="card bg-dark text-white rounded-4 shadow-sm p-4 mb-4 border-0 d-flex flex-row align-items-center justify-content-between flex-wrap gap-3">
      <div class="d-flex align-items-center gap-3">
        <div class="display-5">🛵</div>
        <div>
          <h4 class="fw-bold mb-1">Estimated Arrival Time</h4>
          <p class="text-white-50 small mb-0" id="statusDescText">Your food order is progressing smoothly through our kitchen.</p>
        </div>
      </div>
      <div class="bg-white bg-opacity-10 px-4 py-3 rounded-4 text-center border border-white border-opacity-25" style="min-width: 160px;">
        <span class="d-block small text-white-50 text-uppercase fw-bold">Time Remaining</span>
        <span class="display-6 fw-bolder font-monospace text-warning" id="countdownTimerDisplay">25:00</span>
      </div>
    </div>

    <%-- ===== 7-STAGE STATUS STEPPER ===== --%>
    <div class="bg-white rounded-4 shadow-sm border border-secondary-subtle p-4 mb-4">
      <div class="d-flex justify-content-between align-items-center border-bottom pb-3 mb-4">
        <h5 class="fw-bold mb-0 text-dark">📍 7-Stage Delivery Timeline</h5>
        <span class="badge bg-success-subtle text-success border border-success small" id="livePollingBadge">🔄 Live Auto-Refresh Active</span>
      </div>

      <c:set var="s" value="${order.status}"/>
      <c:if test="${s == 'CANCELLED' || s == 'REJECTED'}">
        <div class="alert alert-danger fw-bold text-center my-3 p-4 rounded-3 shadow-sm fs-5">
          ❌ This order was ${s}.
        </div>
      </c:if>
      <c:if test="${s != 'CANCELLED' && s != 'REJECTED'}">
        <div id="orderStepper" class="stepper my-4 d-flex justify-content-between position-relative" data-status="${order.status}">
          <div class="step text-center flex-fill" data-status="PLACED">
            <div class="step-circle mx-auto shadow-sm mb-2 d-flex align-items-center justify-content-center fw-bold">1</div>
            <div class="step-label small fw-semibold">Placed</div>
          </div>
          <div class="step text-center flex-fill" data-status="ACCEPTED">
            <div class="step-circle mx-auto shadow-sm mb-2 d-flex align-items-center justify-content-center fw-bold">2</div>
            <div class="step-label small fw-semibold">Accepted</div>
          </div>
          <div class="step text-center flex-fill" data-status="PREPARING">
            <div class="step-circle mx-auto shadow-sm mb-2 d-flex align-items-center justify-content-center fw-bold">3</div>
            <div class="step-label small fw-semibold">Preparing</div>
          </div>
          <div class="step text-center flex-fill" data-status="FOOD_READY">
            <div class="step-circle mx-auto shadow-sm mb-2 d-flex align-items-center justify-content-center fw-bold">4</div>
            <div class="step-label small fw-semibold">Food Ready</div>
          </div>
          <div class="step text-center flex-fill" data-status="ASSIGNED">
            <div class="step-circle mx-auto shadow-sm mb-2 d-flex align-items-center justify-content-center fw-bold">5</div>
            <div class="step-label small fw-semibold">Assigned</div>
          </div>
          <div class="step text-center flex-fill" data-status="OUT_FOR_DELIVERY">
            <div class="step-circle mx-auto shadow-sm mb-2 d-flex align-items-center justify-content-center fw-bold">6</div>
            <div class="step-label small fw-semibold">On Way</div>
          </div>
          <div class="step text-center flex-fill" data-status="DELIVERED">
            <div class="step-circle mx-auto shadow-sm mb-2 d-flex align-items-center justify-content-center fw-bold">7</div>
            <div class="step-label small fw-semibold">Delivered 🎉</div>
          </div>
        </div>
      </c:if>
    </div>

    <div class="row g-4 mb-4">
      <div class="col-md-6">
        <div class="bg-white rounded-4 shadow-sm border border-secondary-subtle p-4 h-100">
          <h5 class="fw-bold mb-3 text-dark border-bottom pb-2">🧾 Order Info</h5>
          <table class="table table-borderless mb-0">
            <tr><td class="text-muted py-2">Delivery Address</td>
                <td class="fw-semibold text-dark py-2">${order.deliveryAddress}</td></tr>
            <tr><td class="text-muted py-2">Payment Method</td>
                <td class="py-2"><span class="badge bg-light text-dark border px-3 py-1 fw-bold">${order.paymentMethod}</span></td></tr>
            <tr><td class="text-muted py-2">Current Status</td>
                <td class="py-2"><span class="badge ${order.statusBadgeClass} px-3 py-1" id="orderStatusBadgeSpan">${order.status}</span></td></tr>
            <tr><td class="text-muted py-2 border-top pt-3">Total Paid</td>
                <td class="fw-bold price-col fs-4 text-dark py-2 border-top pt-3">₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></td></tr>
          </table>
        </div>
      </div>

      <div class="col-md-6">
        <c:if test="${not empty payment}">
          <div class="bg-white rounded-4 shadow-sm border border-secondary-subtle p-4 h-100">
            <h5 class="fw-bold mb-3 text-dark border-bottom pb-2">💳 Payment Verification</h5>
            <table class="table table-borderless mb-0">
              <tr><td class="text-muted py-2">Payment Type</td>
                  <td class="py-2"><span class="badge bg-light text-dark border px-3 py-1 fw-bold">${payment.paymentMethod}</span></td></tr>
              <tr><td class="text-muted py-2">Gateway Status</td>
                  <td class="py-2">
                    <span class="badge bg-success px-3 py-1">✓ ${payment.status}</span>
                  </td></tr>
              <c:if test="${not empty payment.transactionId}">
                <tr><td class="text-muted py-2 border-top pt-3">Transaction Ref</td>
                    <td class="font-monospace small py-2 border-top pt-3 text-secondary fw-bold">${payment.transactionId}</td></tr>
              </c:if>
            </table>
          </div>
        </c:if>
      </div>
    </div>

    <%-- ===== ORDER ITEMS TABLE ===== --%>
    <div class="bg-white rounded-4 shadow-sm border border-secondary-subtle overflow-hidden">
      <div class="card-header fw-bold py-3 px-4 bg-dark text-white fs-5 d-flex justify-content-between align-items-center">
        <span>🍽️ Feast Items Ordered</span>
        <span class="badge bg-light text-dark fs-6">${orderItems.size()} Items</span>
      </div>
      <div class="table-responsive p-3">
        <table class="table table-hover align-middle mb-0">
          <thead class="table-light">
            <tr>
              <th class="py-3 px-3">Item Name</th>
              <th class="py-3 text-center">Qty</th>
              <th class="py-3">Unit Price</th>
              <th class="py-3 text-end px-3">Subtotal</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="item" items="${orderItems}">
              <tr>
                <td class="fw-bold px-3 text-dark fs-6">
                  <span class="me-2">${item.food.veg ? '🟢' : '🔴'}</span>${item.food.name}
                </td>
                <td class="text-center"><span class="badge bg-secondary rounded-pill px-3 py-1">${item.quantity}</span></td>
                <td class="text-muted">₹<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/></td>
                <td class="price-col fw-bold fs-6 text-end px-3 text-dark">₹<fmt:formatNumber value="${item.itemTotal}" pattern="#,##0.00"/></td>
              </tr>
            </c:forEach>
          </tbody>
          <tfoot class="bg-light">
            <tr>
              <td colspan="3" class="text-end fw-bold fs-5 py-3 text-secondary pe-3">Total Amount Paid</td>
              <td class="price-col fw-bolder fs-4 py-3 text-end px-3 text-success">₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></td>
            </tr>
          </tfoot>
        </table>
      </div>
    </div>

  </div>

  <footer class="footer mt-auto py-4">
    <div class="container text-center"><p class="mb-0 text-white-50">© <%= java.time.Year.now() %> FoodExpress</p></div>
  </footer>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  <script>
    // ===== LIVE COUNTDOWN TIMER & STEPPER LOGIC =====
    let currentStatus = '${order.status}';
    let remainingSeconds = ${not empty remainingSeconds ? remainingSeconds : 25 * 60};

    const stages = ['PLACED', 'ACCEPTED', 'PREPARING', 'FOOD_READY', 'ASSIGNED', 'OUT_FOR_DELIVERY', 'DELIVERED'];

    function updateStepperUI(status) {
      const stepper = document.getElementById('orderStepper');
      if (!stepper) return;
      const steps = stepper.querySelectorAll('.step');
      let foundIndex = stages.indexOf(status);
      if (status === 'CONFIRMED') foundIndex = 1;

      steps.forEach((step, idx) => {
        const circle = step.querySelector('.step-circle');
        if (idx < foundIndex) {
          circle.style.background = '#198754';
          circle.style.color = '#fff';
          circle.innerHTML = '✓';
        } else if (idx === foundIndex) {
          circle.style.background = '#0d6efd';
          circle.style.color = '#fff';
          circle.style.boxShadow = '0 0 0 5px rgba(13,110,253,0.25)';
        } else {
          circle.style.background = '#e9ecef';
          circle.style.color = '#6c757d';
        }
      });
    }

    function startCountdown() {
      setInterval(() => {
        if (remainingSeconds > 0 && currentStatus !== 'DELIVERED' && currentStatus !== 'CANCELLED') {
          remainingSeconds--;
          const m = Math.floor(remainingSeconds / 60);
          const s = remainingSeconds % 60;
          document.getElementById('countdownTimerDisplay').innerText = 
            (m < 10 ? '0' : '') + m + ':' + (s < 10 ? '0' : '') + s;
        } else if (currentStatus === 'DELIVERED' || remainingSeconds <= 0) {
          document.getElementById('countdownTimerDisplay').innerText = '00:00';
          document.getElementById('statusDescText').innerText = 'Order delivered successfully! Bon Appétit!';
        }
      }, 1000);
    }

    // ===== AJAX LIVE POLLING (6 SECONDS) =====
    function startAjaxPolling() {
      setInterval(() => {
        fetch('${pageContext.request.contextPath}/trackOrder?action=status&orderId=${order.orderId}')
          .then(res => res.json())
          .then(data => {
            if (data && data.status) {
              if (data.status !== currentStatus) {
                currentStatus = data.status;
                const badgeSpan = document.getElementById('orderStatusBadgeSpan');
                if (badgeSpan) {
                  badgeSpan.className = 'badge px-3 py-1 ' + data.badgeClass;
                  badgeSpan.innerText = currentStatus;
                }
                updateStepperUI(currentStatus);

                const toast = document.getElementById('liveToastBox');
                const msg = document.getElementById('liveToastMsg');
                if (toast && msg) {
                  msg.innerText = '🔔 Update: ' + data.notification;
                  toast.classList.remove('d-none');
                }
              }
              if (typeof data.remainingSecs === 'number') {
                remainingSeconds = data.remainingSecs;
              }
            }
          })
          .catch(err => console.log('Live polling paused'));
      }, 6000);
    }

    document.addEventListener('DOMContentLoaded', () => {
      updateStepperUI(currentStatus);
      startCountdown();
      startAjaxPolling();
    });
  </script>
</body>
</html>
