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

var gdbApp = angular.module('gdbApp')

gdbApp.filter('platform', function() {
	return function(data) {
		var result = []
		for (var i = 0; i < data.length; i++) {
			switch (data[i]) {
				case 'WINDOWS':
					result.push('Windows');
					break;
				case 'MAC':
					result.push('Mac');
					break;
				case 'UNIX':
					result.push('Linux');
					break;
				default:
					result.push('UNKNOWN');
					break;
			}
		}
		return result.join(', ');
	};
});

gdbApp.controller('searchController', ['$scope', '$http', function($scope, $http) {
	$scope.game = [];
	$scope.query = '';
	$scope.pagination = {
		size : 0,
		totalElements : 0,
		totalPages : 0,
		page : 0
	}

	$scope.$on('search_query-changed', function(event, args) {
		$scope.pagination.page = 1;
		$scope.query = args.query;
	});

	$scope.$watchGroup(['pagination.page', 'query'], function() {
		if ($scope.pagination.page < 1) {
			return;
		}

		$http.get('/api/game', {
			params : {
				page : $scope.pagination.page,
				term : $scope.query
			}
		}).then(function(response) {
			$scope.games = response.data._embedded.gameList;
			$scope.pagination.size = response.data.page.size;
			$scope.pagination.totalElements = response.data.page.totalElements;
			$scope.pagination.totalPages = response.data.page.totalPages;
		}, function(response) {
			alert('An error occurred!'); // TODO: Replace with something cooler.
		});
	});
}]);
