<!-- HTML structure macros. -->
<#macro bootstrap>
	<meta encoding="UTF-8">

	<#if dev!false>
		<!-- CSS -->
		<!-- Bootstrap -->
		<link rel="stylesheet" href="/css/bootstrap.css">
		<link rel="stylesheet" href="/css/bootstrap-theme.css">

		<!-- JavaScript -->
		<!-- jQuery -->
		<script src="/js/jquery.js"></script>
		<!-- AngularJS -->
		<script src="/js/angular.js"></script>
		<script src="/js/angular-animate.js"></script>
		<script src="/js/angular-resource.js"></script>
		<!-- Bootstrap -->
		<script src="/js/bootstrap.js"></script>
	<#else>
		<!-- CSS -->
		<!-- Bootstrap -->
		<link rel="stylesheet" href="/css/bootstrap.min.css">
		<link rel="stylesheet" href="/css/bootstrap-theme.min.css">

		<!-- JavaScript -->
		<!-- jQuery -->
		<script src="/js/jquery.min.js"></script>
		<!-- AngularJS -->
		<script src="/js/angular.min.js"></script>
		<script src="/js/angular-animate.min.js"></script>
		<script src="/js/angular-resource.min.js"></script>
		<!-- Bootstrap -->
		<script src="/js/bootstrap.min.js"></script>
	</#if>
</#macro>
<#macro head>
	<!-- GDB Stuff -->
	<!-- CSS -->
	<link rel="stylesheet" href="/css/main.css">

	<!-- JavaScript -->
	<script src="/js/constants.js"></script>
	<script src="/js/gdb.js"></script>
</#macro>
<#macro body>
	<noscript>
		<div class="container" style="z-index: 100000; position: fixed; top: 20px; left: 20px">
			<div class="alert alert-danger">
				<strong>JavaScript disabled!</strong> Please enable JavaScript to view this site correctly!
			</div>
		</div>
	</noscript>
</#macro>
<#macro tail>
</#macro>

<!-- GDB macros. -->
<#macro init>
	<script>
		angular.module('gdbApp', []).factory('csrf', function() {
			return {
				parameterName : '${_csrf.parameterName}',
				token : '${_csrf.token}'
			};
		});
	</script>
</#macro>
<#macro csrf>
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</#macro>
