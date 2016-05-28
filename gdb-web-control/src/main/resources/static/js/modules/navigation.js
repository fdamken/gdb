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

gdbApp.controller('navController', ['$scope', '$rootScope', '$http', 'layout', function($scope, $rootScope, $http, layout) {
	var queryChanged = false;

	$scope.query = '';

	$scope.goto = function(id) {
		layout.show(id);
	};
	$scope.executeQuery = function() {
		$rootScope.$broadcast('search_execute-query');
	};
	$scope.openLoginDialog = function() {
		$rootScope.$broadcast('login_open-dialog');
	};
	$scope.logout = function() {
		$('#logout-form').submit();
	};
	$scope.openPreferences = function() {
		$rootScope.$broadcast('preferences_open-dialog');
	};

	$scope.$watch('query', function(query) {
		if (queryChanged) {
			queryChanged = false;
		} else {
			$rootScope.$broadcast('search_query-changed', {
				query : query
			});
		}
	});

	$scope.$on('nav_query-changed', function(event, args) {
		queryChanged = true;
		$scope.query = args.query;
	});
}]);
