<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="jakarta.tags.core" prefix="c" %>
    <%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
      <!DOCTYPE html>
      <html lang="en">

      <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${restaurant.name} Menu — FoodExpress</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
      </head>

      <body class="d-flex flex-column min-vh-100 bg-light">
        <%@ include file="/WEB-INF/views/common/navbar.jsp" %>

          <%-- Restaurant Hero Card --%>
            <div class="bg-white border-bottom shadow-sm py-5 mb-4">
              <div class="container" style="max-width: 900px;">
                <a href="${pageContext.request.contextPath}/restaurants"
                  class="btn btn-outline-secondary rounded-pill btn-sm mb-4 px-3 fw-semibold">← Back to Stores</a>
                <div class="card border border-secondary-subtle rounded-4 p-4 shadow-sm bg-light">
                  <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3">
                    <div>
                      <h1 class="fw-bolder mb-2 text-dark display-6">${restaurant.name}</h1>
                      <div class="d-flex align-items-center flex-wrap gap-2 mb-3">
                        <span class="badge bg-dark text-white fw-bold px-3 py-1 fs-6">${restaurant.cuisineType}</span>
                        <span class="badge bg-success text-white fw-bold px-3 py-1 fs-6">⭐ ${restaurant.rating}</span>
                        <span class="badge bg-primary text-white px-3 py-1 fs-6">🕒 ${not empty restaurant.deliveryTime
                          ? restaurant.deliveryTime : '20-30 mins'}</span>
                      </div>
                      <p class="mb-2 text-secondary fs-6">${restaurant.description}</p>
                      <small class="text-muted d-block fw-medium">📍 ${restaurant.address} &nbsp;|&nbsp; 📞
                        ${restaurant.phone}</small>
                    </div>
                    <div class="text-md-end bg-white p-3 rounded-4 shadow-sm border border-secondary-subtle"
                      style="min-width: 180px;">
                      <span class="d-block small text-muted mb-1 fw-semibold">Delivery Fee</span>
                      <span class="fs-4 fw-bold text-success">₹
                        <fmt:formatNumber value="${restaurant.deliveryFee}" pattern="#,##0" />
                      </span>
                      <span class="d-block badge bg-success-subtle text-success border border-success mt-2">🟢
                        Open</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="container pb-5 flex-grow-1" style="max-width: 900px;">
              <div class="d-flex justify-content-between align-items-center mb-4">
                <h3 class="fw-bold mb-0 text-dark">Culinary Menu</h3>
                <span class="badge bg-secondary px-3 py-2 rounded-pill">${not empty foods ? foods.size() : 0}
                  Dishes</span>
              </div>

              <c:choose>
                <c:when test="${empty foods}">
                  <div class="empty-state-box my-5 text-center">
                    <div class="display-1">🍕</div>
                    <h3 class="fw-bold text-dark mt-3">No Menu Items Available Right Now</h3>
                    <p class="text-muted mb-4">The chef is preparing a brand new menu for this outlet. Please check back
                      soon!</p>
                    <a href="${pageContext.request.contextPath}/restaurants"
                      class="btn btn-primary px-4 py-2 rounded-pill fw-bold">🏪 Browse Other Outlets</a>
                  </div>
                </c:when>
                <c:otherwise>
                  <c:set var="currentCategory" value="" />
                  <c:forEach var="food" items="${foods}">
                    <c:if test="${food.category != currentCategory}">
                      <c:if test="${not empty currentCategory}">
            </div>
            </c:if>
            <c:set var="currentCategory" value="${food.category}" />
            <h4 class="mt-4 mb-3 fw-bold border-bottom pb-2 text-dark d-flex align-items-center gap-2">
              <span>🍴</span> ${food.category}
            </h4>
            <div class="row row-cols-1 g-3 mb-3">
              </c:if>

              <div class="col food-card-col" data-name="${food.name}" data-cat="${food.category}"
                data-veg="${food.veg}">
                <div class="card shadow-sm border border-secondary-subtle rounded-4 p-4 bg-white">
                  <div class="d-flex justify-content-between align-items-center gap-3">
                    <div class="flex-grow-1">
                      <div class="d-flex align-items-center gap-2 mb-1">
                        <span class="fs-6">${food.veg ? '🟢' : '🔴'}</span>
                        <h5 class="fw-bold mb-0 text-dark">${food.name}</h5>
                        <c:if test="${food.price > 250}">
                          <span class="badge bg-warning-subtle text-dark border border-warning small">★
                            Bestseller</span>
                        </c:if>
                      </div>
                      <span class="fs-5 fw-bold text-dark d-block mb-2">₹
                        <fmt:formatNumber value="${food.price}" pattern="#,##0.00" />
                      </span>
                      <p class="small text-muted mb-0">${food.description}</p>
                    </div>
                    <div>
                      <c:choose>
                        <c:when test="${!food.available}">
                          <span class="badge bg-secondary px-3 py-2 rounded-pill">Out of Stock</span>
                        </c:when>
                        <c:otherwise>
                          <form method="post" action="${pageContext.request.contextPath}/cart"
                            class="d-flex flex-column align-items-center gap-1 m-0 bg-light p-2 rounded-3 border">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="foodId" value="${food.foodId}">
                            <div class="d-flex align-items-center gap-1">
                              <input type="number" name="quantity" value="1" min="1" max="10"
                                class="form-control form-control-sm qty-input text-center shadow-none border fw-bold"
                                style="width:48px; border-radius: 6px;">
                              <button type="submit"
                                class="btn btn-sm btn-success px-3 py-1 rounded fw-bold shadow-sm">ADD</button>
                            </div>
                          </form>
                        </c:otherwise>
                      </c:choose>
                    </div>
                  </div>
                </div>
              </div>
              </c:forEach>
              <c:if test="${not empty currentCategory}">
            </div>
            </c:if>
            </c:otherwise>
            </c:choose>

            <%--=====CUSTOMER REVIEWS SECTION=====--%>
              <div class="mt-5 pt-4 border-top">
                <h3 class="fw-bold mb-4 text-dark">⭐ Customer Ratings & Reviews (${reviews.size()})</h3>
                <c:choose>
                  <c:when test="${empty reviews}">
                    <p class="text-muted small">No reviews yet for this outlet. Be the first to order and leave
                      feedback!</p>
                  </c:when>
                  <c:otherwise>
                    <div class="d-flex flex-column gap-3">
                      <c:forEach var="rev" items="${reviews}">
                        <div class="card p-4 border border-secondary-subtle rounded-4 bg-white shadow-sm">
                          <div class="d-flex justify-content-between align-items-center mb-2">
                            <h6 class="fw-bold m-0 text-dark">👤 ${rev.userName}</h6>
                            <span class="badge bg-success small">⭐ ${rev.rating} / 5</span>
                          </div>
                          <p class="small text-secondary mb-2 m-0">${rev.comment}</p>
                          <small class="text-muted" style="font-size:0.75rem;">
                            <fmt:formatDate value="${rev.createdAt}" pattern="dd MMM yyyy, hh:mm a" />
                          </small>
                        </div>
                      </c:forEach>
                    </div>
                  </c:otherwise>
                </c:choose>
              </div>
              </div>

              <%-- Swiggy Cart Restriction Modal --%>
                <c:if test="${not empty sessionScope.diffRestaurantModal}">
                  <div class="modal fade show d-block" id="diffRestaurantModal" tabindex="-1"
                    style="background: rgba(0,0,0,0.6);">
                    <div class="modal-dialog modal-dialog-centered">
                      <div class="modal-content rounded-4 shadow-lg border-0 p-3">
                        <div class="modal-header border-0 pb-0">
                          <h5 class="modal-title fw-bold text-dark fs-4">Items already in cart</h5>
                          <button type="button" class="btn-close"
                            onclick="window.location.href='${pageContext.request.contextPath}/menu?restaurantId=${restaurant.restaurantId}'"></button>
                        </div>
                        <div class="modal-body py-3 text-secondary">
                          Your cart contains dishes from <strong>
                            <c:out value="${sessionScope.currentRestName}" />
                          </strong>. Would you like to reset your cart for adding dishes from this outlet?
                        </div>
                        <div class="modal-footer border-0 pt-0 d-flex gap-2">
                          <button type="button"
                            class="btn btn-outline-secondary flex-grow-1 rounded-pill py-2 fw-semibold"
                            onclick="window.location.href='${pageContext.request.contextPath}/menu?restaurantId=${restaurant.restaurantId}'">NO</button>
                          <form method="post" action="${pageContext.request.contextPath}/cart"
                            class="flex-grow-1 m-0 d-flex">
                            <input type="hidden" name="action" value="clearAndAdd">
                            <button type="submit"
                              class="btn btn-primary flex-grow-1 rounded-pill py-2 fw-bold shadow-sm">YES, START
                              AFRESH</button>
                          </form>
                        </div>
                      </div>
                    </div>
                  </div>
                  <c:remove var="diffRestaurantModal" scope="session" />
                </c:if>

                <footer class="footer mt-auto py-4">
                  <div class="container text-center">
                    <p class="mb-0 text-white-50">© <%= java.time.Year.now() %> FoodExpress</p>
                  </div>
                </footer>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
                <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
      </body>

      </html>