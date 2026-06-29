<%-- Customer Navbar Partial — included in all customer-facing pages --%>
<%
  String reqUri = request.getRequestURI();
  boolean isHome = reqUri.endsWith("/home") || reqUri.endsWith("/");
  boolean isCart = reqUri.endsWith("/cart");
  boolean isOrders = reqUri.endsWith("/orders");
%>
<nav class="navbar navbar-expand-lg navbar-dark sticky-top shadow-sm" style="background-color:#2C3E50;">
  <div class="container">
    <a class="navbar-brand fw-bold fs-4 d-flex align-items-center gap-2" href="${pageContext.request.contextPath}/home">
      <span style="font-size:1.6rem;">🍔</span> FoodExpress
    </a>
    <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarMain">
      <%-- Search Form --%>
      <form class="d-flex mx-auto my-2 my-lg-0" method="get" action="${pageContext.request.contextPath}/search">
        <div class="input-group shadow-sm">
          <input class="form-control border-0 px-3" type="search" name="keyword"
                 placeholder="Search food or restaurants..."
                 value="${not empty keyword ? keyword : ''}" style="min-width:260px; border-radius: 25px 0 0 25px;">
          <button class="btn btn-warning fw-semibold px-3" type="submit" style="border-radius: 0 25px 25px 0;">🔍</button>
        </div>
      </form>
      <%-- Right side --%>
      <ul class="navbar-nav ms-auto align-items-center gap-3">
        <li class="nav-item">
          <a class="nav-link fw-semibold <%= isHome ? "active text-warning" : "" %>" href="${pageContext.request.contextPath}/home">
            🏠 Home
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link fw-semibold position-relative <%= isCart ? "active text-warning" : "" %>" href="${pageContext.request.contextPath}/cart">
            🛒 Cart
            <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill shadow-sm"
                  style="background:#FF6B35; font-size:0.7rem; border: 2px solid #2C3E50;">
              ${not empty cartCount ? cartCount : 0}
            </span>
          </a>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle fw-semibold d-flex align-items-center gap-1" href="#" role="button" data-bs-toggle="dropdown">
            <span class="badge rounded-pill bg-warning text-dark px-2 py-1">👤 ${sessionScope.loggedUser.fullName}</span>
          </a>
          <ul class="dropdown-menu dropdown-menu-end shadow border-0 mt-2" style="border-radius: 12px;">
            <li><a class="dropdown-item py-2 fw-medium <%= isOrders ? "active" : "" %>" href="${pageContext.request.contextPath}/orders">📦 My Orders</a></li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item py-2 text-danger fw-semibold" href="${pageContext.request.contextPath}/logout">🚪 Logout</a></li>
          </ul>
        </li>
      </ul>
    </div>
  </div>
</nav>

