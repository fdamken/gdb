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

<#import "../loadingIndicator.ftl" as li>

<#macro renderScripts>
	<link rel="stylesheet" href="${context}/css/modules/search.css">
	<script src="${context}/js/modules/search.js"></script>
</#macro>
<#macro render>
	<div gdb-layout="{ id : 'search', title : 'Search' }" class="panel panel-default overlay-parent">
		<div class="panel-body">
			<div ng-controller="searchController">
				<div id="advanced-search">
					<form ng-submit="executeQuery()" class="form-horizontal">
						<div class="form-group">
							<label for="as-term" class="col-sm-2 control-label">Query</label>
							<div class="col-sm-9">
								<input ng-model="query.term" id="as-term" class="form-control" type="text" placeholder="Search">
							</div>
						</div>
						<div id="advanced-control" class="form-group">
							<div class="col-sm-offset-2 col-sm-9">
								<button ng-click="onAdvancedToggle()" type="button" class="btn btn-default" data-toggle="collapse" data-target="#advanced">{{ as.toggle }}</button>
							</div>
						</div>
						<div id="advanced" class="collapse">
							<div class="form-group">
								<label for="as-categories1" class="col-sm-2 control-label">Category 1</label>
								<div class="col-sm-9">
									<div ng-repeat="category in as.categories" class="checkbox">
										<label>
											<input checklist-model="query.categories1" checklist-value="category.id" type="checkbox"> {{ category.description }}
										</label>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label for="as-categories2" class="col-sm-2 control-label">Category 2</label>
								<div class="col-sm-9">
									<div ng-repeat="category in as.categories" class="checkbox">
										<label>
											<input checklist-model="query.categories2" checklist-value="category.id" type="checkbox"> {{ category.description }}
										</label>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label for="as-platforms" class="col-sm-2 control-label">Platform</label>
								<div class="col-sm-9">
									<div ng-repeat="platform in as.platforms" class="checkbox">
										<label>
											<input checklist-model="query.platforms" checklist-value="platform" type="checkbox"> {{ platform | platform }}
										</label>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label for="as-sorting" class="col-sm-2 control-label">Sorting</label>
								<div class="col-sm-3">
									<select ng-model="query.sorting.term" ng-options="sortingTerm.id as sortingTerm.name for sortingTerm in as.sortingTerms" class="form-control"></select>
								</div>
								<div class="col-sm-3">
									<p class="form-control-static">
										{{ query.sorting.direction | direction }}
									</p>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-9">
								<button type="submit" class="btn btn-primary">Search</button>
							</div>
						</div>
					</form>
				</div>
				<hr />
				<div id="search-results">
					<div id="search-results-wrapper" ng-show="state !== null">
						<@li.nothing msg="No games found!" />

						<@li.loading />

						<@li.loaded_start />

						<table id="search-results-table" class="table table-bordered table-striped">
							<tr>
								<th class="name">Name</th>
								<th class="platforms">Platforms</th>
								<th class="release-date">Release Date</th>
							</tr>
							<tr ng-repeat="game in games" ng-click="showDetails(game.id)" class="result">
								<td class="name">{{ game.name }}</td>
								<td class="platforms">{{ game.platforms | platform }}</td>
								<td class="release-date">{{ game.releaseDate | date : 'dd.MM.yyyy' }}</td>
							</tr>
						</table>
						<ul class="pagination">
							<li ng-class="{ disabled : pagination.page <= 1 }">
								<a ng-click="(pagination.page > 1) && (pagination.page = pagination.page - 1)">
									<span>&laquo;</span>
								</a>
							</li>
							<li class="active">
								<a href="#/#search">
									{{ pagination.page }} <span class="sr-only">(current)</span>
								</a>
							</li>
							<li ng-class="{ disabled : pagination.page >= pagination.totalPages }">
								<a ng-click="(pagination.page < pagination.totalPages) && (pagination.page = pagination.page + 1)">
									<span>&raquo;</span>
								</a>
							</li>
						</ul>

						<@li.loaded_end />
					</div>
				</div>
			</div>
		</div>
	</div>
</#macro>
