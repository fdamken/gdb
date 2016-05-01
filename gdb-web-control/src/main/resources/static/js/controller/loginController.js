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
angular.module('gdbApp').controller('loginController', ['$http', '$scope', 'csrf', function($http, $scope, csrf) {
	$scope.username = null;
	$scope.password = null;
	$scope.filled = false;
	$scope.authenticated = false;

	$scope.onCredentialChange = function() {
		if ($scope.username && $scope.password) {
			$http.post('/auth', {
				username : $scope.username,
				password : $scope.password
			}, {
				headers : {
					'X-CSRF-TOKEN' : csrf.token
				}
			}).then(function(response) {
				$scope.filled = true;
				$scope.authenticated = response.data.authenticated;
			}, function(response) {
				$scope.authenticated = false;
				$scope.filled = true;
			});
		} else {
			$scope.filled = false;
			$scope.authenticated = false;
		}
	};
	$scope.$watch('username', $scope.onCredentialChange);
	$scope.$watch('password', $scope.onCredentialChange);

	$scope.$watch('authenticated', function() {
		if ($scope.authenticated) {
			$('#login-form').submit();
		}
	})
}]);
