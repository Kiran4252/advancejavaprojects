<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert page</title>
</head>
<body>

   <center> <h1 style="color:green">Insert Player</h1> 
   
   <form action="insert-player" method="post">
	
	<input type="text" name="jn" placeholder="Enter jersey number"><br><br>
	<input type="text" name="pname" placeholder="Enter player name"><br><br>
	<input type="text" name="runs" placeholder="Enter player runs"><br><br>
	
	<input type="submit" value="Insert Player">
   </form>
   
   </center>
</body>
</html>