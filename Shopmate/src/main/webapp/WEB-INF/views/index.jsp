<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <title>ShopMate - Index</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

</head>

<body>
	
		<jsp:include page="header.jsp" />


    <!-- ================= HERO SECTION ================= -->

    <section class="hero">

        <div class="hero-content">

            <h1>Welcome to ShopMate</h1>

            <p>Your Smart Online Shopping Partner</p>

            <a href="/login" class="shop-btn">
                Login First --->
            </a>

        </div>

    </section>

	<jsp:include page="footer.jsp" />
</body>
</html>