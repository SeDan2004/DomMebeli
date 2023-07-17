<%@page language="Java" contentType="text/html; charset=UTF-8" %>

<jsp:include page="./Modules/Index/CheckUserCookie.jsp"/>

<html>
	<head>
		<title>Дом мебели</title>
		<script
  			src="https://code.jquery.com/jquery-3.7.0.min.js"
  			integrity="sha256-2Pmvv0kuTBOenSvLm6bvfBSSHrUJ+3A7x6P5Ebd07/g="
  			crossorigin="anonymous">
		</script>
		
		<link rel="stylesheet" href="./Static/Index/Index.css">
	</head>
	<body>
		
		<jsp:include page="./Modules/Index/Wrapper.jsp" />
		<jsp:include page="./Modules/Index/Header.jsp" />
		<jsp:include page="./Modules/Index/Content.jsp" />
		<jsp:include page="./Modules/Index/Footer.jsp" />
		
		<script src="./Static/Index/Index.js"></script>
	</body>
</html>