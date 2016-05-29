/*
 * #%L
 * Game Database Control
 * %%
 * Copyright (C) 2016 LCManager Group
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

var gdbApp = angular.module('gdbApp');

gdbApp.controller('searchController', ['$scope', '$rootScope', '$http', 'layout', function($scope, $rootScope, $http, layout) {
	var queryChanged = false;

	var asToggleShow = 'Show Advanced Options';
	var asToggleHide = 'Hide Advanced Options';

	$scope.as = {
		toggle : asToggleShow,

		categories : [],
		platforms : [],
		sortingTerms : [
			{
				id : '',
				name : 'Relevance',
				direction : 'ASC'
			}, {
				id : 'Released',
				name : 'Release Date',
				direction : 'DESC'
			}, {
				id : 'Name',
				name : 'Name',
				direction : 'ASC'
			}
		]
	};

	$http.get(Constants.context + '/api/category').then(function(response) {
		$scope.as.categories = (response.data._embedded || {
			categoryList : []
		}).categoryList;
	}, Dialog.ajaxError);
	$http.get(Constants.context + '/api/platform').then(function(response) {
		$scope.as.platforms = (response.data._embedded || {
			osFamilyList : []
		}).osFamilyList;
	}, Dialog.ajaxError);

	$scope.query = {
		term : '',
		categories1 : [],
		categories2 : [],
		platforms : [],
		sorting : {
			term : 'Relevance',
			direction : null
		}
	};

	$scope.$watch('query.term', function(term) {
		if (queryChanged) {
			queryChanged = false;
		} else {
			$rootScope.$broadcast('nav_query-changed', {
				query : term
			});
		}
	});
	$scope.$watch('query.sorting.term', function(sortingTermId) {
		var sortingTerms = $scope.as.sortingTerms;
		for (var i = 0; i < sortingTerms.length; i++) {
			var sortingTerm = sortingTerms[i];
			if (sortingTerm.id === sortingTermId) {
				$scope.query.sorting.direction = sortingTerm.direction;
			}
		}
	});

	$scope.searched = false;
	$scope.game = [];
	$scope.pagination = {
		size : 0,
		totalElements : 0,
		totalPages : 0,
		page : 0
	}

	$scope.onAdvancedToggle = function() {
		$scope.query.categories1 = [];
		$scope.query.categories2 = [];
		$scope.query.platforms = [];
		$scope.query.sorting.term = '';
		if ($scope.as.toggle === asToggleShow) {
			$scope.as.toggle = asToggleHide;
		} else {
			$scope.as.toggle = asToggleShow;
		}
	};
	$scope.showDetails = function(gameId) {
		$rootScope.$broadcast('details_show', {
			gameId : gameId
		});
	};
	$scope.executeQuery = function(oldQuery) {
		if (!oldQuery) {
			$scope.pagination.page = 1;
		}

		var overlay = new Overlay(jQuery('#search').parents('.overlay-parent'));

		overlay.attach();

		layout.show('search', 'search-results');

		$http.get(Constants.context + '/api/game', {
			params : {
				page : $scope.pagination.page,
				term : $scope.query.term,
				categories1 : $scope.query.categories1.join(','),
				categories2 : $scope.query.categories2.join(','),
				platforms : $scope.query.platforms.join(','),
				sortingTerm : $scope.query.sorting.term,
				sortingDirection : $scope.query.sorting.direction
			}
		}).then(function(response) {
			$scope.games = (response.data._embedded || {
				gameList : []
			}).gameList;
			$scope.pagination.size = response.data.page.size;
			$scope.pagination.totalElements = response.data.page.totalElements;
			$scope.pagination.totalPages = response.data.page.totalPages;

			$scope.searched = true;

			overlay.detach();
		}, Dialog.ajaxError);
	};

	$scope.$on('search_query-changed', function(event, args) {
		queryChanged = true;
		$scope.query.term = args.query;
	});
	$scope.$on('search_execute-query', function() {
		$scope.executeQuery();
	});

	$scope.$watchGroup(['pagination.page'], function() {
		if ($scope.pagination.page < 1) {
			return;
		}

		$scope.executeQuery(true);
	});
}]);
