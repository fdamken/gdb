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

gdbApp.controller('detailsController', ['$scope', '$http', '$sce', '$timeout', 'layout', function($scope, $http, $sce, $timeout, layout) {
	$scope.gameId = null;
	$scope.state = Constants.State.NOTHING;

	$scope.trust = function(html) {
		return $sce.trustAsHtml(html);	
	};

	var carouselInterval;
	var restartCarousel = function() {
		clearInterval(carouselInterval);
		var $carousel = $('#screenshots');

		$carousel.css('height', Math.ceil($carousel.width() / (16 / 9)));

		carouselInterval = setInterval(function() {
			if (!$carousel.is(':hover')) {
				$carousel.carousel('next');
			}
		}, 5000);
	};
	var stopCarousel = function() {
		clearInterval(carouselInterval);
	};

	$scope.$on('details_show', function(event, args) {
		$scope.gameId = args.gameId;

		layout.show('details');
	});
	$scope.$on('layout_on-show', function(event, args) {
		if (args.layoutPart === 'details' && $scope.state === Constants.State.LOADED) {
			$timeout(restartCarousel);
		}
	})
	$scope.$on('layout_on-hide', function(event, args) {
		if (args.layoutPart === 'details') {
			stopCarousel();
		}
	})

	$scope.$watch('gameId', function() {
		if ($scope.gameId) {
			$scope.state = Constants.State.LOADING;

			$http.get(Constants.context + '/api/game/' + $scope.gameId).then(function(response) {
				$scope.gameDetails = response.data;

				$timeout(function() {
					$scope.state = Constants.State.LOADED;

					$timeout(restartCarousel);
				});
			}, function(response) {
				alert('An error occurred!'); // TODO: Replace with something cooler.
			});
		} else {
			$scope.state = Constants.State.NOTHING;
		}
	});
}]);
