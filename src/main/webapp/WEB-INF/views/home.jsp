<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="jakarta.tags.core" prefix="c" %>
    <%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
      <!DOCTYPE html>
      <html lang="en">

      <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Home — FoodExpress</title>
        <meta name="description" content="Order food from top restaurants on FoodExpress.">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
      </head>

      <body class="d-flex flex-column min-vh-100 bg-light">
        <%@ include file="/WEB-INF/views/common/navbar.jsp" %>

          <%-- Hero Banner --%>
            <div class="page-header text-center py-5">
              <div class="container">
                <h1 class="fw-bolder display-6 mb-2">Good day,
                  <c:out value="${sessionScope.loggedUser.fullName}" />! 🎉
                </h1>
                <p class="lead opacity-90 mb-0">What delicious feast are you craving today? Explore top culinary
                  destinations.</p>
              </div>
            </div>

            <div class="container py-5 flex-grow-1">

              <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show shadow-sm rounded-4">
                  <c:out value="${error}" />
                  <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
              </c:if>

              <%-- Live Search & Filter Bar --%>
                <div class="mx-auto mb-5" style="max-width: 700px;">
                  <input type="text" id="liveSearchInput"
                    class="form-control rounded-pill px-4 py-3 shadow-sm mb-3 border-secondary"
                    placeholder="🔍 Instant search by store name or cuisine (e.g. Pizza, Biryani)...">
                  <div class="d-flex gap-2 flex-wrap justify-content-center">
                    <button type="button"
                      class="btn btn-outline-dark rounded-pill px-3 py-1 filter-pill active bg-dark text-white fw-bold small"
                      data-filter="all">🌟 All Stores</button>
                    <button type="button"
                      class="btn btn-outline-success rounded-pill px-3 py-1 filter-pill fw-bold small"
                      data-filter="veg">🟢 Pure Veg</button>
                    <button type="button"
                      class="btn btn-outline-danger rounded-pill px-3 py-1 filter-pill fw-bold small"
                      data-filter="nonveg">🔴 Non-Veg</button>
                    <button type="button"
                      class="btn btn-outline-warning text-dark rounded-pill px-3 py-1 filter-pill fw-bold small"
                      data-filter="rating">⭐ 4.2+ Rated</button>
                    <button type="button"
                      class="btn btn-outline-primary rounded-pill px-3 py-1 filter-pill fw-bold small"
                      data-filter="fast">⚡ Fast (<25m)< /button>
                        <button type="button"
                          class="btn btn-outline-info text-dark rounded-pill px-3 py-1 filter-pill fw-bold small"
                          data-filter="offer">🔥 Offers</button>
                  </div>
                </div>

                <div class="d-flex justify-content-between align-items-center mb-4">
                  <h3 class="fw-bold mb-0 text-dark">Top Outlets Near You</h3>
                  <span class="badge bg-secondary px-3 py-2 rounded-pill">${not empty restaurants ? restaurants.size() :
                    0} Outlets</span>
                </div>

                <c:choose>
                  <c:when test="${empty restaurants}">
                    <div class="empty-state-box my-5 text-center">
                      <div class="display-1">🍽️</div>
                      <h3 class="fw-bold text-dark mt-3">No Restaurants Available Right Now</h3>
                      <p class="text-muted mb-4">Our delivery partners are currently offline. Please check back shortly!
                      </p>
                      <a href="${pageContext.request.contextPath}/home"
                        class="btn btn-primary px-4 py-2 rounded-pill fw-bold">🔄 Refresh Page</a>
                    </div>
                  </c:when>
                  <c:otherwise>
                    <div class="row row-cols-1 row-cols-md-3 g-4">
                      <c:forEach var="restaurant" items="${restaurants}">
                        <div class="col restaurant-card-col" data-name="${restaurant.name}"
                          data-cat="${restaurant.cuisineType}" data-veg="${restaurant.cuisineType.contains('Veg')}"
                          data-rating="${restaurant.rating}" data-time="25" data-offer="${restaurant.rating >= 4.5}">
                          <div
                            class="card restaurant-card h-100 shadow-sm border border-secondary-subtle rounded-4 p-4 d-flex flex-column justify-content-between bg-white">
                            <div>
                              <div class="d-flex justify-content-between align-items-start mb-2">
                                <div>
                                  <h5 class="fw-bold text-dark mb-1">${restaurant.name}</h5>
                                  <span
                                    class="badge bg-light text-secondary border small">${restaurant.cuisineType}</span>
                                </div>
                                <span class="badge bg-success text-white px-2 py-1 fw-bold small shadow-sm">⭐
                                  ${restaurant.rating}</span>
                              </div>
                              <p class="text-muted small mb-3"
                                style="display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">
                                ${restaurant.description}
                              </p>
                            </div>

                            <div>
                              <div
                                class="d-flex justify-content-between align-items-center py-2 border-top border-bottom mb-3 small fw-semibold text-secondary">
                                <span>🕒 ${not empty restaurant.deliveryTime ? restaurant.deliveryTime : '20-30
                                  mins'}</span>
                                <span>🛵 ${restaurant.deliveryFee == 0 ? '<span class=\"text-success\">Free
                                    Delivery</span>' : '₹'.concat(restaurant.deliveryFee)}</span>
                                <span class="badge bg-success-subtle text-success border border-success-subtle px-2">🟢
                                  Open</span>
                              </div>

                              <c:if test="${restaurant.rating >= 4.5}">
                                <div
                                  class="badge bg-danger-subtle text-danger border border-danger-subtle w-100 py-2 mb-3 small text-center fw-bold">
                                  🔥 Flat 20% OFF on all orders above ₹299
                                </div>
                              </c:if>

                              <a href="${pageContext.request.contextPath}/menu?restaurantId=${restaurant.restaurantId}"
                                class="btn btn-dark w-100 rounded-pill fw-bold py-2">Explore Menu →</a>
                            </div>
                          </div>
                        </div>
                      </c:forEach>
                    </div>
                  </c:otherwise>
                </c:choose>
            </div>

            <footer class="footer mt-auto py-4">
              <div class="container text-center">
                <p class="mb-0 text-white-50">© <%= java.time.Year.now() %> FoodExpress </p>
              </div>
            </footer>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
            <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
      </body>

      </html>