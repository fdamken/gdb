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

gdbApp.controller('encoderController', ['$http', '$scope', 'csrf', function($http, $scope, csrf) {
	$scope.password = null;
	$scope.passwordEncoded = null;

	$scope.$watch('password', function() {
		if ($scope.password) {
			$http.post(Constants.context + '/dev/encoder', $scope.password, {
				headers : {
					'X-CSRF-TOKEN' : csrf.token
				}
			}).then(function(response) {
				$scope.passwordEncoded = response.data;
			}, function(response) {
				$scope.passwordEncoded = null;
			});
		}
	});
}]);

gdbApp.controller('createUserController', ['$http', '$scope', 'csrf', function($http, $scope, csrf) {
	$scope.username = null;
	$scope.displayName = null;
	$scope.password = null;
	$scope.usernameInvalid = false;
	$scope.displayNameInvalid = false;
	$scope.passwordInvalid = false;

	$scope.create = function() {
		if ($scope.username && $scope.password) {
			$http.put(Constants.context + '/dev/user', {
				username : $scope.username,
				displayName : $scope.displayName,
				password : $scope.password
			}, {
				headers : {
					'X-CSRF-TOKEN' : csrf.token
				}
			}).then(function(response) {
				alert('User created!');

				$scope.username = null;
				$scope.displayName = null;
				$scope.password = null;

				$scope.usernameInvalid = false;
				$scope.displayNameInvalid = false;
				$scope.passwordInvalid = false;
			}, function(response) {
				$scope.usernameInvalid = true;
				$scope.displayNameInvalid = true;
				$scope.passwordInvalid = true;

				if (typeof response.data.msg === 'string') {
					alert(response.data.msg);
				} else {
					alert('An error occurred!');
				}
			})
		} else {
			$scope.usernameInvalid = true;
			$scope.displayNameInvalid = true;
			$scope.passwordInvalid = true;
		}
	};
}]);
