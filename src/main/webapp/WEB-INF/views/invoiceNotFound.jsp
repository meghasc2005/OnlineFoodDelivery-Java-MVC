<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Invoice Not Found — FoodExpress</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light d-flex align-items-center justify-content-center min-vh-100 font-monospace">
  <div class="text-center p-5 bg-white rounded-4 shadow border border-secondary-subtle" style="max-width: 600px;">
    <div style="font-size: 4rem;" class="mb-3">📄❌</div>
    <h3 class="fw-bold text-dark mb-2">Invoice Document Not Found</h3>
    <p class="text-secondary small mb-4">
      We couldn't locate the tax receipt for Order #${param.orderId}. The order record may not exist in our database, or you do not have permission to access this billing document.
    </p>
    <div class="d-flex justify-content-center gap-3">
      <a href="${pageContext.request.contextPath}/orders" class="btn btn-dark px-4 py-2 fw-bold">← My Orders</a>
      <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary px-4 py-2">Home</a>
    </div>
  </div>
</body>
</html>
