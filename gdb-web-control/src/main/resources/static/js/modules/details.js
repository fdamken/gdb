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

gdbApp.controller('detailsController', ['$scope', '$http', 'layout', function($scope, $http, layout) {
	$scope.gameId = null;

	$scope.$on('details_show', function(event, args) {
		$scope.gameId = args.gameId;

		layout.show('details');
	});

	$scope.$watch('gameId', function() {
		if ($scope.gameId) {
			$http.get(Constants.context + '/api/game/' + $scope.gameId).then(function(response) {
				$scope.gameDetails = response.data;
			}, function(response) {
				alert('An error occurred!'); // TODO: Replace with something cooler.
			});
		}
	})
}]);
