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

		<form class="form-signin" action="/user/doLogin" method="post">
			<h2 class="form-signin-heading">Please sign in</h2>
			<input type="text" class="form-control" placeholder="Email address"
				required="true" autofocus="" name="name"> <input
				type="password" class="form-control" placeholder="Password"
				required="true" name="passwd"> <label class="checkbox">
				<input type="checkbox" value="remember-me">Remember me
			</label>
			<button class="btn btn-lg btn-primary btn-block" type="submit">Sign
				in</button>
		</form>
		<c:out value="${dd }"/>
	</div>
</body>
</html>