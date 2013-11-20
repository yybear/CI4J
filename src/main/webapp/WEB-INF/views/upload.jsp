<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<link href="/assets/css/bootstrap.min.css" rel="stylesheet">
<link href="/assets/css/base.css" rel="stylesheet">
<title>Insert title here</title>
</head>
<body>
	<div class="container">

		<form action="/user/doUpload" method="post" enctype="Multipart/form-data">
			<input type="file" name="file1">
			<input type="submit" value="upload">
		</form>
	</div>
</body>
</html>