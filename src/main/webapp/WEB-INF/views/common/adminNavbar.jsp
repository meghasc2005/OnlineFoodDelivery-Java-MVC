<%-- Admin Navbar Partial — included in all admin pages --%>
<%
  String admUri = request.getRequestURI();
  boolean isDash = admUri.endsWith("/admin/dashboard");
  boolean isRest = admUri.contains("/admin/restaurant");
  boolean isFood = admUri.contains("/admin/food");
  boolean isAdmOrd = admUri.contains("/admin/orders");
%>
<nav class="navbar navbar-expand-lg navbar-dark shadow" style="background-color:#1a252f;">
  <div class="container-fluid px-4">
    <a class="navbar-brand fw-bold d-flex align-items-center gap-2" href="${pageContext.request.contextPath}/admin/dashboard">
      <span style="font-size:1.5rem;">🍔</span> FoodExpress <span class="badge bg-danger ms-1 px-2 py-1 rounded-pill" style="font-size:0.65rem; letter-spacing: 0.5px;">ADMIN</span>
    </a>
    <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#adminNav">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="adminNav">
      <ul class="navbar-nav me-auto gap-1 mt-2 mt-lg-0">
        <li class="nav-item">
          <a class="nav-link px-3 rounded <%= isDash ? "active bg-secondary text-warning fw-bold" : "" %>" href="${pageContext.request.contextPath}/admin/dashboard">📊 Dashboard</a>
        </li>
        <li class="nav-item">
          <a class="nav-link px-3 rounded <%= isRest ? "active bg-secondary text-warning fw-bold" : "" %>" href="${pageContext.request.contextPath}/admin/restaurant">🏪 Restaurants</a>
        </li>
        <li class="nav-item">
          <a class="nav-link px-3 rounded <%= isFood ? "active bg-secondary text-warning fw-bold" : "" %>" href="${pageContext.request.contextPath}/admin/food">🍕 Foods</a>
        </li>
        <li class="nav-item">
          <a class="nav-link px-3 rounded <%= isAdmOrd ? "active bg-secondary text-warning fw-bold" : "" %>" href="${pageContext.request.contextPath}/admin/orders">📦 Orders</a>
        </li>
      </ul>
      <ul class="navbar-nav ms-auto align-items-center gap-3">
        <li class="nav-item">
          <span class="badge bg-warning text-dark px-3 py-2 rounded-pill fw-bold">👑 ${sessionScope.loggedUser.fullName}</span>
        </li>
        <li class="nav-item">
          <a class="btn btn-outline-danger btn-sm rounded-pill px-3 fw-semibold" href="${pageContext.request.contextPath}/logout">🚪 Logout</a>
        </li>
      </ul>
    </div>
  </div>
</nav>

