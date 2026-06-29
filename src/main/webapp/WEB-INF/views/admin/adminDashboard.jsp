<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin Dashboard — FoodExpress</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
  <style>
    body { background:#f4f6f9; }
    .stat-card { border:none; border-radius:1.2rem; box-shadow:0 4px 20px rgba(0,0,0,.08); color:#fff; padding:1.8rem; transition:transform .2s; }
    .stat-card:hover { transform:translateY(-4px); }
    .stat-number { font-size:2.5rem; font-weight:800; line-height:1.1; }
    .stat-label  { font-size:0.95rem; opacity:.9; margin-top:.3rem; font-weight:600; }
    .action-card { border:1px solid #dee2e6; border-radius:1.2rem; box-shadow:0 4px 16px rgba(0,0,0,.04); transition:all .2s; text-decoration:none; background:#fff; }
    .action-card:hover { transform:translateY(-4px); box-shadow:0 8px 24px rgba(0,0,0,.08); }
    .section-title { color:#2C3E50; font-weight:700; border-left:4px solid #FF6B35; padding-left:.7rem; }
  </style>
</head>
<body class="d-flex flex-column min-vh-100">
  <%@ include file="/WEB-INF/views/common/adminNavbar.jsp" %>

  <div class="container-fluid py-4 px-4 flex-grow-1" style="max-width: 1400px; margin: 0 auto;">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h3 class="section-title mb-0">📊 Executive Analytics Overview</h3>
      <span class="badge bg-dark px-3 py-2 fs-6">📅 Today: <%= java.time.LocalDate.now() %></span>
    </div>

    <%-- Stats Row 1: Key Metrics --%>
    <div class="row g-4 mb-4">
      <div class="col-md-3">
        <div class="stat-card" style="background:linear-gradient(135deg,#FF6B35,#e05520);">
          <div class="stat-number">₹<fmt:formatNumber value="${todayRevenue}" pattern="#,##0"/></div>
          <div class="stat-label">💰 Today's Gross Revenue</div>
        </div>
      </div>
      <div class="col-md-3">
        <div class="stat-card" style="background:linear-gradient(135deg,#3498db,#2980b9);">
          <div class="stat-number">${todayOrders}</div>
          <div class="stat-label">🛒 Today's Orders</div>
        </div>
      </div>
      <div class="col-md-3">
        <div class="stat-card" style="background:linear-gradient(135deg,#27ae60,#1e8449);">
          <div class="stat-number">${activeOutlets} <small class="fs-6">/ ${restaurantCount}</small></div>
          <div class="stat-label">🟢 Active Kitchen Outlets</div>
        </div>
      </div>
      <div class="col-md-3">
        <div class="stat-card" style="background:linear-gradient(135deg,#8e44ad,#6c3483);">
          <div class="stat-number">${totalUsers}</div>
          <div class="stat-label">👥 Registered Customers</div>
        </div>
      </div>
    </div>

    <%-- Stats Row 2: Breakdown & Highlights --%>
    <div class="row g-4 mb-5">
      <div class="col-md-8">
        <div class="card border-0 shadow-sm rounded-4 p-4 bg-white h-100">
          <h5 class="fw-bold mb-4 text-dark">📈 Order Status Breakdown (All Time)</h5>
          <div class="row text-center g-3">
            <div class="col-6 col-md-3">
              <div class="p-3 rounded-4 bg-secondary-subtle border">
                <h3 class="fw-bolder mb-1 text-secondary">${placedCount}</h3>
                <span class="small fw-bold text-muted text-uppercase">New / Placed</span>
              </div>
            </div>
            <div class="col-6 col-md-3">
              <div class="p-3 rounded-4 bg-warning-subtle border">
                <h3 class="fw-bolder mb-1 text-dark">${preparingCount}</h3>
                <span class="small fw-bold text-dark text-uppercase">In Kitchen</span>
              </div>
            </div>
            <div class="col-6 col-md-3">
              <div class="p-3 rounded-4 bg-success-subtle border border-success">
                <h3 class="fw-bolder mb-1 text-success">${deliveredCount}</h3>
                <span class="small fw-bold text-success text-uppercase">Delivered</span>
              </div>
            </div>
            <div class="col-6 col-md-3">
              <div class="p-3 rounded-4 bg-danger-subtle border border-danger">
                <h3 class="fw-bolder mb-1 text-danger">${cancelledCount}</h3>
                <span class="small fw-bold text-danger text-uppercase">Cancelled</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="col-md-4">
        <div class="card border-0 shadow-sm rounded-4 p-4 bg-dark text-white h-100 d-flex flex-column justify-content-center">
          <span class="badge bg-warning text-dark w-auto mb-2 fw-bold align-self-start">🏆 #1 BESTSELLER</span>
          <h4 class="fw-bolder display-6 text-warning mb-2">${topSellingDish}</h4>
          <p class="text-white-50 small mb-0">Most ordered delicacy across all culinary stores on the FoodExpress platform.</p>
        </div>
      </div>
    </div>

    <%-- Quick Actions --%>
    <h5 class="section-title mb-3">⚡ Admin Operations Portal</h5>
    <div class="row g-4">
      <div class="col-md-4">
        <a href="${pageContext.request.contextPath}/admin/restaurant" class="card action-card p-4 h-100 d-flex flex-column justify-content-between">
          <div>
            <div style="font-size:2.5rem; margin-bottom:.5rem;">🏪</div>
            <h5 class="fw-bold text-dark mb-2">Manage Restaurants</h5>
            <p class="text-muted small mb-0">Configure store timing, minimum order limit, banners, and active status.</p>
          </div>
          <span class="fw-bold text-primary mt-3 small">Open Portal →</span>
        </a>
      </div>
      <div class="col-md-4">
        <a href="${pageContext.request.contextPath}/admin/food" class="card action-card p-4 h-100 d-flex flex-column justify-content-between">
          <div>
            <div style="font-size:2.5rem; margin-bottom:.5rem;">🍕</div>
            <h5 class="fw-bold text-dark mb-2">Manage Dishes & Menus</h5>
            <p class="text-muted small mb-0">Add new delicacies, update prices, manage stock availability, and categorize.</p>
          </div>
          <span class="fw-bold text-primary mt-3 small">Open Portal →</span>
        </a>
      </div>
      <div class="col-md-4">
        <a href="${pageContext.request.contextPath}/admin/orders" class="card action-card p-4 h-100 d-flex flex-column justify-content-between">
          <div>
            <div style="font-size:2.5rem; margin-bottom:.5rem;">📦</div>
            <h5 class="fw-bold text-dark mb-2">Live Orders Console</h5>
            <p class="text-muted small mb-0">Real-time lifecycle control (Accept, Reject, Kitchen Prep, Assign Valet, Deliver).</p>
          </div>
          <span class="fw-bold text-primary mt-3 small">Open Console →</span>
        </a>
      </div>
    </div>
  </div>

  <footer class="footer mt-auto py-4">
    <div class="container text-center"><p class="mb-0 text-white-50">© <%= java.time.Year.now() %> FoodExpress</p></div>
  </footer>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
