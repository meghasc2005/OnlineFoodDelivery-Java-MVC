<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Manage Restaurants — FoodExpress Admin</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
  <style>
    body { background:#f4f6f9; }
    .section-title { color:#2C3E50; font-weight:700; border-left:4px solid #FF6B35; padding-left:.7rem; }
    .form-card, .table-card { border:none; border-radius:1rem; box-shadow:0 4px 16px rgba(0,0,0,.08); }
    .table-card thead { background:#2C3E50; color:#fff; }
    .btn-save { background:#FF6B35; color:#fff; border:none; font-weight:700; }
    .btn-save:hover { background:#e05520; color:#fff; }
  </style>
</head>
<body>
  <%@ include file="/WEB-INF/views/common/adminNavbar.jsp" %>

  <div class="container-fluid py-4 px-4">
    <div class="d-flex align-items-center justify-content-between mb-4">
      <h3 class="section-title mb-0">🏪 Manage Restaurants</h3>
    </div>

    <c:if test="${not empty error}">
      <div class="alert alert-danger alert-dismissible fade show">
        <c:out value="${error}"/>
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
      </div>
    </c:if>

    <%-- ===== ADD / EDIT FORM ===== --%>
    <div class="card form-card p-4 mb-4">
      <h5 class="fw-bold mb-3" style="color:#2C3E50;">
        <c:choose>
          <c:when test="${not empty editRestaurant}">✏️ Edit Restaurant</c:when>
          <c:otherwise>➕ Add New Restaurant</c:otherwise>
        </c:choose>
      </h5>
      <form method="post" action="${pageContext.request.contextPath}/admin/restaurant">
        <input type="hidden" name="action"
               value="${not empty editRestaurant ? 'edit' : 'add'}">
        <c:if test="${not empty editRestaurant}">
          <input type="hidden" name="restaurantId" value="${editRestaurant.restaurantId}">
        </c:if>

        <div class="row g-3">
          <div class="col-md-4">
            <label class="form-label fw-semibold">Restaurant Name *</label>
            <input type="text" class="form-control" name="name" required
                   value="${not empty editRestaurant ? editRestaurant.name : ''}">
          </div>
          <div class="col-md-4">
            <label class="form-label fw-semibold">Cuisine Type</label>
            <input type="text" class="form-control" name="cuisineType"
                   value="${not empty editRestaurant ? editRestaurant.cuisineType : ''}">
          </div>
          <div class="col-md-2">
            <label class="form-label fw-semibold">Rating (0–5)</label>
            <input type="number" class="form-control" name="rating"
                   min="0" max="5" step="0.1"
                   value="${not empty editRestaurant ? editRestaurant.rating : '4.0'}">
          </div>
          <div class="col-md-2 d-flex align-items-end">
            <div class="form-check">
              <input type="checkbox" class="form-check-input" id="isActive" name="isActive"
                     value="on"
                     ${(empty editRestaurant || editRestaurant.active) ? 'checked' : ''}>
              <label class="form-check-label fw-semibold" for="isActive">Is Active</label>
            </div>
          </div>
          <div class="col-md-6">
            <label class="form-label fw-semibold">Description</label>
            <textarea class="form-control" name="description" rows="2"
            ><c:if test="${not empty editRestaurant}"><c:out value="${editRestaurant.description}"/></c:if></textarea>
          </div>
          <input type="hidden" name="imageUrl" value="">
          <div class="col-md-6">
            <label class="form-label fw-semibold">Address</label>
            <input type="text" class="form-control" name="address"
                   value="${not empty editRestaurant ? editRestaurant.address : ''}">
          </div>
          <div class="col-md-3">
            <label class="form-label fw-semibold">Phone</label>
            <input type="tel" class="form-control" name="phone"
                   value="${not empty editRestaurant ? editRestaurant.phone : ''}">
          </div>
          <div class="col-md-3 d-flex align-items-end gap-2">
            <button type="submit" class="btn btn-save px-4">💾 Save</button>
            <c:if test="${not empty editRestaurant}">
              <a href="${pageContext.request.contextPath}/admin/restaurant"
                 class="btn btn-outline-secondary">Cancel</a>
            </c:if>
          </div>
        </div>
      </form>
    </div>

    <%-- ===== RESTAURANTS TABLE ===== --%>
    <div class="card table-card">
      <div class="card-header fw-bold" style="background:#2C3E50; color:#fff;">
        All Restaurants (${restaurants.size()})
      </div>
      <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
          <thead>
            <tr>
              <th>ID</th><th>Name</th><th>Cuisine</th>
              <th>Rating</th><th>Active</th><th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <c:choose>
              <c:when test="${empty restaurants}">
                <tr><td colspan="6" class="text-center text-muted py-4">No restaurants yet.</td></tr>
              </c:when>
              <c:otherwise>
                <c:forEach var="r" items="${restaurants}">
                  <tr>
                    <td>#<c:out value="${r.restaurantId}"/></td>
                    <td class="fw-semibold"><c:out value="${r.name}"/></td>
                    <td><span class="badge bg-warning text-dark"><c:out value="${r.cuisineType}"/></span></td>
                    <td>⭐ <c:out value="${r.rating}"/></td>
                    <td>
                      <c:choose>
                        <c:when test="${r.active}"><span class="badge bg-success">Active</span></c:when>
                        <c:otherwise><span class="badge bg-secondary">Inactive</span></c:otherwise>
                      </c:choose>
                    </td>
                    <td>
                      <a href="${pageContext.request.contextPath}/admin/restaurant?action=edit&id=${r.restaurantId}"
                         class="btn btn-sm btn-outline-primary me-1">✏️ Edit</a>
                      <form method="post" action="${pageContext.request.contextPath}/admin/restaurant"
                            class="d-inline delete-form">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${r.restaurantId}">
                        <button type="submit" class="btn btn-sm btn-outline-danger">🗑️ Delete</button>
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
