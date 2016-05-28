'use strict';

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

var ACTION_LOGIN = 'login';
var ACTION_REGISTER = 'register';

var gdbApp = angular.module('gdbApp')

gdbApp.controller('loginDialogController', ['$scope', function($scope) {
	$scope.action = ACTION_LOGIN;
}]);
gdbApp.controller('loginController', ['$http', '$scope', '$timeout', 'csrf', function($http, $scope, $timeout, csrf) {
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
gdbApp.controller('registerController', ['$http', '$scope', '$timeout', 'csrf', function($http, $scope, $timeout, csrf) {
	$scope.username = '';
	$scope.password = '';
	$scope.passwordRepeated = '';
	$scope.displayName = '';

	$scope.usernameAccessed = false,
	$scope.passwordAccessed = false;
	$scope.passwordRepeatedAccessed = false,
	$scope.displayNameAccessed = false;

	$scope.usernameLastChanged = new Date().getTime();
	$scope.passwordLastChanged = new Date().getTime();
	$scope.displayNameLastChanged = new Date().getTime();

	$scope.registering = false;

	var reset = function() {
		$scope.valid = false;

		$scope.usernameError = '';
		$scope.passwordError = '';
		$scope.displayNameError = '';

		$scope.usernameValid = false;
		$scope.passwordValid = false;
		$scope.displayNameValid = false;

		$scope.usernameChecked = false;
		$scope.passwordChecked = false;
		$scope.displayNameChecked = false;
	};
	reset();

	var firstUsername = true;
	$scope.$watchGroup(['username', 'usernameAccessed'], function() {
		if (firstUsername) {
			firstUsername = false;
			return;
		}

		if ($scope.usernameAccessed) {
			if ($scope.username) {
				$http.get(Constants.context + '/api/user/exists/' + encodeURIComponent($scope.username)).then(function(response) {
					$scope.usernameChecked = true;

					// IDEA: Implement user name proposals???
					$scope.usernameValid = !response.data.content.exists;

					$scope.usernameLastChanged = new Date().getTime();
				}, function(response) {
					alert('An error occurred!'); // TODO: Replace with something cooler.
				});
			} else {
				$scope.usernameChecked = true;
				$scope.usernameValid = false;

				$scope.usernameLastChanged = new Date().getTime();
			}
		}
	});
	var firstPassword = true;
	$scope.$watchGroup(['password', 'passwordRepeated', 'passwordAccessed', 'passwordRepeatedAccessed'], function() {
		if (firstPassword) {
			firstPassword = false;
			return;
		}

		if ($scope.passwordAccessed && $scope.passwordRepeatedAccessed) {
			$scope.passwordChecked = true;
			$scope.passwordValid = $scope.password && $scope.passwordRepeated && $scope.password === $scope.passwordRepeated;

			$scope.passwordLastChanged = new Date().getTime();
		}
	});
	var firstDisplayName = true;
	$scope.$watchGroup(['displayName', 'displayNameAccessed'], function() {
		if (firstDisplayName) {
			firstDisplayName = false;
			return;
		}

		if ($scope.displayNameAccessed) {
			$scope.displayNameChecked = true;
			$scope.displayNameValid = Boolean($scope.displayName);

			$scope.displayNameLastChanged = new Date().getTime();
		}
	});
	$scope.$watchGroup(['usernameValid', 'passwordValid', 'displayNameValid'], function() {
		$scope.valid = $scope.usernameValid && $scope.passwordValid && $scope.displayNameValid;
	});
	$scope.$watchGroup(['usernameLastChanged', 'passwordLastChanged', 'displayNameLastChanged'], function() {
		var requiredField = 'This is a required field!';
		$scope.usernameError = $scope.username ? 'Username already in use!' : requiredField;
		$scope.passwordError = $scope.password || $scope.passwordRepeated ? 'The passwords do not match!' : requiredField;
		$scope.displayNameError = $scope.displayName ? 'IMPOSSIBLE' : requiredField;
	});

	$scope.register = function() {
		if ($scope.usernameValid && $scope.passwordValid && $scope.displayNameValid) {
			$scope.registering = true;

			$http.put(Constants.context + '/api/user', {
				username : $scope.username,
				password : $scope.password,
				displayName : $scope.displayName
			}, {
				headers : {
					'X-CSRF-TOKEN' : csrf.token
				}
			}).then(function() {
				$('#register-form').submit();
			}, function() {
				alert('An error occurred!'); // TODO: Replace with something cooler.
			});
		}
	};
}]);
