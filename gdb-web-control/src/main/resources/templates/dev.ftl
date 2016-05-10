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

	<script src="${context}/js/dev.js"></script>

	<title>GDB - Development</title>

	<style>
		#container {
			margin-top: 20px;
		}
	</style>
</head>
<body ng-app="gdbApp" ng-cloak="true">
	<@gdb.body />

	<div id="container" class="container">
		<div ng-controller="encoderController" id="encoder" class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Password Encoder</h3>
			</div>
			<div class="panel-body">
				<form>
					<div class="form-group">
						<label for="password" class="control-label">Password</label>
						<input ng-model="password" type="password" id="password" class="form-control" placeholder="Password" />
					</div>
					<div class="form-group">
						<label for="passwordEncoded" class="control-label">Encoded Password</label>
						<input ng-model="passwordEncoded" type="text" id="passwordEncoded" class="form-control" placeholder="Encoded Password" />
					</div>
				</form>
			</div>
		</div>
		<hr />
		<div ng-controller="createUserController" id="create-user" class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Create User</h3>
			</div>
			<div class="panel-body">
				<form>
					<div class="form-group">
						<label for="username" class="control-label">Username</label>
						<input ng-model="username" type="text" id="username" class="form-control" name="username" placeholder="Username" />
					</div>
					<div class="form-group">
						<label for="displayName" class="control-label">Display Name</label>
						<input ng-model="displayName" type="displayName" id="displayName" class="form-control" name="displayName" placeholder="Display Name" />
					</div>
					<div class="form-group">
						<label for="password2" class="control-label">Password</label>
						<input ng-model="password" type="password" id="password2" class="form-control" name="password" placeholder="Password" />
					</div>
					<button ng-click="create()" type="button" class="btn btn-default">Create</button>
				</form>
			</div>
		</div>
	</div>

	<@gdb.tail />
</body>
</html>
