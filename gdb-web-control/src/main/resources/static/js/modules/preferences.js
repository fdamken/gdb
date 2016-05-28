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
	$scope.$on('preferences_open-dialog', function() {
		$('#preferences-dialog').modal('show');
	});

	if (Constants.isAdmin) {
		$scope.roles = ['ROLE_USER', 'ROLE_ADMIN'];

		$scope.users = [];

		$http.get(Constants.context + '/api/user').then(function(response) {
			$scope.users = response.data._embedded.userList;
		}, function() {
			alert('An error occurred!'); // TODO: Replace with something cooler.
		});

		$scope.saveUser = function(index) {
			var user = $scope.users[index];
			user.saving = true;
			$http.patch(Constants.context + '/api/user/' + encodeURIComponent(user.id), user, {
				headers : {
					'X-CSRF-TOKEN' : csrf.token
				}
			}).then(function() {
				user.saving = false;
				user.dirty = false;
			}, function() {
				alert('An error occurred!'); // TODO: Replace with something cooler.
			});
		};
		$scope.deleteUser = function(index) {
			var user = $scope.users[index];
			$scope.users.splice(index, 1);
			$http.delete(Constants.context + '/api/user/' + encodeURIComponent(user.id), {
				headers : {
					'X-CSRF-TOKEN' : csrf.token
				}
			}).then(function() {
				// Already removed from UI --> No message necessary.
			}, function() {
				alert('An error occurred!'); // TODO: Replace with something cooler.
			});
		};
	}
}]);
