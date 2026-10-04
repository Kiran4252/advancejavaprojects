
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <title>ShopMate - Register</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/register.css">

</head>

<body>

    <!-- HEADER -->

    <jsp:include page="header.jsp" />
	
	<h3> ${msg} </h3>


    <!-- REGISTER SECTION -->

    <section class="register-section">

        <div class="register-box">

            <h2>Create Account</h2>

            <p class="register-subtitle">
                Join ShopMate today
            </p>


            <form action="${pageContext.request.contextPath}/add-customer"
                  method="post">


                <!-- NAME -->

                <div class="form-group">

                    <label for="name">
                        Full Name
                    </label>

                    <input type="text"
                           id="name"
                           name="name"
                           placeholder="Enter your name"
                           required>

                </div>


                <!-- USERNAME -->

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


                <!-- PASSWORD -->

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


                <!-- EMAIL -->

                <div class="form-group">

                    <label for="email">
                        Email
                    </label>

                    <input type="email"
                           id="email"
                           name="email"
                           placeholder="Enter email"
                           required>

                </div>


                <!-- MOBILE -->

                <div class="form-group">

                    <label for="mobile">
                        Mobile Number
                    </label>

                    <input type="tel"
                           id="mobile"
                           name="mobile"
                           placeholder="Enter mobile number"
                           required>

                </div>


                <!-- REGISTER BUTTON -->

                <button type="submit" class="register-btn">
                    Register
                </button>

            </form>


            <!-- LOGIN LINK -->

            <div class="login-link">

                Already have an account?

                <a href="${pageContext.request.contextPath}/login">
                    Login Here
                </a>

            </div>

        </div>

    </section>


    <!-- FOOTER -->

    <jsp:include page="footer.jsp" />

</body>
</html>
