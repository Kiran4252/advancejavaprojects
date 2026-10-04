
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <title>ShopMate - Products</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/products.css">

</head>

<body>

    <!-- HEADER -->

    <jsp:include page="header.jsp" />


    <!-- PRODUCTS SECTION -->

    <section class="products-section">

        <h1>Product List</h1>

       <h4 class="page-content">Search Product : </h4>
       <h4 class="page-content">Sort Products : </h4>
	   
        <!-- PRODUCT GRID -->

        <div class="product-container">

            <c:forEach var="product" items="${products}">

                <div class="product-card">

                    <!-- PRODUCT IMAGE -->

                    <div class="product-image">

                        <img
                            src="${pageContext.request.contextPath}/images/${product.imagePath}"
                            alt="${product.pname}">

                    </div>


                    <!-- PRODUCT DETAILS -->

                    <div class="product-details">

                        <h2>
                            ${product.pname}
                        </h2>

                        <p class="category">
                            ${product.category}
                        </p>

                        <p class="price">
                            ₹ ${product.price}
                        </p>


                        <!-- BUTTONS -->

                        <div class="product-buttons">

                            <a href="${pageContext.request.contextPath}/cart/add/${product.pid}"
                               class="cart-btn">
                                Add to Cart
                            </a>

                            <a href="${pageContext.request.contextPath}/buy/${product.pid}"
                               class="buy-btn">
                                Buy Now
                            </a>

                        </div>

                    </div>

                </div>

            </c:forEach>

        </div>


        <!-- NO PRODUCTS MESSAGE -->

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
