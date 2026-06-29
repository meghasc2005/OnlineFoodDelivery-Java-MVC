<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>TAX INVOICE #${not empty order ? order.orderId : ''} — FoodExpress</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <style>
    body {
      background: #f8f9fa;
      font-family: 'Courier New', Courier, monospace;
      color: #000;
    }
    .invoice-container {
      max-width: 800px;
      margin: 40px auto;
      background: #fff;
      padding: 40px;
      border: 1px solid #dee2e6;
      box-shadow: 0 4px 12px rgba(0,0,0,0.05);
    }
    .invoice-title {
      font-size: 24px;
      font-weight: bold;
      letter-spacing: 2px;
      border-bottom: 2px solid #000;
      padding-bottom: 10px;
      margin-bottom: 20px;
    }
    .table th, .table td {
      border-color: #000 !important;
      padding: 10px;
    }
    @media print {
      body { background: #fff; }
      .no-print { display: none !important; }
      .invoice-container { margin: 0; border: none; box-shadow: none; width: 100%; max-width: 100%; padding: 10px; }
    }
  </style>
</head>
<body>

  <div class="text-center my-4 no-print">
    <button onclick="window.print();" class="btn btn-dark px-5 py-2 fw-bold shadow">🖨️ Print Receipt / Save PDF</button>
    <button onclick="window.close();" class="btn btn-outline-secondary px-4 py-2 ms-2">Close</button>
  </div>

  <div class="invoice-container">
    <div class="d-flex justify-content-between align-items-start mb-4">
      <div>
        <h2 class="fw-bolder m-0">FOOD EXPRESS LTD</h2>
        <p class="small m-0">HQ: MG Road, Bangalore - 560001</p>
        <p class="small m-0">GSTIN: 29AAAAA0000A1Z5</p>
        <p class="small m-0">Support: care@foodexpress.in | 1800-123-456</p>
      </div>
      <div class="text-end">
        <h3 class="invoice-title m-0">TAX INVOICE</h3>
        <p class="m-0 fw-bold">Order Ref: #${not empty order ? order.orderId : ''}</p>
        <p class="small m-0">Date: <c:if test="${not empty order && not empty order.createdAt}"><fmt:formatDate value="${order.createdAt}" pattern="dd-MM-yyyy HH:mm:ss"/></c:if></p>
      </div>
    </div>

    <div class="row mb-4 pt-3 border-top border-bottom py-3">
      <div class="col-6">
        <p class="small fw-bold text-uppercase text-muted m-0">Billed To (Customer):</p>
        <p class="fw-bold m-0">${not empty user ? user.fullName : 'Valued Customer'}</p>
        <p class="small m-0">${not empty order ? order.deliveryAddress : ''}</p>
        <p class="small m-0">Phone: ${not empty user ? user.phone : ''}</p>
      </div>
      <div class="col-6 text-end">
        <p class="small fw-bold text-uppercase text-muted m-0">Fulfilled By (Restaurant):</p>
        <c:if test="${not empty restaurant}">
          <p class="fw-bold m-0">${restaurant.name}</p>
          <p class="small m-0">${restaurant.address}</p>
          <p class="small m-0">Phone: ${restaurant.phone}</p>
        </c:if>
        <c:if test="${empty restaurant}">
          <p class="fw-bold m-0">Partner Kitchen Outlet</p>
        </c:if>
      </div>
    </div>

    <table class="table table-bordered mb-4">
      <thead class="table-light text-center fw-bold">
        <tr>
          <th>#</th>
          <th class="text-start">Dish Description</th>
          <th>Unit Price</th>
          <th>Qty</th>
          <th class="text-end">Net Amount</th>
        </tr>
      </thead>
      <tbody>
        <c:set var="subtot" value="0"/>
        <c:if test="${not empty orderItems}">
          <c:forEach var="item" items="${orderItems}" varStatus="loop">
            <c:set var="subtot" value="${subtot + item.itemTotal}"/>
            <tr>
              <td class="text-center">${loop.count}</td>
              <td>
                <strong>${not empty item.food ? item.food.name : 'Delicacy Dish Item'}</strong>
                <span class="small d-block text-muted">(${not empty item.food && item.food.veg ? 'Pure Veg' : 'Non-Veg'})</span>
              </td>
              <td class="text-center">₹<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/></td>
              <td class="text-center">${item.quantity}</td>
              <td class="text-end fw-bold">₹<fmt:formatNumber value="${item.itemTotal}" pattern="#,##0.00"/></td>
            </tr>
          </c:forEach>
        </c:if>
      </tbody>
    </table>

    <div class="row justify-content-end mb-4">
      <div class="col-md-6">
        <table class="table table-borderless small m-0">
          <tr>
            <td>Item Subtotal:</td>
            <td class="text-end fw-bold">₹<fmt:formatNumber value="${subtot}" pattern="#,##0.00"/></td>
          </tr>
          <tr>
            <td>GST / Taxes (5%):</td>
            <td class="text-end fw-bold">₹<fmt:formatNumber value="${not empty order ? order.gstAmount : 0}" pattern="#,##0.00"/></td>
          </tr>
          <tr>
            <td>Platform Fee:</td>
            <td class="text-end fw-bold">₹<fmt:formatNumber value="${not empty order ? order.platformFee : 0}" pattern="#,##0.00"/></td>
          </tr>
          <tr>
            <td>Delivery Charge:</td>
            <td class="text-end fw-bold">₹<fmt:formatNumber value="${not empty order ? order.deliveryFee : 0}" pattern="#,##0.00"/></td>
          </tr>
          <c:if test="${not empty order && order.discountAmount > 0}">
            <tr class="text-success">
              <td>Coupon Discount (${order.couponCode}):</td>
              <td class="text-end fw-bold">-₹<fmt:formatNumber value="${order.discountAmount}" pattern="#,##0.00"/></td>
            </tr>
          </c:if>
          <tr class="border-top border-bottom fs-6 fw-bolder">
            <td class="py-2">TOTAL AMOUNT PAID:</td>
            <td class="py-2 text-end">₹<fmt:formatNumber value="${not empty order ? order.totalAmount : 0}" pattern="#,##0.00"/></td>
          </tr>
        </table>
      </div>
    </div>

    <div class="border p-3 rounded mb-4 small bg-light d-flex justify-content-between align-items-center flex-wrap gap-2">
      <div>
        <strong>Payment Mode:</strong> ${not empty order && not empty order.paymentMethod ? order.paymentMethod : (not empty payment ? payment.paymentMethod : 'COD')} &nbsp;|&nbsp; <strong>Status:</strong> ${not empty payment && not empty payment.status ? payment.status : 'COMPLETED'}
      </div>
      <c:if test="${not empty payment && not empty payment.transactionId}">
        <div>
          <strong>Bank Trans Ref:</strong> <span class="font-monospace">${payment.transactionId}</span>
        </div>
      </c:if>
    </div>

    <div class="text-center pt-3 border-top small text-muted">
      <p class="m-0">Thank you for dining with FoodExpress! This is a computer-generated tax invoice.</p>
    </div>
  </div>

</body>
</html>
