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

gdbApp.controller('preferencesController', ['$http', '$scope', '$timeout', 'csrf', '$rootScope', function($http, $scope, $timeout, csrf, $rootScope) {
	$scope.roles = ['ROLE_USER', 'ROLE_ADMIN'];
	$scope.brands = {
		processor : [{
			value : 'INTEL',
			name : 'Intel'
		}],
		graphics : [{
			value : 'NVIDIA',
			name : 'Nvidia'
		}]
	};
	$http.get(Constants.context + '/api/platform').then(function(response) {
		$scope.platforms = ((response.data._embedded || {}).osFamilyList || []);
	}, Dialog.ajaxError);

	$scope.$on('preferences_open-dialog', function() {
		$('#preferences-dialog').modal('show');
	});

	if (Constants.isUser) {
		// Computer systems tab.

		var MODE_CREATE = 'mode_create';
		var MODE_EDIT = 'mode_edit';

		$scope.cs = {
			systemSearch : '',
			systems : []
		};

		var reset = function() {
			if (!$scope.cs.dialog) {
				$scope.cs.dialog = {};
			}

			$scope.cs.dialog.descriptionTouched = false;
			$scope.cs.dialog.memoryTouched = false;
			$scope.cs.dialog.osFamilyTouched = false;
			$scope.cs.dialog.processorModelTouched = false;
			$scope.cs.dialog.graphicsModelTouched = false;

			$scope.cs.dialog.processorChecking = false;
			$scope.cs.dialog.graphicsChecking = false;

			$scope.cs.dialog.descriptionError = null;
			$scope.cs.dialog.memoryError = null;
			$scope.cs.dialog.osFamilyError = null;
			$scope.cs.dialog.processorError = null;
			$scope.cs.dialog.graphicsError = null;

			$scope.cs.dialog.id = 0;
			$scope.cs.dialog.mode = MODE_CREATE;
			$scope.cs.dialog.description = '';
			$scope.cs.dialog.memory = 0;
			$scope.cs.dialog.primary = false;
			$scope.cs.dialog.osFamily = 'WINDOWS';
			$scope.cs.dialog.processorBrand = '';
			$scope.cs.dialog.processorModel = '';
			$scope.cs.dialog.graphicsBrand = '';
			$scope.cs.dialog.graphicsModel = '';

			$scope.cs.dialog.valid = false;
			$scope.cs.dialog.trigger = new Date();
		};
		reset();

		var loadSystems = function() {
			$http.get(Constants.context + '/api/computer-system').then(function(response) {
				$scope.cs.systems = ((response.data._embedded || {}).computerSystemList || []);
			});
		};
		loadSystems();

		var prepareDialog = function() {
			reset();

			$('#cs-dialog').modal({
				backdrop : 'static',
				keyboard : false,
				show : false
			});
		};
		var showDialog = function() {
			$('#cs-dialog').modal('show');
		};
		var closeDialog = function() {
			$('#cs-dialog').modal('hide');

			reset();
		};

		$scope.cs.editSystem = function(index) {
			prepareDialog();

			var system = $scope.cs.systems[index];
			$scope.cs.dialog.id = system.id;
			$scope.cs.dialog.mode = MODE_EDIT;
			$scope.cs.dialog.description = system.description;
			$scope.cs.dialog.memory = system.memory,
			$scope.cs.dialog.primary = system.primary,
			$scope.cs.dialog.osFamily = system.osFamily,
			$scope.cs.dialog.processorBrand = system.processor.brand.name.toUpperCase();
			$scope.cs.dialog.processorModel = system.processor.model;
			$scope.cs.dialog.graphicsBrand = system.graphics.brand.name.toUpperCase();
			$scope.cs.dialog.graphicsModel = system.graphics.model;

			showDialog();

			$timeout(function() {
				// This is an edit call! Everything was touched before.

				$scope.cs.dialog.descriptionTouched = true;
				$scope.cs.dialog.memoryTouched = true;
				$scope.cs.dialog.osFamilyTouched = true;
				$scope.cs.dialog.processorModelTouched = true;
				$scope.cs.dialog.graphicsModelTouched = true;
			});
		};
		$scope.cs.createSystem = function() {
			prepareDialog();

			$scope.cs.dialog.mode = MODE_CREATE;

			showDialog();
		};

		$scope.cs.closeDialog = function() {
			Dialog.confirm('Are you sure you want to dismiss all changes?', true, closeDialog);
		};
		$scope.cs.saveSystem = function() {
			var finish = function() {
				closeDialog();
				loadSystems();
			};
			if ($scope.cs.dialog.mode === MODE_CREATE) {
				$http.put(Constants.context + '/api/computer-system', $scope.cs.dialog, {
					headers : {
						'X-CSRF-TOKEN' : csrf.token
					}
				}).then(function() {
					finish();

					Dialog.success('Computer system ' + $scope.cs.dialog.description + ' created successfully!');
				}, Dialog.ajaxError);
			} else {
				$http.patch(Constants.context + '/api/computer-system/' + encodeURIComponent($scope.cs.dialog.id), $scope.cs.dialog, {
					headers : {
						'X-CSRF-TOKEN' : csrf.token
					}
				}).then(function() {
					finish();

					Dialog.success('Data for computer system ' + $scope.cs.dialog.description + ' saved successfully!');
				}, Dialog.ajaxError);
			}
		};
		$scope.cs.deleteSystem = function(index) {
			var system = $scope.cs.systems[index];
			Dialog.confirm('Are you sure you want to delete the computer system ' + system.description + '?', true, function() {
				$scope.cs.systems.splice(index, 1);
				$http.delete(Constants.context + '/api/computer-system/' + encodeURIComponent(system.id), {
					headers : {
						'X-CSRF-TOKEN' : csrf.token
					}
				}).then(function() {
					Dialog.success('Computer system ' + system.description + ' deleted successfully!');
				}, Dialog.ajaxError);
			});
		};

		var requiredField = 'This is a required field!';
		$scope.$watchGroup(['cs.dialog.trigger', 'cs.dialog.descriptionTouched', 'cs.dialog.description'], function() {
			if ($scope.cs.dialog.descriptionTouched) {
				if ($scope.cs.dialog.description) {
					$scope.cs.dialog.descriptionError = false;
				} else {
					$scope.cs.dialog.descriptionError = requiredField;
				}
			} else {
				$scope.cs.dialog.descriptionError = null;
			}
		});
		$scope.$watchGroup(['cs.dialog.trigger', 'cs.dialog.memoryTouched', 'cs.dialog.memory'], function() {
			if ($scope.cs.dialog.memoryTouched) {
				if ($scope.cs.dialog.memory > 0) {
					$scope.cs.dialog.memoryError = false;
				} else if ($scope.cs.dialog.memory < 0) {
					$scope.cs.dialog.memoryError = 'Memory must not be less than zero!'
				} else {
					$scope.cs.dialog.memoryError = requiredField;
				}
			} else {
				$scope.cs.dialog.memoryError = null;
			}
		});
		$scope.$watchGroup(['cs.dialog.trigger', 'cs.dialog.osFamilyTouched', 'cs.dialog.osFamily'], function() {
			if ($scope.cs.dialog.osFamilyTouched) {
				if ($scope.cs.dialog.osFamily) {
					$scope.cs.dialog.osFamilyError = false;
				} else {
					$scope.cs.dialog.osFamilyError = requiredField;
				}
			} else {
				$scope.cs.dialog.osFamilyError = null;
			}
		});
		$scope.$watchGroup(['cs.dialog.trigger', 'cs.dialog.processorModelTouched',
				'cs.dialog.processorBrand', 'cs.dialog.processorModel'], function() {
			if ($scope.cs.dialog.processorModelTouched) {
				if ($scope.cs.dialog.processorBrand && $scope.cs.dialog.processorModel) {
					$scope.cs.dialog.processorChecking = true;
					$http.get(Constants.context + '/api/processor/'
							+ encodeURIComponent($scope.cs.dialog.processorBrand) + '/'
							+ encodeURIComponent($scope.cs.dialog.processorModel)).then(function(response) {
						$scope.cs.dialog.processorError = false;

						$scope.cs.dialog.processorChecking = false;
					}, function() {
						$scope.cs.dialog.processorError = 'Unknown brand/model combination!';

						$scope.cs.dialog.processorChecking = false;
					});
				} else {
					$scope.cs.dialog.processorError = requiredField;
				}
			} else {
				$scope.cs.dialog.processorError = null;
			}
		});
		$scope.$watchGroup(['cs.dialog.trigger', 'cs.dialog.graphicsModelTouched',
				'cs.dialog.graphicsBrand', 'cs.dialog.graphicsModel'], function() {
			if ($scope.cs.dialog.graphicsModelTouched) {
				if ($scope.cs.dialog.graphicsBrand && $scope.cs.dialog.graphicsModel) {
					$scope.cs.dialog.graphicsChecking = true;
					$http.get(Constants.context + '/api/graphics/'
							+ encodeURIComponent($scope.cs.dialog.graphicsBrand) + '/'
							+ encodeURIComponent($scope.cs.dialog.graphicsModel)).then(function() {
						$scope.cs.dialog.graphicsError = false;

						$scope.cs.dialog.graphicsChecking = false;
					}, function() {
						$scope.cs.dialog.graphicsError = 'Unknown brand/model combination!';

						$scope.cs.dialog.graphicsChecking = false;
					});
				} else {
					$scope.cs.dialog.graphicsError = requiredField;
				}
			} else {
				$scope.cs.dialog.graphicsError = null;
			}
		});
		$scope.$watchGroup(['cs.dialog.descriptionError', 'cs.dialog.memoryError',
				'cs.dialog.processorError', 'cs.dialog.graphicsError',
				'cs.dialog.processorChecking', 'cs.dialog.graphicsChecking'], function() {
			$scope.cs.dialog.valid = !$scope.cs.dialog.processorChecking && !$scope.cs.dialog.graphicsChecking
					&& $scope.cs.dialog.descriptionError === false
					&& $scope.cs.dialog.memoryError === false
					&& $scope.cs.dialog.processorError === false
					&& $scope.cs.dialog.graphicsError === false;
		});
	}

	if (Constants.isAdmin) {
		// Users tab.

		$scope.users = {
			userSearch : '',
			users : [],

			saveUser : function(index) {
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
			},
			deleteUser : function(index) {
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
			}
		};

		$http.get(Constants.context + '/api/user').then(function(response) {
			$scope.users.users = ((response.data._embedded || {}).userList || []);
		}, Dialog.ajaxError);
	}
}]);
