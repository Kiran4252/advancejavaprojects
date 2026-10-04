
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <title>ShopMate - Shop</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/shop.css">

</head>

<body>

    <!-- HEADER -->

    <jsp:include page="header.jsp" />


    <!-- SHOP SECTION -->

    <section class="shop-section">

        <h1>Shop Products</h1>

        <p class="shop-subtitle">
            Find the best products at ShopMate
        </p>


        <!-- PRODUCT GRID -->

        <div class="shop-container">

            <c:forEach var="product" items="${products}">

                <div class="shop-card">

                    <!-- IMAGE -->

                    <div class="shop-image">

                        <img
                            src="${pageContext.request.contextPath}/images/${product.imagePath}"
                            alt="${product.pname}">

                    </div>


                    <!-- PRODUCT DETAILS -->

                    <div class="shop-details">

                        <h2>
                            ${product.pname}
                        </h2>

                        <p class="shop-category">
                            ${product.category}
                        </p>

                        <p class="shop-price">
                            ₹ ${product.price}
                        </p>


                        <!-- BUTTONS -->

                        <div class="shop-buttons">

                            <!-- ADD TO CART -->

                            <a href="${pageContext.request.contextPath}/cart/add/${product.pid}"
                               class="add-cart-btn">
                                Add to Cart
                            </a>


                            <!-- SHOP / BUY NOW -->

                            <a href="${pageContext.request.contextPath}/buy/${product.pid}"
                               class="shop-now-btn">
                                Shop Now
                            </a>

                        </div>

                    </div>

                </div>

            </c:forEach>

        </div>


        <!-- NO PRODUCTS -->

        <c:if test="${empty products}">

            <div class="no-products">

                <h2>No Products Available</h2>

                <p>Please check again later.</p>

            </div>

        </c:if>

    </section>


    <!-- FOOTER -->

    <jsp:include page="footer.jsp" />

</body>
</html>
