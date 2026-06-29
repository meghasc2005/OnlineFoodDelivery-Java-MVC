<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
    // If user is already logged in, redirect to home immediately
    Object loggedUser = session.getAttribute("loggedUser");
    if (loggedUser != null) {
        response.sendRedirect(request.getContextPath() + "/home");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="FoodExpress — Order from the best restaurants near you. Fast delivery, easy payment, wide selection.">
    <title>FoodExpress 🍔 — Hungry? We've Got You Covered.</title>

    <!-- Bootstrap 5.3 CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">

    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
          rel="stylesheet">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700;800&display=swap"
          rel="stylesheet">

    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">

    <style>
        :root {
            --primary:    #FF6B35;
            --secondary:  #2C3E50;
            --accent:     #F7C948;
            --bg:         #F8F9FA;
            --success:    #28A745;
            --danger:     #DC3545;
        }

        * { font-family: 'Outfit', sans-serif; }

        /* ── Navbar ── */
        .navbar-brand {
            font-size: 1.6rem;
            font-weight: 800;
            color: #fff !important;
            letter-spacing: -0.5px;
        }

        /* ── Hero ── */
        .hero-section {
            background: linear-gradient(135deg, #FF6B35 0%, #e8551f 50%, #2C3E50 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            position: relative;
            overflow: hidden;
        }

        .hero-section::before {
            content: '';
            position: absolute;
            width: 600px;
            height: 600px;
            background: rgba(247, 201, 72, 0.12);
            border-radius: 50%;
            top: -150px;
            right: -150px;
            animation: float 6s ease-in-out infinite;
        }

        .hero-section::after {
            content: '';
            position: absolute;
            width: 400px;
            height: 400px;
            background: rgba(255, 255, 255, 0.06);
            border-radius: 50%;
            bottom: -100px;
            left: -100px;
            animation: float 8s ease-in-out infinite reverse;
        }

        @keyframes float {
            0%, 100% { transform: translateY(0px); }
            50%       { transform: translateY(-30px); }
        }

        .hero-content {
            position: relative;
            z-index: 2;
        }

        .hero-heading {
            font-size: clamp(2.5rem, 5vw, 4rem);
            font-weight: 800;
            color: #fff;
            line-height: 1.15;
            text-shadow: 0 4px 20px rgba(0,0,0,0.2);
        }

        .hero-subheading {
            font-size: clamp(1rem, 2vw, 1.35rem);
            font-weight: 400;
            color: rgba(255,255,255,0.85);
            margin-top: 1rem;
        }

        .hero-badge {
            display: inline-block;
            background: var(--accent);
            color: var(--secondary);
            font-weight: 700;
            font-size: 0.8rem;
            padding: 6px 16px;
            border-radius: 50px;
            margin-bottom: 1.2rem;
            letter-spacing: 1px;
            text-transform: uppercase;
        }

        .btn-primary-custom {
            background: #fff;
            color: var(--primary);
            border: none;
            font-weight: 700;
            padding: 14px 36px;
            border-radius: 50px;
            font-size: 1rem;
            transition: all 0.3s ease;
            box-shadow: 0 8px 30px rgba(0,0,0,0.15);
        }

        .btn-primary-custom:hover {
            background: var(--accent);
            color: var(--secondary);
            transform: translateY(-3px);
            box-shadow: 0 12px 40px rgba(0,0,0,0.2);
        }

        .btn-outline-custom {
            background: transparent;
            color: #fff;
            border: 2px solid rgba(255,255,255,0.7);
            font-weight: 600;
            padding: 12px 34px;
            border-radius: 50px;
            font-size: 1rem;
            transition: all 0.3s ease;
        }

        .btn-outline-custom:hover {
            background: rgba(255,255,255,0.15);
            border-color: #fff;
            color: #fff;
            transform: translateY(-3px);
        }

        .hero-emoji {
            font-size: clamp(6rem, 15vw, 12rem);
            animation: bounce 2s ease-in-out infinite;
            display: block;
            filter: drop-shadow(0 20px 40px rgba(0,0,0,0.2));
        }

        @keyframes bounce {
            0%, 100% { transform: translateY(0); }
            50%       { transform: translateY(-20px); }
        }

        /* ── Features Section ── */
        .features-section {
            background: #fff;
            padding: 80px 0;
        }

        .section-title {
            font-size: 2.2rem;
            font-weight: 800;
            color: var(--secondary);
            text-align: center;
            margin-bottom: 0.5rem;
        }

        .section-subtitle {
            text-align: center;
            color: #6c757d;
            font-size: 1.05rem;
            margin-bottom: 3rem;
        }

        .feature-card {
            border: none;
            border-radius: 20px;
            padding: 2.5rem 2rem;
            text-align: center;
            background: var(--bg);
            transition: all 0.35s ease;
            height: 100%;
            position: relative;
            overflow: hidden;
        }

        .feature-card::before {
            content: '';
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            height: 4px;
            background: linear-gradient(90deg, var(--primary), var(--accent));
            transform: scaleX(0);
            transition: transform 0.35s ease;
        }

        .feature-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 20px 60px rgba(255,107,53,0.15);
        }

        .feature-card:hover::before {
            transform: scaleX(1);
        }

        .feature-icon {
            font-size: 3.5rem;
            margin-bottom: 1.2rem;
            display: block;
            animation: pulse-icon 3s ease-in-out infinite;
        }

        .feature-card:nth-child(2) .feature-icon { animation-delay: 0.5s; }
        .feature-card:nth-child(3) .feature-icon { animation-delay: 1s; }

        @keyframes pulse-icon {
            0%, 100% { transform: scale(1); }
            50%       { transform: scale(1.1); }
        }

        .feature-title {
            font-size: 1.3rem;
            font-weight: 700;
            color: var(--secondary);
            margin-bottom: 0.8rem;
        }

        .feature-text {
            color: #6c757d;
            font-size: 0.95rem;
            line-height: 1.6;
        }

        /* ── Stats Bar ── */
        .stats-bar {
            background: linear-gradient(135deg, var(--secondary) 0%, #1a252f 100%);
            padding: 50px 0;
        }

        .stat-item {
            text-align: center;
            color: #fff;
        }

        .stat-number {
            font-size: 2.5rem;
            font-weight: 800;
            color: var(--accent);
            display: block;
            line-height: 1;
        }

        .stat-label {
            font-size: 0.9rem;
            color: rgba(255,255,255,0.7);
            margin-top: 0.3rem;
        }

        /* ── Footer ── */
        .footer {
            background: var(--secondary);
            color: rgba(255,255,255,0.7);
            padding: 24px 0;
            text-align: center;
            font-size: 0.9rem;
        }

        .footer a {
            color: var(--accent);
            text-decoration: none;
        }

        /* ── Navbar Custom ── */
        .navbar-custom {
            background: rgba(44, 62, 80, 0.95);
            backdrop-filter: blur(10px);
            border-bottom: 1px solid rgba(255,255,255,0.1);
            padding: 14px 0;
        }

        .btn-nav-login {
            background: transparent;
            color: #fff;
            border: 1.5px solid rgba(255,255,255,0.6);
            border-radius: 50px;
            padding: 7px 22px;
            font-weight: 600;
            font-size: 0.9rem;
            transition: all 0.25s;
        }

        .btn-nav-login:hover {
            background: rgba(255,255,255,0.12);
            border-color: #fff;
            color: #fff;
        }

        .btn-nav-register {
            background: var(--primary);
            color: #fff;
            border: none;
            border-radius: 50px;
            padding: 7px 22px;
            font-weight: 600;
            font-size: 0.9rem;
            transition: all 0.25s;
        }

        .btn-nav-register:hover {
            background: #e8551f;
            color: #fff;
            transform: translateY(-1px);
        }
    </style>
</head>
<body>

    <!-- ===== Navbar ===== -->
    <nav class="navbar navbar-expand-lg navbar-custom fixed-top" id="mainNav">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                🍔 FoodExpress
            </a>
            <div class="d-flex gap-2 ms-auto">
                <a id="btnNavLogin"
                   href="${pageContext.request.contextPath}/login"
                   class="btn btn-nav-login">
                    Sign In
                </a>
                <a id="btnNavRegister"
                   href="${pageContext.request.contextPath}/register"
                   class="btn btn-nav-register">
                    Register
                </a>
            </div>
        </div>
    </nav>

    <!-- ===== Hero Section ===== -->
    <section class="hero-section" id="heroSection">
        <div class="container">
            <div class="row align-items-center g-5">

                <div class="col-lg-7 hero-content">
                    <span class="hero-badge">🚀 Fast &amp; Reliable Delivery</span>
                    <h1 class="hero-heading">
                        Hungry? We've Got<br>You Covered.
                    </h1>
                    <p class="hero-subheading">
                        Order from the best restaurants near you.<br>
                        Fresh food delivered to your doorstep in minutes.
                    </p>
                    <div class="d-flex flex-wrap gap-3 mt-4">
                        <a id="btnOrderNow"
                           href="${pageContext.request.contextPath}/register"
                           class="btn btn-primary-custom">
                            🍕 Order Now
                        </a>
                        <a id="btnSignIn"
                           href="${pageContext.request.contextPath}/login"
                           class="btn btn-outline-custom">
                            Sign In →
                        </a>
                    </div>
                </div>

                <div class="col-lg-5 text-center d-none d-lg-block">
                    <span class="hero-emoji">🍔</span>
                </div>

            </div>
        </div>
    </section>

    <!-- ===== Stats Bar ===== -->
    <section class="stats-bar">
        <div class="container">
            <div class="row g-4">
                <div class="col-6 col-md-3">
                    <div class="stat-item">
                        <span class="stat-number">500+</span>
                        <div class="stat-label">Restaurants</div>
                    </div>
                </div>
                <div class="col-6 col-md-3">
                    <div class="stat-item">
                        <span class="stat-number">50K+</span>
                        <div class="stat-label">Happy Customers</div>
                    </div>
                </div>
                <div class="col-6 col-md-3">
                    <div class="stat-item">
                        <span class="stat-number">30 min</span>
                        <div class="stat-label">Avg Delivery Time</div>
                    </div>
                </div>
                <div class="col-6 col-md-3">
                    <div class="stat-item">
                        <span class="stat-number">4.8 ⭐</span>
                        <div class="stat-label">Average Rating</div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- ===== Features Section ===== -->
    <section class="features-section" id="featuresSection">
        <div class="container">
            <h2 class="section-title">Why Choose FoodExpress?</h2>
            <p class="section-subtitle">Everything you need for a perfect meal experience</p>

            <div class="row g-4">

                <!-- Card 1: Wide Selection -->
                <div class="col-md-4">
                    <div class="feature-card">
                        <span class="feature-icon">🍕</span>
                        <div class="feature-title">Wide Selection</div>
                        <p class="feature-text">
                            Browse hundreds of dishes from top-rated restaurants.
                            From local favourites to gourmet cuisine — all in one place.
                        </p>
                    </div>
                </div>

                <!-- Card 2: Fast Delivery -->
                <div class="col-md-4">
                    <div class="feature-card">
                        <span class="feature-icon">⚡</span>
                        <div class="feature-title">Fast Delivery</div>
                        <p class="feature-text">
                            Hot and fresh food delivered straight to your doorstep
                            in minutes. Real-time tracking keeps you in the loop.
                        </p>
                    </div>
                </div>

                <!-- Card 3: Easy Payment -->
                <div class="col-md-4">
                    <div class="feature-card">
                        <span class="feature-icon">💳</span>
                        <div class="feature-title">Easy Payment</div>
                        <p class="feature-text">
                            Pay your way — Cash on Delivery, UPI, or Card.
                            Secure, seamless, and always hassle-free checkout.
                        </p>
                    </div>
                </div>

            </div>
        </div>
    </section>

    <!-- ===== Footer ===== -->
    <footer class="footer">
        <div class="container">
            <p class="mb-0">
                © <%= java.time.Year.now() %> FoodExpress
            </p>
        </div>
    </footer>

    <!-- Bootstrap 5.3 JS Bundle (CDN) -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc4s9bIOgUxi8T/jzmSF+i79N/3mSdkEn0V5VuAXlJHf"
            crossorigin="anonymous"></script>

    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

    <script>
        // Smooth navbar scroll effect
        window.addEventListener('scroll', function () {
            const nav = document.getElementById('mainNav');
            if (window.scrollY > 50) {
                nav.style.boxShadow = '0 4px 30px rgba(0,0,0,0.3)';
            } else {
                nav.style.boxShadow = 'none';
            }
        });
    </script>

</body>
</html>
