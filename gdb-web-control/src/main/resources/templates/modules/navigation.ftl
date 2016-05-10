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
				<a class="navbar-brand">
					<img alt="Brand" src="/images/brand.png" />
				</a>
			</div>
			<div class="navbar-body">
				<ul class="nav navbar-nav navbar-left">
					<!-- Some menu entries may be added. -->
				</ul>
				<ul class="nav navbar-nav navbar-right">
					<li>
						<a href="#">Login / Register</a>
					</li>
				</ul>
				<form class="navbar-form navbar-right">
					<div class="form-group">
						<input ng-model="query" type="search" class="form-control" placeholder="Search">
					</div>
					<button ng-click="executeQuery()" type="button" class="btn btn-default">Search</button>
				</form>
			</div>
		</div>
	</nav>
</#macro>
