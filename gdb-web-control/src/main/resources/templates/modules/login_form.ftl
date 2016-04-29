<#macro renderScripts>
	<script src="/js/controller/loginController.js"></script>
</#macro>
<#macro render>
	<div ng-controller="loginController" id="login-wrapper">
		<div ng-class="{ 'hidden' : !filled || authenticated }" class="alert alert-danger fade in">
			<a href="#" class="close" data-dismiss="alert">&times;</a>
			<strong>Login failed!</strong> The provided credentials are not correct.
		</div>
		<#if failed!false>
			<input ng-model="hasError" type="hidden" value="true" />
		</#if>
		<#if out!false>
			<div class="alert alert-success fade in">
				<a href="#" class="close" data-dismiss="alert">&times;</a>
				<strong>Logged out!</strong> You have logged out successfully.
			</div>
		</#if>
		<form id="login-form" action="/login" method="POST">
			<@gdb.csrf />

			<div ng-class="{ 'has-success' : filled && authenticated, 'has-error' : filled && !authenticated }" class="form-group has-feedback">
				<label for="username" class="control-label">Username</label>
				<input ng-model="username" ng-model-options="{ debounce : 500 }" type="text" id="username" class="form-control" name="username" placeholder="Username">
				<span class="glyphicon glyphicon-ok form-control-feedback">&nbsp;</span>
				<span class="glyphicon glyphicon-remove form-control-feedback">&nbsp;</span>
			</div>
			<div ng-class="{ 'has-success' : filled && authenticated, 'has-error' : filled && !authenticated }" class="form-group has-feedback">
				<label for="password" class="control-label">Password</label>
				<input ng-model="password" ng-model-options="{ debounce : 500 }" type="password" id="password" class="form-control" name="password" placeholder="Password">
				<span class="glyphicon glyphicon-ok form-control-feedback">&nbsp;</span>
				<span class="glyphicon glyphicon-remove form-control-feedback">&nbsp;</span>
			</div>
		</form>
	</div>
</#macro>
