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
	<link rel="stylesheet" href="${context}/css/modules/details.css">
	<script src="${context}/js/modules/details.js"></script>
</#macro>
<#macro render>
	<div gdb-layout="{ id : 'details', title : 'Game Details', side : 'right' }" class="panel panel-default overlay-parent">
		<div id="details-content-wrapper" class="panel-body">
			<div ng-controller="detailsController" id="details-content">
				<@li.nothing msg="Please search and select a game." title="No details available!" />

				<@li.loading />

				<@li.loaded_start />

				<div class="header">
					<div class="col-sm-12">
						<h1>{{ gameDetails.name }}</h1>
					</div>
				</div>

				<div class="eyecatcher">
					<div class="col-sm-9">
						<div id="screenshots" class="screenshots carousel slide" data-ride="carousel" data-interval="false">
							<ol class="carousel-indicators">
								<li ng-repeat="(i, screenshot) in gameDetails.screenshots" ng-class="{ active : i === 0 }" data-target="#screenshots" data-slide-to="{{ i }}">&nbsp;</li>
							</ol>
							<div class="carousel-inner">
								<div ng-repeat="(i, screenshot) in gameDetails.screenshots" ng-class="{ active : i === 0 }" class="item">
									<div class="img" ng-style="{ 'background-image' : 'url(' + screenshot.image + ')' }">&nbsp;</div>
								</div>
							</div>
							<a class="left carousel-control" data-target="#screenshots" data-slide="prev">
								<span class="glyphicon glyphicon-chevron-left">&nbsp;</span>
							</a>
							<a class="right carousel-control" data-target="#screenshots" data-slide="next">
								<span class="glyphicon glyphicon-chevron-right">&nbsp;</span>
							</a>
						</div>
					</div>
					<div class="raw-details col-sm-3">
						<a class="header-image" href="{{ gameDetails.website }}" target="_blank">
							<img ng-src="{{ gameDetails.headerImage }}" alt="Header Image">
						</a>
						<a ng-show="gameDetails.metacriticUrl !== null" class="mc" href="{{ gameDetails.metacriticUrl }}" target="_blank">
							<img src="${context}/images/mc/logo_inverted.png" alt="Metacritic Logo" class="mc-logo">&nbsp;</span>
							<span class="mc-score" data-score="{{ gameDetails.metacriticScore }}">{{ gameDetails.metacriticScore }}</span>
						</a>
						<div class="facts-raw">
							<div class="details-list-item release-date">
								<label>Release Date</label>
								<span class="details-value">{{ gameDetails.releaseDate | date }}</span>
							</div>
							<div class="details-list-item website">
								<label>Website</label>
								<a class="details-value" href="{{ gameDetails.website }}" target="_blank">{{ gameDetails.website | host }}</a>
							</div>
						</div>
						<#if is_user>
							<div ng-show="reqSatisfy.enabled" id="req-satisfy">
								<span ng-show="reqSatisfy.minimum" id="req-satisfy-minimum"></span>
								<span ng-show="reqSatisfy.recommended" id="req-satisfy-recommended"></span>
							</div>
						</#if>
					</div>
				</div>

				<div class="detailed">
					<div class="description col-sm-8">
						<p ng-bind-html="trust(gameDetails.description)">
							<!-- Inserted by AngularJS -->
						</p>
					</div>
					<div class="facts col-sm-4">
						<div class="details-list-item genres">
							<label>
								<ng-pluralize count="gameDetails.genres.length" when="{ one : 'Genre', other : 'Genres' }" />
							</label>
							<ul class="details-value comma-separated">
								<li ng-repeat="genre in gameDetails.genres">
									<span class="value">{{ genre.description }}</span>
								</li>
							</ul>
						</div>
						<div class="details-list-item categories">
							<label>
								<ng-pluralize count="gameDetails.categories.length" when="{ one : 'Category', other : 'Categories' }" />
							</label>
							<ul class="details-value comma-separated">
								<li ng-repeat="category in gameDetails.categories">
									<span class="value">{{ category.description }}</span>
								</li>
							</ul>
						</div>
						<div class="details-list-item publishers">
							<label>
								<ng-pluralize count="gameDetails.publishers.length" when="{ one : 'Publisher', other : 'Publishers' }" />
							</label>
							<ul class="details-value comma-separated">
								<li ng-repeat="publisher in gameDetails.publishers">
									<span class="value">{{ publisher.name }}</span>
								</li>
							</ul>
						</div>
						<div class="details-list-item developers">
							<label>
								<ng-pluralize count="gameDetails.developers.length" when="{ one : 'Developer', other : 'Developers' }" />
							</label>
							<ul class="details-value comma-separated">
								<li ng-repeat="developer in gameDetails.developers">
									<span class="value">{{ developer.name }}</span>
								</li>
							</ul>
						</div>
						<div class="details-list-item platforms">
							<label>
								<ng-pluralize count="gameDetails.platforms.length" when="{ one : 'Platform', other : 'Platforms' }" />
							</label>
							<ul class="details-value comma-separated">
								<li ng-repeat="platform in gameDetails.platforms">
									<span class="value">{{ platform | platform }}</span>
								</li>
							</ul>
						</div>
					</div>
				</div>

				<@li.loaded_end />
			</div>
		</div>
	</div>
</#macro>
