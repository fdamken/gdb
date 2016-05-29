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
	<link rel="stylesheet" href="${context}/css/modules/preferences.css">
	<script src="${context}/js/modules/preferences.js"></script>
</#macro>
<#macro render>
	<div ng-controller="preferencesController" id="preferences-dialog" class="modal fade">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span>&times;</span>
					</button>
					<h4 class="modal-title">Preferences</h4>
				</div>
				<div class="modal-body">
					<ul class="nav nav-tabs">
						<#if is_user>
							<li class="active">
								<a data-toggle="tab" data-target="#preference-computer-systems">Computer Systems</a>
							</li>
						</#if>
						<#if is_admin>
							<li>
								<a data-toggle="tab" data-target="#preference-users">Users</a>
							</li>
						</#if>
					</ul>
					<div class="tab-content">
						<#if is_user>
							<div id="preference-computer-systems" class="tab-pane active">
								<div class="computer-systems">
									<input ng-model="cs.systemSearch.$" class="form-control" type="text" placeholder="Search Computer System">
									<table class="table table-striped table-hover">
										<tr class="table-head">
											<th class="col-sm-3">Description</th>
											<th class="col-sm-3">Operating System</th>
											<th class="col-sm-3">Processor</th>
											<th class="col-sm-3">Graphics</th>
										</tr>
										<tr ng-repeat="(i, system) in cs.systems | filter : cs.systemSearch" class="table-body" data-system="{{ i }}">
											<td class="col-sm-3 description">{{ system.description }}</td>
											<td class="col-sm-3 os">{{ system.operatingSystem.formatted }}</td>
											<td class="col-sm-3 processor">{{ system.processor.formatted }}</td>
											<td class="col-sm-3 graphics">{{ system.graphics.formatted }}</td>
										</tr>
									</table>
								</div>
							</div>
						</#if>
						<#if is_admin>
							<#if is_user>
								<div id="preference-users" class="tab-pane">
							<#else>
								<div id="preference-users" class="tab-pane active">
							</#if>
								<div class="users">
									<input ng-model="users.userSearch.$" class="form-control" type="text" placeholder="Search User">
									<table class="table table-striped">
										<tr class="table-head">
											<th class="col-sm-3 username">Username</th>
											<th class="col-sm-4 display-name">Display Name</th>
											<th class="col-sm-3 roles">Roles</th>
											<th class="col-sm-2 actions" colspan="2">&nbsp;</th>
										</tr>
										<tr ng-repeat="(i, user) in users.users | filter : users.userSearch" class="table-body" data-user="{{ i }}">
											<td class="col-sm-3 username">{{ user.username }}</td>
											<td class="col-sm-4 display-name">
												<input gdb-dirty="user" ng-model="user.displayName" ng-disabled="user.saving" type="text" class="form-control" placeholder="Display Name">
											</td>
											<td class="col-sm-3 roles">
												<div ng-repeat="role in roles" class="checkbox">
													<label>
														<input gdb-dirty="user" gdb-dirty-event="click" checklist-model="user.authorities" checklist-value="role" ng-disabled="user.saving" type="checkbox">{{ role | role }}</input>
													</label>
												</div>
											</td>
											<td ng-show="user.dirty && !user.saving" class="col-sm-2 ds-btn save">
												<button ng-click="saveUser(i)" ng-disabled="user.saving" class="btn btn-success">Save</button>
											</td>
											<td ng-show="!user.dirty && !user.saving" class="col-sm-2 ds-btn delete">
												<button ng-click="deleteUser(i)" ng-disabled="user.saving" class="btn btn-danger">Delete</button>
											</td>
											<td ng-show="user.saving" class="col-sm-2 saving">
												<div class="spinner">
													<div class="double-bounce1"></div>
													<div class="double-bounce2"></div>
												</div>
												<span>
													Saving …
												</span>
											</td>
										</tr>
									</table>
								</div>
							</div>
						</#if>
					</div>
				</div>
			</div>
		</div>
	</div>
</#macro>
