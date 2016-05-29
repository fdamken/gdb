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

gdbApp.filter('role', function() {
	return function(data) {
		var split = data.split('_');
		split.shift();
		for (var i = 0; i < split.length; i++) {
			split[i] = split[i].charAt(0).toUpperCase() + split[i].substring(1).toLowerCase();
		}
		var role = split.join(' ');
		return role;
	};
});
gdbApp.directive('gdbDirty', [function() {
	return {
		link : function($scope, element, attributes) {
			var obj = $scope.$eval(attributes.gdbDirty);
			var initial = JSON.stringify(obj);
			element.bind(attributes.gdbDirtyEvent ? attributes.gdbDirtyEvent : 'input', function() {
				var copy = $.extend({}, obj);
				delete copy.dirty;
				obj.dirty = initial !== JSON.stringify(copy);

				$scope.$apply();
			});
		}
	}
}]);

gdbApp.controller('preferencesController', ['$http', '$scope', '$timeout', 'csrf', function($http, $scope, $timeout, csrf) {
	$scope.roles = ['ROLE_USER', 'ROLE_ADMIN'];

	$scope.$on('preferences_open-dialog', function() {
		$('#preferences-dialog').modal('show');
	});

	if (Constants.isUser) {
		// Computer systems tab.

		$scope.cs = {
			systemSearch : '',
			systems : []
		};

		$http.get(Constants.context + '/api/computer-system').then(function(response) {
			$scope.cs.systems = ((response.data._embedded || {}).computerSystemList || []);
		});
	}

	if (Constants.isAdmin) {
		// Users tab.

		$scope.users = {
			userSearch : '',
			users : []
		};

		$http.get(Constants.context + '/api/user').then(function(response) {
			$scope.users.users = ((response.data._embedded || {}).userList || []);
		}, Dialog.ajaxError);

		$scope.saveUser = function(index) {
			var user = $scope.users.users[index];
			user.saving = true;
			$http.patch(Constants.context + '/api/user/' + encodeURIComponent(user.id), user, {
				headers : {
					'X-CSRF-TOKEN' : csrf.token
				}
			}).then(function() {
				user.saving = false;
				user.dirty = false;

				Dialog.success('Data for user ' + user.username + ' saved successfully!');
			}, Dialog.ajaxError);
		};
		$scope.deleteUser = function(index) {
			var user = $scope.users.users[index];
			Dialog.confirm('Are you sure you want to delete the user ' + user.username + '?', true, function() {
				$scope.users.users.splice(index, 1);
				$http.delete(Constants.context + '/api/user/' + encodeURIComponent(user.id), {
					headers : {
						'X-CSRF-TOKEN' : csrf.token
					}
				}).then(function() {
					Dialog.success('User ' + user.username + ' deleted successfully!');
				}, Dialog.ajaxError);
			});
		};
	}
}]);
