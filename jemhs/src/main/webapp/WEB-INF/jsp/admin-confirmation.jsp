<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>

<html>

<head>
<title>Admin Page</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<c:url value="/css/main.css" var="jstlCss" />
<link href="${jstlCss}" rel="stylesheet" />
</head>

<body>
	<p class="text-primary">
		<!-- 	<strong> Welcome Admin,</strong> -->
	</p>
	<button type="button" class="btn btn-success"
		onclick="location.href='/showRegistrationForm'">Register
		Student</button>
	<button type="button" class="btn btn-danger"
		onclick="location.href='#'">Register Faculty</button>

	<div class="container">
		<div class="row">
			<!-- <h2>Stylish Search Box</h2> -->
			<div id="custom-search-input">
				<div class="input-group col-sm-2">
					<input type="text" class="  search-query form-control"
						placeholder="Student ID or Name" /> <span class="input-group-btn">
						<button class="btn btn-danger" type="button">
							<span class=" glyphicon glyphicon-search"></span>
						</button>
					</span>
				</div>
			</div>
		</div>

	</div>
	Dear
	<strong>${user}</strong>, Welcome to Admin Page.
	<%-- <a href="<c:url value="/logout" />">Logout</a> --%>
	<a href="<c:url value="/logout"/>"> Logout</a>

</body>

</html>










