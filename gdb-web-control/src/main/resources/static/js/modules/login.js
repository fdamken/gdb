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

angular.module('gdbApp').controller('loginController', ['$http', '$scope', '$timeout', 'csrf', function($http, $scope, $timeout, csrf) {
	$scope.username = '';
	$scope.password = '';

	$scope.filled = false;
	$scope.credsChecked = false;
	$scope.authenticated = false;

	$scope.login = {
		failed : Boolean(Constants.login.failed),
		out : Boolean(Constants.login.out)
	};

	var reset = function() {
		$scope.login.failed = false;
		$scope.login.out = false;
		$scope.filled = false;
		$scope.credsChecked = false;
		$scope.authenticated = false;
	};

	$scope.$on('login_open-dialog', function() {
		$('#login-dialog').modal('show');
	});

	var first = true;
	$scope.$watchGroup(['username', 'password'], function() {
		if (first) {
			first = false;
		} else {
			reset();

			if ($scope.username && $scope.password) {
				$scope.filled = true;

				$http.post(Constants.context + '/auth', {
					username : $scope.username,
					password : $scope.password
				}, {
					headers : {
						'X-CSRF-TOKEN' : csrf.token
					},
					params : {
						checkOnly : true
					}
				}).then(function(response) {
					$scope.credsChecked = true;
					$scope.authenticated = Boolean(response.data.authenticated);
				}, function() {
					$scope.credsChecked = true;
					$scope.authenticated = false;
				});
			}
		}
	});
}]);
