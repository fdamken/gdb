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
	<link rel="stylesheet" href="${context}/css/modules/navigation.css">
	<script src="${context}/js/modules/navigation.js"></script>
</#macro>
<#macro render>
	<nav ng-controller="navController" id="navigation" class="navbar navbar-default">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="#">
					<img alt="Brand" src="${context}/images/brand/brand_64.png" />
				</a>
			</div>
			<div class="navbar-body">
				<ul class="nav navbar-nav navbar-left">
					<li>
						<a ng-click="goto('search')">Search</a>
					</li>
				</ul>
				<ul class="nav navbar-nav navbar-right">
				<#if is_user || is_admin>
					<li class="dropdown">
						<a class="dropdown-toggle" data-toggle="dropdown">
							${principal.displayName} <span class="caret">&nbsp;</span>
						</a>
						<ul class="dropdown-menu">
							<li class="divider">&nbsp;</li>
							<li>
								<form id="logout-form" class="hidden" action="${context}/logout" method="POST">
									<@gdb.csrf />
								</form>
								<a ng-click="logout()">Logout</a>
							</li>
						</ul>
					</li>
				<#else>
					<li>
						<a ng-click="openLoginDialog()">Login / Register</a>
					</li>
				</#if>
				</ul>
				<form ng-submit="executeQuery()" class="navbar-form navbar-right">
					<div class="form-group">
						<input ng-model="query" type="search" class="form-control" placeholder="Search">
					</div>
					<button type="submit" class="btn btn-default">Search</button>
				</form>
			</div>
		</div>
	</nav>
</#macro>
