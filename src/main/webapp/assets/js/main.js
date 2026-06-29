/**
 * FoodExpress — main.js
 * Vanilla JavaScript — no jQuery or external libraries.
 * All functionality wrapped in DOMContentLoaded to ensure DOM readiness.
 */

document.addEventListener('DOMContentLoaded', function () {

  // ============================================================
  // SECTION 1: Auto-dismiss Bootstrap alerts after 3 seconds
  // ============================================================
  document.querySelectorAll('.alert').forEach(function (alertEl) {
    setTimeout(function () {
      try {
        var bsAlert = bootstrap.Alert.getInstance(alertEl)
                   || new bootstrap.Alert(alertEl);
        bsAlert.close();
      } catch (e) {
        // Bootstrap may not be loaded on error pages — fail silently
        alertEl.style.display = 'none';
      }
    }, 3000);
  });


  // ============================================================
  // SECTION 2: Payment method form toggle
  // Shows/hides UPI or Card field groups based on radio selection.
  // Applies to checkout.jsp and payment.jsp.
  // ============================================================
  var paymentRadios = document.querySelectorAll('input[name="paymentMethod"]');

  function togglePaymentFields(selectedValue) {
    var upiField   = document.getElementById('upi-field');
    var cardFields = document.getElementById('card-fields');

    if (upiField)   upiField.style.display   = (selectedValue === 'UPI')  ? 'block' : 'none';
    if (cardFields) cardFields.style.display  = (selectedValue === 'CARD') ? 'block' : 'none';
  }

  if (paymentRadios.length > 0) {
    // Attach change listeners
    paymentRadios.forEach(function (radio) {
      radio.addEventListener('change', function () {
        togglePaymentFields(this.value);
      });
    });

    // Trigger on page load for whichever radio is pre-selected
    var checkedRadio = document.querySelector('input[name="paymentMethod"]:checked');
    if (checkedRadio) {
      togglePaymentFields(checkedRadio.value);
    }
  }


  // ============================================================
  // SECTION 3: Cart quantity input validation
  // Prevents value below 1, prevents non-numeric input,
  // resets to 1 on blur if empty.
  // ============================================================
  document.querySelectorAll('.qty-input').forEach(function (input) {

    // Prevent typing non-numeric characters
    input.addEventListener('keypress', function (e) {
      if (!/[0-9]/.test(e.key)) {
        e.preventDefault();
      }
    });

    // Enforce minimum of 1 on change
    input.addEventListener('change', function () {
      var val = parseInt(this.value, 10);
      if (isNaN(val) || val < 1) {
        this.value = 1;
      }
    });

    // Reset to 1 if field is emptied and focus leaves
    input.addEventListener('blur', function () {
      if (this.value.trim() === '' || parseInt(this.value, 10) < 1) {
        this.value = 1;
      }
    });
  });


  // ============================================================
  // SECTION 4: Admin delete confirmation
  // Any form with class .delete-form will prompt user before submit.
  // ============================================================
  document.querySelectorAll('.delete-form').forEach(function (form) {
    form.addEventListener('submit', function (e) {
      if (!confirm('Are you sure you want to delete this item? This action cannot be undone.')) {
        e.preventDefault();
      }
    });
  });


  // ============================================================
  // SECTION 5: Credit card number formatting
  // Inserts a space every 4 digits; max 19 chars (16 digits + 3 spaces).
  // Input: "4111111111111111" → "4111 1111 1111 1111"
  // ============================================================
  var cardNumberInput = document.getElementById('cardNumber');
  if (cardNumberInput) {
    cardNumberInput.addEventListener('input', function () {
      var raw     = this.value.replace(/\D/g, '').substring(0, 16);
      var groups  = raw.match(/.{1,4}/g);
      this.value  = groups ? groups.join(' ') : raw;
    });

    // Prevent non-numeric keystrokes (allow backspace, delete, tab, arrows)
    cardNumberInput.addEventListener('keydown', function (e) {
      var allowed = ['Backspace', 'Delete', 'Tab', 'ArrowLeft', 'ArrowRight', 'Home', 'End'];
      if (!allowed.includes(e.key) && !/^\d$/.test(e.key)) {
        e.preventDefault();
      }
    });
  }


  // ============================================================
  // SECTION 6: Expiry date formatting (MM/YY)
  // Auto-inserts "/" after the 2nd digit, max 5 characters.
  // ============================================================
  var expiryInput = document.querySelector('input[name="expiry"]');
  if (expiryInput) {
    expiryInput.addEventListener('input', function (e) {
      var raw = this.value.replace(/\D/g, '').substring(0, 4);
      if (raw.length >= 3) {
        this.value = raw.substring(0, 2) + '/' + raw.substring(2);
      } else {
        this.value = raw;
      }
    });

    expiryInput.setAttribute('maxlength', '5');
    expiryInput.setAttribute('placeholder', 'MM/YY');
  }


  // ============================================================
  // SECTION 7: Bootstrap HTML5 form validation
  // Activates Bootstrap's .was-validated class to show feedback
  // on any form with class .needs-validation.
  // ============================================================
  document.querySelectorAll('.needs-validation').forEach(function (form) {
    form.addEventListener('submit', function (e) {
      if (!form.checkValidity()) {
        e.preventDefault();
        e.stopPropagation();
      }
      form.classList.add('was-validated');
    });
  });


  // ============================================================
  // SECTION 8: Search form — prevent empty submission
  // Adds a shake animation if user tries to search with blank input.
  // ============================================================
  var searchForm = document.getElementById('searchForm');
  if (searchForm) {
    searchForm.addEventListener('submit', function (e) {
      var searchInput = searchForm.querySelector('input[name="keyword"]');
      if (searchInput && searchInput.value.trim() === '') {
        e.preventDefault();
        // Shake animation to indicate empty search
        searchInput.classList.add('is-invalid');
        searchInput.style.animation = 'none';
        // Force reflow to restart animation
        void searchInput.offsetWidth;
        searchInput.style.animation = 'shake 0.4s ease';

        searchInput.addEventListener('input', function () {
          searchInput.classList.remove('is-invalid');
          searchInput.style.animation = 'none';
        }, { once: true });
      }
    });
  }


  // ============================================================
  // SECTION 9: Order status stepper builder
  // Reads the current order status from #orderStepper[data-status]
  // and applies 'completed' / 'active' CSS classes to each step.
  // ============================================================

  /**
   * Applies CSS classes to stepper step elements.
   * @param {string} currentStatus - The current order status string.
   */
  function buildStepper(currentStatus) {
    var steps = ['PLACED', 'CONFIRMED', 'PREPARING', 'OUT_FOR_DELIVERY', 'DELIVERED'];
    var currentIndex = steps.indexOf(currentStatus);

    document.querySelectorAll('.step[data-status]').forEach(function (stepEl) {
      var stepStatus = stepEl.getAttribute('data-status');
      var stepIndex  = steps.indexOf(stepStatus);

      stepEl.classList.remove('completed', 'active');

      if (stepIndex < currentIndex) {
        stepEl.classList.add('completed');
      } else if (stepIndex === currentIndex) {
        stepEl.classList.add('active');
      }
    });
  }

  // Initialise stepper on trackOrder page if the container exists
  var stepperContainer = document.getElementById('orderStepper');
  if (stepperContainer) {
    var currentStatus = stepperContainer.getAttribute('data-status');
    if (currentStatus) {
      buildStepper(currentStatus);
    }
  }


  // ============================================================
  // SECTION 10: Cart "Add to Cart" — success toast notification
  // Shows a brief Bootstrap Toast confirming item was added.
  // ============================================================

  /**
   * Creates and shows a lightweight Bootstrap toast.
   * @param {string} message - Message to display in the toast.
   */
  function showToast(message) {
    // Build toast container if it doesn't exist
    var toastContainer = document.getElementById('toastContainer');
    if (!toastContainer) {
      toastContainer = document.createElement('div');
      toastContainer.id = 'toastContainer';
      toastContainer.className = 'toast-container position-fixed bottom-0 end-0 p-3';
      toastContainer.style.zIndex = '9999';
      document.body.appendChild(toastContainer);
    }

    // Create toast element
    var toastEl = document.createElement('div');
    toastEl.className = 'toast align-items-center text-white border-0 show';
    toastEl.style.background = 'linear-gradient(135deg, #FF6B35, #e05520)';
    toastEl.style.borderRadius = '10px';
    toastEl.innerHTML =
      '<div class="d-flex">' +
        '<div class="toast-body fw-semibold">🛒 ' + message + '</div>' +
        '<button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>' +
      '</div>';

    toastContainer.appendChild(toastEl);

    // Auto-remove after 2.5 seconds
    setTimeout(function () {
      toastEl.classList.remove('show');
      setTimeout(function () { toastEl.remove(); }, 400);
    }, 2500);
  }

  // Attach to all Add to Cart forms
  document.querySelectorAll('.add-to-cart-form').forEach(function (form) {
    form.addEventListener('submit', function () {
      showToast('Added to cart!');
    });
  });

  // ============================================================
  // SECTION 11: Live Search & Instant Filtering Pills (Phase 2)
  // Filters restaurants and dishes instantly without reload.
  // ============================================================
  const liveSearchInput = document.getElementById('liveSearchInput');
  const filterPills = document.querySelectorAll('.filter-pill');
  let currentFilter = 'all';

  function applyInstantFilters() {
    const q = (liveSearchInput ? liveSearchInput.value : '').toLowerCase().trim();
    const cards = document.querySelectorAll('.restaurant-card-col, .food-card-col');

    cards.forEach(card => {
      const name = (card.getAttribute('data-name') || '').toLowerCase();
      const cat = (card.getAttribute('data-cat') || '').toLowerCase();
      const veg = card.getAttribute('data-veg') === 'true';
      const rating = parseFloat(card.getAttribute('data-rating') || '0');
      const time = parseInt(card.getAttribute('data-time') || '30', 10);
      const offer = card.getAttribute('data-offer') === 'true';

      let matchQuery = !q || name.includes(q) || cat.includes(q);
      let matchPill = true;

      if (currentFilter === 'veg') matchPill = veg;
      else if (currentFilter === 'nonveg') matchPill = !veg;
      else if (currentFilter === 'rating') matchPill = (rating >= 4.2);
      else if (currentFilter === 'fast') matchPill = (time <= 25);
      else if (currentFilter === 'offer') matchPill = offer;

      if (matchQuery && matchPill) {
        card.style.display = '';
        card.style.animation = 'fadeIn 0.3s ease';
      } else {
        card.style.display = 'none';
      }
    });
  }

  if (liveSearchInput) {
    liveSearchInput.addEventListener('input', applyInstantFilters);
  }

  if (filterPills.length > 0) {
    filterPills.forEach(pill => {
      pill.addEventListener('click', function() {
        filterPills.forEach(p => p.classList.remove('active', 'bg-dark', 'text-white'));
        this.classList.add('active', 'bg-dark', 'text-white');
        currentFilter = this.getAttribute('data-filter') || 'all';
        applyInstantFilters();
      });
    });
  }

}); // END DOMContentLoaded
