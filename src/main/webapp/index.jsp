<%@page language="Java" contentType="text/html; charset=UTF-8" %>

<jsp:include page="./Modules/Index/CheckUserCookie.jsp"/>

<html>
	<head>
		<title>Дом мебели</title>
		<meta name="viewport" content="width=device-width">
		<script
  			src="https://code.jquery.com/jquery-3.7.0.min.js"
  			integrity="sha256-2Pmvv0kuTBOenSvLm6bvfBSSHrUJ+3A7x6P5Ebd07/g="
  			crossorigin="anonymous">
		</script>
		
		<script src="https://cdn.jsdelivr.net/npm/gsap@3.12.2/dist/gsap.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/vue@2/dist/vue.js"></script>
		<link rel="stylesheet" href="./Static/Index/Index.css">
	</head>
	<body>
		
		<jsp:include page="./Modules/Wrapper/Wrapper.jsp" />
		<jsp:include page="./Modules/Index/Header.jsp" />
		<jsp:include page="./Modules/Index/Content.jsp" />
		<jsp:include page="./Modules/Index/Footer.jsp" />
		
		<script src="./Static/Index/Index.js"></script>
	</body>
</html>