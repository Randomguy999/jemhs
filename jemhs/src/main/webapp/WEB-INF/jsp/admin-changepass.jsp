<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<jsp:include page="../fragments/header.jsp" />
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="webjars/bootstrap/3.3.7/css/bootstrap.min.css" />

<title>Admin Page</title>
<c:url value="/css/main.css" var="jstlCss" />
<link href="${jstlCss}" rel="stylesheet" />
<style>
.error {
	color: red
}
h4{
	color:red;
}
</style>

</head>
<body>
<h4 align="center">${message}</h4>
	<div class="container">
		<form:form class="form-horizontal" action="adminchangePassword" modelAttribute="changePass">
			<spring:bind path="userName">
				<div class="form-group required">
					<label class="col-sm-2 control-label">Username</label>
					<div class="col-md-4">
						<form:input path="userName" type="text" class="form-control "
							id="name" placeholder="Enter username" required="required"/>
						<form:errors path="userName" class="control-label" />
					</div>
				</div>
			</spring:bind>
			
			<spring:bind path="password">
				<div class="form-group required">
					<label class="col-sm-2 control-label">Current Password</label>
					<div class="col-md-4">
						<form:input path="password" type="password" class="form-control "
							id="name" placeholder="Enter Current Password" required="required"/>
						<form:errors path="password" class="control-label" />
					</div>
				</div>
			</spring:bind>
			
			<spring:bind path="newPassword">
				<div class="form-group required">
					<label class="col-sm-2 control-label">New Password</label>
					<div class="col-md-4">
						<form:input path="newPassword" type="password" class="form-control "
							id="name" placeholder="Enter New Password" required="required"/>
						<form:errors path="newPassword" class="control-label" />
					</div>
				</div>
			</spring:bind>
			
			<button type="submit" class="btn-lg btn-primary center-block">Submit</button>
		</form:form>
	</div>
<jsp:include page="../fragments/footer.jsp" />
</body>
</html>
			






