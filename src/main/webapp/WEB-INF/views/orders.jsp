<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>My Orders — FoodExpress</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
  <%@ include file="/WEB-INF/views/common/navbar.jsp" %>

  <div class="page-header text-center py-5">
    <div class="container">
      <h1 class="fw-bold display-6 mb-1">My Orders History 📦</h1>
      <p class="lead opacity-90 mb-0">Track active deliveries and review past feasts</p>
    </div>
  </div>

  <div class="container pb-5">
    <c:choose>
      <c:when test="${empty orders}">
        <div class="empty-state-box">
          <div class="empty-state-icon">📭</div>
          <h3 class="fw-bold text-dark mt-3">No Orders Placed Yet</h3>
          <p class="text-muted mb-4">You haven't ordered any food with us. Whenever you place an order, its real-time tracking will appear right here!</p>
          <a href="${pageContext.request.contextPath}/home" class="btn btn-primary px-5 py-3 rounded-pill fw-bold fs-5 shadow">🍔 Order Delicious Meal Now</a>
        </div>
      </c:when>
      <c:otherwise>
        <div class="bg-white rounded-4 shadow-sm border p-3 mb-4">
          <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
              <thead class="table-dark">
                <tr>
                  <th class="py-3 px-4 rounded-start">Order ID</th>
                  <th class="py-3">Date & Time Placed</th>
                  <th class="py-3">Payment Method</th>
                  <th class="py-3">Total Amount</th>
                  <th class="py-3">Order Status</th>
                  <th class="py-3 text-center rounded-end">Action</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="order" items="${orders}">
                  <tr>
                    <td class="fw-bold px-4 text-dark fs-6">#<c:out value="${order.orderId}"/></td>
                    <td class="text-muted fw-medium fs-6">
                      <fmt:formatDate value="${order.createdAt}" pattern="dd MMM yyyy, hh:mm a"/>
                    </td>
                    <td>
                      <span class="badge bg-light text-dark border px-3 py-2 fw-bold small">
                        💳 <c:out value="${order.paymentMethod}"/>
                      </span>
                    </td>
                    <td class="fw-bold text-primary fs-5">
                      ₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/>
                    </td>
                    <td>
                      <span class="badge ${order.statusBadgeClass} px-3 py-2 fs-6 shadow-sm">
                        <c:out value="${order.status}"/>
                      </span>
                    </td>
                    <td class="text-center">
                      <a href="${pageContext.request.contextPath}/trackOrder?orderId=${order.orderId}" class="btn btn-primary btn-sm rounded-pill px-3 py-1 fw-bold shadow-sm">📍 Track</a>
                      <c:if test="${order.status == 'DELIVERED'}">
                        <button type="button" class="btn btn-warning btn-sm rounded-pill px-3 py-1 fw-bold text-dark ms-1 shadow-sm" data-bs-toggle="modal" data-bs-target="#reviewModal${order.orderId}">⭐ Rate</button>

                        <%-- Review Modal --%>
                        <div class="modal fade text-start" id="reviewModal${order.orderId}" tabindex="-1">
                          <div class="modal-dialog modal-dialog-centered">
                            <div class="modal-content rounded-4 shadow-lg border-0 p-3">
                              <div class="modal-header border-0 pb-0">
                                <h5 class="modal-title fw-bold text-dark">Rate Order #${order.orderId}</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                              </div>
                              <form method="post" action="${pageContext.request.contextPath}/review">
                                <div class="modal-body py-3">
                                  <input type="hidden" name="orderId" value="${order.orderId}">
                                  <div class="mb-3">
                                    <label class="form-label fw-bold small text-muted">Star Rating (1 - 5 ⭐)</label>
                                    <select name="rating" class="form-select fw-bold text-warning fs-5">
                                      <option value="5">⭐⭐⭐⭐⭐ (5/5 Excellent)</option>
                                      <option value="4">⭐⭐⭐⭐ (4/5 Good)</option>
                                      <option value="3">⭐⭐⭐ (3/5 Average)</option>
                                      <option value="2">⭐⭐ (2/5 Poor)</option>
                                      <option value="1">⭐ (1/5 Terrible)</option>
                                    </select>
                                  </div>
                                  <div class="mb-3">
                                    <label class="form-label fw-bold small text-muted">Feedback Comment</label>
                                    <textarea name="comment" class="form-control" rows="3" placeholder="Tell us how the food tasted!"></textarea>
                                  </div>
                                </div>
                                <div class="modal-footer border-0 pt-0">
                                  <button type="button" class="btn btn-outline-secondary rounded-pill px-4" data-bs-dismiss="modal">Cancel</button>
                                  <button type="submit" class="btn btn-success rounded-pill px-4 fw-bold">Submit Review ⭐</button>
                                </div>
                              </form>
                            </div>
                          </div>
                        </div>
                      </c:if>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
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

