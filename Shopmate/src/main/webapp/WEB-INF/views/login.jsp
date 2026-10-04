
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <title>ShopMate - Login</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

</head>

<body>

    <!-- HEADER -->

    <jsp:include page="header.jsp" />
	
	<h3> ${msg} </h3>


    <!-- LOGIN SECTION -->

    <section class="login-section">

        <div class="login-box">

            <h2>Login to ShopMate</h2>

            <form action="${pageContext.request.contextPath}/verify-login"
                  method="post">

                <div class="form-group">

                    <label for="username">
                        Username
                    </label>

                    <input type="text"
                           id="username"
                           name="username"
                           placeholder="Enter username"
                           required>

                </div>


                <div class="form-group">

                    <label for="password">
                        Password
                    </label>

                    <input type="password"
                           id="password"
                           name="password"
                           placeholder="Enter password"
                           required>

                </div>


                <button type="submit" class="login-btn">
                    Login
                </button>

            </form>


            <div class="register-link">

                Don't have an account?

                <a href="${pageContext.request.contextPath}/register">
                    Register Here
                </a>

            </div>

        </div>

    </section>


    <!-- FOOTER -->

    <jsp:include page="footer.jsp" />

</body>
</html>
