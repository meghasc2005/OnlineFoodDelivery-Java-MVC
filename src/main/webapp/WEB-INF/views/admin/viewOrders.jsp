<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>All Orders — FoodExpress Admin</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
  <style>
    body { background:#f4f6f9; }
    .section-title { color:#2C3E50; font-weight:700; border-left:4px solid #FF6B35; padding-left:.7rem; }
    .table-card { border:none; border-radius:1rem; box-shadow:0 4px 16px rgba(0,0,0,.08); overflow:hidden; }
    .table-card thead { background:#2C3E50; color:#fff; }
    .price-col { color:#FF6B35; font-weight:700; }
    .status-select { font-size:.8rem; border-radius:.4rem; padding:.2rem .5rem; }
    .btn-update { background:#27ae60; color:#fff; border:none; font-size:.8rem; border-radius:.4rem; padding:.25rem .6rem; }
    .btn-update:hover { background:#1e8449; color:#fff; }
  </style>
</head>
<body>
  <%@ include file="/WEB-INF/views/common/adminNavbar.jsp" %>

  <div class="container-fluid py-4 px-4">
    <h3 class="section-title mb-4">📦 All Orders</h3>

    <div class="card table-card">
      <div class="card-header fw-bold" style="background:#2C3E50; color:#fff;">
        Total Orders: ${orders.size()}
      </div>
      <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
          <thead>
            <tr>
              <th>Order ID</th>
              <th>Customer ID</th>
              <th>Date</th>
              <th>Total</th>
              <th>Payment</th>
              <th>Delivery Address</th>
              <th>Status</th>
              <th>Update Status</th>
            </tr>
          </thead>
          <tbody>
            <c:choose>
              <c:when test="${empty orders}">
                <tr>
                  <td colspan="8" class="text-center text-muted py-5">
                    <div style="font-size:3rem;">📭</div>
                    <div>No orders yet.</div>
                  </td>
                </tr>
              </c:when>
              <c:otherwise>
                <c:forEach var="order" items="${orders}">
                  <tr>
                    <td class="fw-bold">#<c:out value="${order.orderId}"/></td>
                    <td class="text-muted">User #<c:out value="${order.userId}"/></td>
                    <td>
                      <small>
                        <fmt:formatDate value="${order.createdAt}"
                                        pattern="dd MMM yyyy"/>
                        <br>
                        <span class="text-muted">
                          <fmt:formatDate value="${order.createdAt}" pattern="hh:mm a"/>
                        </span>
                      </small>
                    </td>
                    <td class="price-col">
                      ₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/>
                    </td>
                    <td>
                      <span class="badge bg-secondary">
                        <c:out value="${order.paymentMethod}"/>
                      </span>
                    </td>
                    <td>
                      <small class="text-muted" style="max-width:150px; display:block; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;"
                             title="${order.deliveryAddress}">
                        <c:out value="${order.deliveryAddress}"/>
                      </small>
                    </td>
                    <td>
                      <span class="badge ${order.statusBadgeClass}">
                        <c:out value="${order.status}"/>
                      </span>
                    </td>
                    <td>
                      <form method="post" action="${pageContext.request.contextPath}/admin/orders"
                            class="d-flex align-items-center gap-1">
                        <input type="hidden" name="action" value="updateStatus">
                        <input type="hidden" name="orderId" value="${order.orderId}">
                        <select name="status" class="form-select form-select-sm status-select" style="width:auto;">
                          <c:forEach var="opt" items="${statusOptions}">
                            <option value="${opt}" ${order.status == opt ? 'selected' : ''}>${opt}</option>
                          </c:forEach>
                        </select>
                        <button type="submit" class="btn btn-sm btn-dark">Save</button>
                      </form>

                      <%-- Quick Lifecycle Progression Buttons --%>
                      <div class="d-flex gap-1 mt-1 flex-wrap">
                        <c:if test="${order.status == 'PLACED'}">
                          <form method="post" action="${pageContext.request.contextPath}/admin/orders" class="m-0">
                            <input type="hidden" name="action" value="updateStatus"><input type="hidden" name="orderId" value="${order.orderId}"><input type="hidden" name="status" value="ACCEPTED">
                            <button type="submit" class="btn btn-sm btn-success py-0 px-2 small fw-bold">Accept</button>
                          </form>
                          <form method="post" action="${pageContext.request.contextPath}/admin/orders" class="m-0">
                            <input type="hidden" name="action" value="updateStatus"><input type="hidden" name="orderId" value="${order.orderId}"><input type="hidden" name="status" value="REJECTED">
                            <button type="submit" class="btn btn-sm btn-outline-danger py-0 px-2 small fw-bold">Reject</button>
                          </form>
                        </c:if>
                        <c:if test="${order.status == 'ACCEPTED' || order.status == 'CONFIRMED'}">
                          <form method="post" action="${pageContext.request.contextPath}/admin/orders" class="m-0">
                            <input type="hidden" name="action" value="updateStatus"><input type="hidden" name="orderId" value="${order.orderId}"><input type="hidden" name="status" value="PREPARING">
                            <button type="submit" class="btn btn-sm btn-warning py-0 px-2 small fw-bold text-dark">Start Preparing</button>
                          </form>
                        </c:if>
                        <c:if test="${order.status == 'PREPARING'}">
                          <form method="post" action="${pageContext.request.contextPath}/admin/orders" class="m-0">
                            <input type="hidden" name="action" value="updateStatus"><input type="hidden" name="orderId" value="${order.orderId}"><input type="hidden" name="status" value="FOOD_READY">
                            <button type="submit" class="btn btn-sm btn-primary py-0 px-2 small fw-bold">Food Ready</button>
                          </form>
                        </c:if>
                        <c:if test="${order.status == 'FOOD_READY'}">
                          <form method="post" action="${pageContext.request.contextPath}/admin/orders" class="m-0">
                            <input type="hidden" name="action" value="updateStatus"><input type="hidden" name="orderId" value="${order.orderId}"><input type="hidden" name="status" value="ASSIGNED">
                            <button type="submit" class="btn btn-sm btn-info py-0 px-2 small fw-bold text-dark">Assign Delivery</button>
                          </form>
                        </c:if>
                        <c:if test="${order.status == 'ASSIGNED'}">
                          <form method="post" action="${pageContext.request.contextPath}/admin/orders" class="m-0">
                            <input type="hidden" name="action" value="updateStatus"><input type="hidden" name="orderId" value="${order.orderId}"><input type="hidden" name="status" value="OUT_FOR_DELIVERY">
                            <button type="submit" class="btn btn-sm btn-primary py-0 px-2 small fw-bold">Out For Delivery</button>
                          </form>
                        </c:if>
                        <c:if test="${order.status == 'OUT_FOR_DELIVERY'}">
                          <form method="post" action="${pageContext.request.contextPath}/admin/orders" class="m-0">
                            <input type="hidden" name="action" value="updateStatus"><input type="hidden" name="orderId" value="${order.orderId}"><input type="hidden" name="status" value="DELIVERED">
                            <button type="submit" class="btn btn-sm btn-success py-0 px-2 small fw-bold">Mark Delivered</button>
                          </form>
                        </c:if>
                      </div>
                    </td>
                  </tr>
                </c:forEach>
              </c:otherwise>
            </c:choose>
          </tbody>
        </table>
      </div>
    </div>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>
