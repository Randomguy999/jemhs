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

<style>
.error {
	color: red
}
</style>

<title>Student Registration Page</title>

<c:url value="/css/main.css" var="jstlCss" />
<link href="${jstlCss}" rel="stylesheet" />
</head>
<body>
	<h4 align="center">${message}</h4>

	<div class="container">
		<form:form class="form-horizontal" action="RegistrationProcess"
			modelAttribute="studentRegistration">

			<spring:bind path="firstName">
				<div class="form-group required">
					<label class=" col-sm-2 control-label">First Name </label>
					<div class="col-md-4">
						<form:input path="firstName" type="text" class="form-control "
							id="firstName" placeholder="Enter First Name" required="required" />
						<form:errors path="firstName" class="control-label" />
					</div>
				</div>
			</spring:bind>

			<spring:bind path="middleName">
				<div class="form-group">
					<label class="col-sm-2 control-label">Middle Name</label>
					<div class="col-md-4">
						<form:input path="middleName" type="text" class="form-control "
							id="middleName" placeholder="Enter Middle Name" />
						<form:errors path="middleName" class="control-label" />
					</div>
				</div>
			</spring:bind>

			<spring:bind path="lastName">
				<div class="form-group required">
					<label class="col-sm-2 control-label">Last Name </label>
					<div class="col-md-4">
						<form:input path="lastName" type="text" class="form-control "
							id="lastName" placeholder="Enter Last Name" required="required" />
						<form:errors path="lastName" class="control-label" />
					</div>
				</div>
			</spring:bind>


			<spring:bind path="gender">
				<div class="form-group required ">
					<label class="col-sm-2 control-label">Gender </label>
					<div class="col-sm-10">
						<label class="radio-inline"> <form:radiobutton
								path="gender" value="M" /> Male
						</label> <label class="radio-inline"> <form:radiobutton
								path="gender" value="F" required="required" /> Female
						</label> <br />
						<form:errors path="gender" class="control-label" />
					</div>
				</div>
			</spring:bind>


			<spring:bind path="dob">
				<div class="form-group required ">
					<label class="col-sm-2 control-label">DOB </label>
					<div class="col-md-4">
						<form:input path="dob" type="text" class="form-control " id="dob"
							placeholder="DD/MM/YYYY" required="required" />
						<form:errors path="dob" class="control-label" />
					</div>
				</div>
			</spring:bind>


			<spring:bind path="standard">
				<div class="form-group required">
					<label class="col-sm-2 control-label">Standard </label>
					<div class="col-sm-2">
						<form:select path="standard" class="form-control">
							<form:option value="NONE" label="--- Select ---"
								reuired="required" />
							<form:options items="${studentRegistration.standardList}" />
						</form:select>
						<form:errors path="standard" class="control-label" />
					</div>
					<div class="col-sm-2"></div>
				</div>
			</spring:bind>


			<spring:bind path="phone">
				<div class="form-group required ">
					<label class="col-sm-2 control-label">Phone </label>
					<div class="col-md-4">
						<form:input path="phone" type="text" class="form-control "
							id="phone" placeholder="Enter Valid Phone Number"
							required="required" />
						<form:errors path="phone" class="control-label" />
					</div>
				</div>
			</spring:bind>

			<spring:bind path="email">
				<div class="form-group required ">
					<label class="col-sm-2 control-label">Email </label>
					<div class="col-md-4">
						<form:input path="email" type="text" class="form-control "
							id="email" placeholder="Enter Valid Email ID" required="required" />
						<form:errors path="email" class="control-label" />
					</div>
				</div>
			</spring:bind>

			<spring:bind path="guardian">
				<div class="form-group ">
					<label class="col-sm-2 control-label">Guardian </label>
					<div class="col-md-4">
						<form:input path="guardian" type="text" class="form-control "
							id="lastName" placeholder="Enter Guardian Name" />
						<form:errors path="guardian" class="control-label" />
					</div>
				</div>
			</spring:bind>

			<spring:bind path="relation">
				<div class="form-group">
					<label class="col-sm-2 control-label">Relation</label>
					<div class="col-md-4">
						<form:input path="relation" type="text" class="form-control "
							id="lastName" placeholder="Enter Relation" />
						<form:errors path="relation" class="control-label" />
					</div>
				</div>
			</spring:bind>

			<spring:bind path="address">
				<div class="form-group required">
					<label class="col-sm-2 control-label">Address </label>
					<div class="col-md-4">
						<form:textarea path="address" rows="5" class="form-control"
							id="address" placeholder="Enter Address" required="required" />
						<form:errors path="address" class="control-label" />
					</div>
				</div>
			</spring:bind>


			<button type="submit" class="btn-lg btn-primary center-block">Submit</button>
			<!-- <a href="/changePass">Change password</a> -->
		</form:form>
	</div>
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>

