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

<#macro renderScripts>
	<script>
		Constants.login = {
			failed : ${(failed!false)?c},
			out : ${(out!false)?c}
		};
	</script>

	<link rel="stylesheet" href="${context}/css/modules/login.css">
	<script src="${context}/js/modules/login.js"></script>
</#macro>
<#macro render>
	<div ng-controller="loginController" id="login-dialog" class="modal fade">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span>&times;</span>
					</button>
					<h4 class="modal-title">Login</h4>
				</div>
				<div class="modal-body">
					<div ng-show="(credsChecked && !authenticated) || login.failed" class="alert alert-danger">
						<strong>Invalid Credentials!</strong> The provided credentials are not correct.
					</div>
					<form id="login-form" action="${context}/login" method="POST">
						<@gdb.csrf />

						<div ng-class="{ 'has-success' : credsChecked && authenticated, 'has-error' : (credsChecked && !authenticated) || login.failed }" class="form-group has-feedback">
							<label for="username" class="control-label">Username</label>
							<input ng-model="username" ng-model-options="{ debounce : 500 }" type="text" id="username" class="form-control" name="username"  placeholder="Username" autofocus="true">
						</div>
						<div ng-class="{ 'has-success' : credsChecked && authenticated, 'has-error' : (credsChecked && !authenticated) || login.failed }" class="form-group has-feedback">
							<label for="password" class="control-label">Password</label>
							<input ng-model="password" ng-model-options="{ debounce : 500 }" type="password" id="password" class="form-control" name="password" placeholder="Password">
						</div>
						<div>
							<button ng-disabled="!filled || !credsChecked || !authenticated" type="submit" class="btn btn-primary">
								Login
							</button>
							<div id="login-btn-status" ng-show="filled && !credsChecked">
								<div class="spinner">
									<div class="double-bounce1"></div>
									<div class="double-bounce2"></div>
								</div>
								<span>
									Checking your credentials …
								</span>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</#macro>
