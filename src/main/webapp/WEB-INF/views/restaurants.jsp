<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="jakarta.tags.core" prefix="c" %>
    <%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
      <!DOCTYPE html>
      <html lang="en">

      <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>All Restaurants — FoodExpress</title>
        <meta name="description" content="Browse all restaurants and search for your favourite food.">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
      </head>

      <body class="d-flex flex-column min-vh-100 bg-light">
        <%@ include file="/WEB-INF/views/common/navbar.jsp" %>

          <div class="page-header text-center py-5">
            <div class="container">
              <c:if test="${not empty keyword}">
                <h1 class="fw-bold display-6 mb-1">
                  🔍 Search results for: "<span class="text-warning">
                    <c:out value="${keyword}" />
                  </span>"
                </h1>
              </c:if>
              <c:if test="${empty keyword}">
                <h1 class="fw-bold display-6 mb-1">🏪 All Restaurant Outlets</h1>
                <p class="lead opacity-90 mb-0">Discover top rated culinary destinations near your location</p>
              </c:if>
            </div>
          </div>

          <div class="container py-5 flex-grow-1">

            <%--=====SEARCH RESULTS VIEW=====--%>
              <c:if test="${not empty keyword}">
                <a href="${pageContext.request.contextPath}/restaurants"
                  class="btn btn-outline-secondary rounded-pill mb-4 px-4 fw-semibold">← Back to All Outlets</a>

                <c:choose>
                  <c:when test="${empty searchResults}">
                    <div class="empty-state-box my-5 text-center">
                      <div class="display-1">😕</div>
                      <h3 class="fw-bold text-dark mt-3">No Delicacies Found Matching "
                        <c:out value="${keyword}" />"
                      </h3>
                      <p class="text-muted mb-4">Try searching for generic terms like Burger, Pizza, Biryani, or
                        Noodles.</p>
                      <a href="${pageContext.request.contextPath}/restaurants"
                        class="btn btn-primary px-5 py-2 rounded-pill fw-bold">🏪 View All Outlets</a>
                    </div>
                  </c:when>
                  <c:otherwise>
                    <div class="d-flex justify-content-between align-items-center mb-4">
                      <h3 class="fw-bold mb-0 text-dark">Found ${searchResults.size()} Food Item(s)</h3>
                    </div>
                    <div class="row row-cols-1 row-cols-md-3 g-4">
                      <c:forEach var="food" items="${searchResults}">
                        <div class="col food-card-col" data-name="${food.name}" data-cat="${food.category}"
                          data-veg="${food.veg}">
                          <div
                            class="card food-card h-100 shadow-sm border border-secondary-subtle rounded-4 p-4 d-flex flex-column justify-content-between bg-white">
                            <div>
                              <div class="d-flex justify-content-between align-items-start mb-2">
                                <h6 class="fw-bold mb-0 text-dark fs-5">${food.name}</h6>
                                <span
                                  class="badge ${food.veg ? 'bg-success-subtle text-success border border-success' : 'bg-danger-subtle text-danger border border-danger'} small">
                                  ${food.veg ? '🟢 VEG' : '🔴 NON-VEG'}
                                </span>
                              </div>
                              <span class="badge bg-warning-subtle text-dark border small mb-2">${food.category}</span>
                              <p class="small text-muted mb-3">${food.description}</p>
                            </div>
                            <div class="d-flex justify-content-between align-items-center pt-3 border-top">
                              <span class="fs-5 fw-bold text-dark">₹
                                <fmt:formatNumber value="${food.price}" pattern="#,##0.00" />
                              </span>
                              <a href="${pageContext.request.contextPath}/menu?restaurantId=${food.restaurantId}"
                                class="btn btn-sm btn-dark rounded-pill px-4 fw-bold">View Store →</a>
                            </div>
                          </div>
                        </div>
                      </c:forEach>
                    </div>
                  </c:otherwise>
                </c:choose>
              </c:if>

              <%--=====RESTAURANT LISTING VIEW (no search)=====--%>
                <c:if test="${empty keyword}">
                  <%-- Live Search & Filter Bar --%>
                    <div class="mx-auto mb-5" style="max-width: 700px;">
                      <input type="text" id="liveSearchInput"
                        class="form-control rounded-pill px-4 py-3 shadow-sm mb-3 border-secondary"
                        placeholder="🔍 Instant search by store name or cuisine...">
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

                    <c:choose>
                      <c:when test="${empty restaurants}">
                        <div class="empty-state-box my-5 text-center">
                          <div class="display-1">🍽️</div>
                          <h3 class="fw-bold text-dark mt-3">No Restaurants Available Right Now</h3>
                          <p class="text-muted mb-4">Please check back later!</p>
                          <a href="${pageContext.request.contextPath}/home"
                            class="btn btn-primary px-4 py-2 rounded-pill fw-bold">🏠 Go Home</a>
                        </div>
                      </c:when>
                      <c:otherwise>
                        <div class="row row-cols-1 row-cols-md-3 g-4">
                          <c:forEach var="restaurant" items="${restaurants}">
                            <div class="col restaurant-card-col" data-name="${restaurant.name}"
                              data-cat="${restaurant.cuisineType}" data-veg="${restaurant.cuisineType.contains('Veg')}"
                              data-rating="${restaurant.rating}" data-time="25"
                              data-offer="${restaurant.rating >= 4.5}">
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
                                    <span
                                      class="badge bg-success-subtle text-success border border-success-subtle px-2">🟢
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
                </c:if>

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