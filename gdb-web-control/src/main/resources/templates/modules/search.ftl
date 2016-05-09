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
	<link rel="stylesheet" href="/css/modules/search.css">
	<script src="/js/modules/search.js"></script>
</#macro>
<#macro render>
	<div ng-controller="searchController" ng-class="{ hidden : pagination.totalElements < 1 }">
		<table id="search-results" class="table table-bordered table-striped">
			<tr>
				<th class="name">Name</th>
				<th class="platforms">Platforms</th>
				<th class="release-date">Release Date</th>
			</tr>
			<tr ng-repeat="game in games">
				<td class="name">{{ game.name }}</td>
				<td class="platforms">{{ game.platforms | platform }}</td>
				<td class="release-date">{{ game.releaseDate | date : 'dd.MM.yyyy' }}</td>
			</tr>
		</table>
		<ul class="pagination">
			<li>
				<a ng-click="pagination.page = pagination.page - 1" ng-class="{ disabled : pagination.page === 1 }" href="#">
					<span>&laquo;</span>
				</a>
			</li>
			<li class="active">
				<a href="#">
					{{ pagination.page }} <span class="sr-only">(current)</span>
				</a>
			</li>
			<li>
				<a ng-click="pagination.page = pagination.page + 1" ng-class="{ disabled : pagination.page === pagination.totalPages }" href="#">
					<span>&raquo;</span>
				</a>
			</li>
		</ul>
	</div>
</#macro>
