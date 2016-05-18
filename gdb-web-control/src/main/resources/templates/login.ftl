<#--
 #%L
 Game Database Control
 %%
 Copyright (C) 2016 LCManager Group
 %%
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
      http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 #L%
-->

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
	<script>
		$(function() {
			$('#login-dialog .modal-header .close').remove();
			$('#login-dialog').modal({
				backdrop : false,
				keyboard : false
			});
		});
	</script>
</head>
<body ng-app="gdbApp" ng-cloak="true">
	<@gdb.body />

	<@gdb.tail />

	<!-- Dialogs -->
	<@m.login />
</body>
</html>
