<#import "gdb.ftl" as gdb>
<#import "modules.ftl" as m>

<!DOCTYPE html>
<html>
<head>
	<@gdb.bootstrap />
	<@gdb.init />
	<@gdb.head />

	<meta name="robots" content="noindex, follow">

	<title>GDB - Login</title>

	<style>
		#container {
			margin-top: 20px;
		}
	</style>
</head>
<body ng-app="gdbApp" ng-cloak="true">
	<@gdb.body />

	<div id="container" class="container">
		<@m.login_form />
	</div>

	<@gdb.tail />
</body>
</html>
