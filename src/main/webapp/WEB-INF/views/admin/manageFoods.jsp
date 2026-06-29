<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Manage Foods — FoodExpress Admin</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
  <style>
    body { background:#f4f6f9; }
    .section-title { color:#2C3E50; font-weight:700; border-left:4px solid #FF6B35; padding-left:.7rem; }
    .form-card, .table-card { border:none; border-radius:1rem; box-shadow:0 4px 16px rgba(0,0,0,.08); }
    .table-card thead { background:#2C3E50; color:#fff; }
    .btn-save { background:#FF6B35; color:#fff; border:none; font-weight:700; }
    .btn-save:hover { background:#e05520; color:#fff; }
    .price-col { color:#FF6B35; font-weight:700; }
  </style>
</head>
<body>
  <%@ include file="/WEB-INF/views/common/adminNavbar.jsp" %>

  <div class="container-fluid py-4 px-4">
    <h3 class="section-title mb-4">🍕 Manage Food Items</h3>

    <%-- ===== ADD / EDIT FORM ===== --%>
    <div class="card form-card p-4 mb-4">
      <h5 class="fw-bold mb-3" style="color:#2C3E50;">
        <c:choose>
          <c:when test="${not empty editFood}">✏️ Edit Food Item</c:when>
          <c:otherwise>➕ Add New Food Item</c:otherwise>
        </c:choose>
      </h5>
      <form method="post" action="${pageContext.request.contextPath}/admin/food">
        <input type="hidden" name="action"
               value="${not empty editFood ? 'edit' : 'add'}">
        <c:if test="${not empty editFood}">
          <input type="hidden" name="foodId" value="${editFood.foodId}">
        </c:if>

        <div class="row g-3">
          <div class="col-md-4">
            <label class="form-label fw-semibold">Food Name *</label>
            <input type="text" class="form-control" name="name" required
                   value="${not empty editFood ? editFood.name : ''}">
          </div>
          <div class="col-md-3">
            <label class="form-label fw-semibold">Restaurant *</label>
            <select class="form-select" name="restaurantId" required>
              <option value="">-- Select Restaurant --</option>
              <c:forEach var="r" items="${restaurants}">
                <option value="${r.restaurantId}"
                  ${(not empty editFood && editFood.restaurantId == r.restaurantId) ? 'selected' : ''}>
                  <c:out value="${r.name}"/>
                </option>
              </c:forEach>
            </select>
          </div>
          <div class="col-md-2">
            <label class="form-label fw-semibold">Price (₹) *</label>
            <input type="number" class="form-control" name="price"
                   step="0.01" min="0" required
                   value="${not empty editFood ? editFood.price : ''}">
          </div>
          <div class="col-md-3">
            <label class="form-label fw-semibold">Category</label>
            <input type="text" class="form-control" name="category"
                   placeholder="e.g. Starters, Main Course"
                   value="${not empty editFood ? editFood.category : ''}">
          </div>
          <div class="col-md-8">
            <label class="form-label fw-semibold">Description</label>
            <textarea class="form-control" name="description" rows="2"
            ><c:if test="${not empty editFood}"><c:out value="${editFood.description}"/></c:if></textarea>
          </div>
          <input type="hidden" name="imageUrl" value="">
          <div class="col-md-2 d-flex align-items-end">
            <div class="form-check">
              <input type="checkbox" class="form-check-input" id="isAvailable" name="isAvailable"
                     value="on"
                     ${(empty editFood || editFood.available) ? 'checked' : ''}>
              <label class="form-check-label fw-semibold" for="isAvailable">Available</label>
            </div>
          </div>
          <div class="col-md-4 d-flex align-items-end gap-2">
            <button type="submit" class="btn btn-save px-4">💾 Save</button>
            <c:if test="${not empty editFood}">
              <a href="${pageContext.request.contextPath}/admin/food"
                 class="btn btn-outline-secondary">Cancel</a>
            </c:if>
          </div>
        </div>
      </form>
    </div>

    <%-- ===== FOOD ITEMS TABLE ===== --%>
    <div class="card table-card">
      <div class="card-header fw-bold" style="background:#2C3E50; color:#fff;">
        All Food Items (${foods.size()})
      </div>
      <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
          <thead>
            <tr>
              <th>ID</th><th>Name</th><th>Restaurant</th>
              <th>Category</th><th>Price</th><th>Available</th><th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <c:choose>
              <c:when test="${empty foods}">
                <tr><td colspan="7" class="text-center text-muted py-4">No food items yet.</td></tr>
              </c:when>
              <c:otherwise>
                <c:forEach var="f" items="${foods}">
                  <tr>
                    <td>#<c:out value="${f.foodId}"/></td>
                    <td class="fw-semibold"><c:out value="${f.name}"/></td>
                    <td>
                      <%-- Find restaurant name from the list --%>
                      <c:forEach var="r" items="${restaurants}">
                        <c:if test="${r.restaurantId == f.restaurantId}">
                          <small class="text-muted"><c:out value="${r.name}"/></small>
                        </c:if>
                      </c:forEach>
                    </td>
                    <td><span class="badge bg-warning text-dark"><c:out value="${f.category}"/></span></td>
                    <td class="price-col">₹<fmt:formatNumber value="${f.price}" pattern="#,##0.00"/></td>
                    <td>
                      <c:choose>
                        <c:when test="${f.available}"><span class="badge bg-success">Yes</span></c:when>
                        <c:otherwise><span class="badge bg-secondary">No</span></c:otherwise>
                      </c:choose>
                    </td>
                    <td>
                      <a href="${pageContext.request.contextPath}/admin/food?action=edit&id=${f.foodId}"
                         class="btn btn-sm btn-outline-primary me-1">✏️ Edit</a>
                      <form method="post" action="${pageContext.request.contextPath}/admin/food"
                            class="d-inline delete-form">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${f.foodId}">
                        <button type="submit" class="btn btn-sm btn-outline-danger">🗑️</button>
                      </form>
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
