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
	<div ng-controller="preferencesController">
		<div id="preferences-dialog" class="modal fade">
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
										<div ng-show="cs.systems.length > 0">
											<input ng-model="cs.systemSearch.$" class="form-control" type="text" placeholder="Search Computer System">
											<table class="table table-striped table-hover">
												<tr class="table-head">
													<th class="col-sm-2 description">Description</th>
													<th class="col-sm-3 os">Operating System</th>
													<th class="col-sm-3 processor">Processor</th>
													<th class="col-sm-3 graphics">Graphics</th>
													<th class="col-sm-1 actions" colspan="1">&nbsp;</th>
												</tr>
												<tr ng-repeat="(i, system) in cs.systems | filter : cs.systemSearch" ng-click="cs.editSystem(i)" ng-class="{ success : system.primary }" class="table-body" data-system="{{ i }}">
													<td class="col-sm-2 description">{{ system.description }}</td>
													<td class="col-sm-3 os">{{ system.osFamily | platform }}</td>
													<td class="col-sm-3 processor">{{ system.processor.formatted }}</td>
													<td class="col-sm-3 graphics">{{ system.graphics.formatted }}</td>
													<td class="col-sm-1 delete">
														<button ng-click="cs.deleteSystem(i); $event.stopPropagation();" class="btn btn-danger">Delete</button>
													</td>
												</tr>
											</table>
										</div>
										<button ng-click="cs.createSystem()" ng-class="{ 'btn-success' : cs.systems.length <= 0, 'btn-default' : cs.systems.length > 0 }" type="button" id="cs-new-system" class="btn">Create a new Computer System</button>
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
													<button ng-click="users.saveUser(i)" ng-disabled="user.saving" class="btn btn-success">Save</button>
												</td>
												<td ng-show="!user.dirty && !user.saving" class="col-sm-2 ds-btn delete">
													<button ng-click="users.deleteUser(i)" ng-disabled="user.saving" class="btn btn-danger">Delete</button>
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

		<div id="cs-dialog" class="modal fade">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h4 ng-show="cs.dialog.mode === 'mode_create'" class="modal-title">Create Computer Systems</h4>
						<h4 ng-show="cs.dialog.mode === 'mode_edit'" class="modal-title">Edit Computer System</h4>
					</div>
					<div class="modal-body">
						<form ng-submit="cs.saveSystem()">
							<div ng-class="{ 'has-success' : cs.dialog.descriptionError === false, 'has-error' : cs.dialog.descriptionError }" class="form-group has-feedback">
								<label for="cs-description" class="control-label">Description</label>
								<input ng-focus="cs.dialog.descriptionTouched = true" ng-model="cs.dialog.description" id="cs-description" class="form-control" type="text" placeholder="Description">
								<span ng-show="cs.dialog.descriptionError" class="help-block">{{ cs.dialog.descriptionError }}</span>
							</div>
							<div ng-class="{ 'has-success' : cs.dialog.memoryError === false, 'has-error' : cs.dialog.memoryError }" class="form-group has-feedback">
								<label for="cs-memory" class="control-label">Memory (RAM)</label>
								<input ng-focus="cs.dialog.memoryTouched = true" ng-model="cs.dialog.memory" id="cs-memory" type="number" class="form-control" placeholder="Memory (RAM)">
								<span ng-show="cs.dialog.memoryError" class="help-block">{{ cs.dialog.memoryError }}</span>
							</div>
							<div class="checkbox has-feedback checkbox">
								<label>
									<input ng-model="cs.dialog.primary" id="cs-primary" type="checkbox"> Primary
								</label>
							</div>
							<div ng-class="{ 'has-success' : cs.dialog.osFamilyError === false, 'has-error' : cs.dialog.osFamilyError }" class="form-group has-feedback">
								<label for="cs-osfamily" class="control-label">Operating System Family</label>
								<select ng-click="cs.dialog.osFamilyTouched = true" ng-model="cs.dialog.osFamily" id="cs-osfamily" type="text" class="form-control">
									<option ng-repeat="platform in platforms" value="{{ platform }}">{{ platform | platform }}</option>
								</select>
								<span ng-show="cs.dialog.osFamilyError" class="help-block">{{ cs.dialog.osFamilyError }}</span>
							</div>
							<div ng-class="{ 'has-success' : cs.dialog.processorError === false, 'has-error' : cs.dialog.processorError }" class="form-group has-feedback">
								<label class="control-label">Processor</label>
								<div class="pg">
									<select ng-model="cs.dialog.processorBrand" id="cs-processor-brand" class="form-control pg-brand">
										<option ng-repeat="brand in brands.processor" value="{{ brand.value }}">{{ brand.name }}</option>
									</select>
									<input ng-focus="cs.dialog.processorModelTouched = true" ng-model="cs.dialog.processorModel" ng-model-options="{ debounce : 500 }" type="text" id="cs-processor-model" class="form-control pg-model" placeholder="Processor Model">
									<div ng-show="cs.dialog.processorChecking" class="checking-spinner">
										<div>
											<div class="spinner">
												<div class="double-bounce1"></div>
												<div class="double-bounce2"></div>
											</div>
										</div>
										<span>
											Checking …
										</span>
									</div>
								</div>
								<span ng-show="cs.dialog.processorError" class="help-block">{{ cs.dialog.processorError }}</span>
							</div>
							<div ng-class="{ 'has-success' : cs.dialog.graphicsError === false, 'has-error' : cs.dialog.graphicsError }" class="form-group has-feedback">
								<label class="control-label">Graphics Card</label>
								<div class="pg">
									<select ng-model="cs.dialog.graphicsBrand" id="cs-graphics-brand" class="form-control pg-brand">
										<option ng-repeat="brand in brands.graphics" value="{{ brand.value }}">{{ brand.name }}</option>
									</select>
									<input ng-focus="cs.dialog.graphicsModelTouched = true" ng-model="cs.dialog.graphicsModel" ng-model-options="{ debounce : 500 }" type="text" id="cs-graphics-model" class="form-control pg-model" placeholder="Graphics Card Model">
									<div ng-show="cs.dialog.graphicsChecking" class="checking-spinner">
										<div>
											<div class="spinner">
												<div class="double-bounce1"></div>
												<div class="double-bounce2"></div>
											</div>
										</div>
										<span>
											Checking …
										</span>
									</div>
								</div>
								<span ng-show="cs.dialog.graphicsError" class="help-block">{{ cs.dialog.graphicsError }}</span>
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button ng-click="cs.closeDialog()" type="button" class="btn btn-default">Abort</button>
						<button ng-disabled="!cs.dialog.valid" ng-click="cs.saveSystem()" type="button" class="btn btn-primary">Save</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</#macro>
