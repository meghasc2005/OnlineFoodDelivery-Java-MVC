<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Page Not Found (404) — FoodExpress</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="bg-light d-flex flex-column min-vh-100">
  <div class="container text-center my-auto py-5">
    <div class="display-1 mb-3">🛸</div>
    <h1 class="fw-bold text-dark display-4 mb-3">404 - Delicious Page Not Found</h1>
    <p class="text-muted fs-5 mb-4 mx-auto" style="max-width: 500px;">
      Oops! It seems the recipe or page you are looking for has been moved or eaten.
    </p>
    <a href="${pageContext.request.contextPath}/home" class="btn btn-primary btn-lg rounded-pill px-5 py-3 fw-bold shadow">
      🍔 Return to Homepage
    </a>
  </div>
</body>
</html>
