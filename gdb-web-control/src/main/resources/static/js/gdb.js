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

$(function() {
	// Enables the usage of multiple, overlapping modals.
	// Thanks to A1rPun who mentioned this solution on
	// StackOverflow (http://stackoverflow.com/a/24914782).
	$(document).on('show.bs.modal', '.modal', function () {
		var zIndex = 1040 + (10 * $('.modal:visible').length);
		$(this).css('z-index', zIndex);
		setTimeout(function() {
			$('.modal-backdrop').not('.modal-stack').css('z-index', zIndex - 1).addClass('modal-stack');
		}, 0);
	});
});

var gdbApp = angular.module('gdbApp');

gdbApp.filter('direction', function() {
	return function(data) {
		switch (data) {
			case 'ASC':
				return 'Ascending';
			case 'DESC':
				return 'Descending';
			default:
				return 'Other';
		};
	};
});
gdbApp.filter('platform', function() {
	var filter = function(platform) {
		switch (platform) {
			case 'WINDOWS':
				return 'Windows';
			case 'MAC':
				return 'Mac';
			case 'UNIX':
				return 'Linux';
			default:
				return 'Other';
		}
	};

	return function(data) {
		if (typeof data === 'string') {
			return filter(data);
		} else if (typeof data === 'object') {
			var result = []
			for (var i = 0; i < data.length; i++) {
				result.push(filter(data[i]));
			}
			return result.join(', ');
		} else {
			throw 'Data must either be a string or an object!';
		}
	};
});
gdbApp.filter('host', function() {
	return function(url) {
		return $('<a></a>').attr('href', url).prop('host');
	};
});

gdbApp.config(['$anchorScrollProvider', function($anchorScrollProvider) {
	$anchorScrollProvider.disableAutoScrolling = true;
}]);

gdbApp.run(['$rootScope', '$location', 'smoothScroll', function($rootScope, $location, smoothScroll) {
	// Handles the URL #<hashes>.
	var selfInvoke = false;
	$rootScope.$watch(function() {
		return $location.hash();
	}, function(hash) {
		if (selfInvoke) {
			selfInvoke = false;
			return;
		}

		hash = hash.indexOf('_r:') < 0 ? hash : hash.split('_r:')[0];

		selfInvoke = true;
		$location.hash(hash);

		var element = document.getElementById(hash);
		if (element) {
			smoothScroll(element, {
				offset : 80
			});
		}

		selfInvoke = true;
		$location.hash(hash + "_r:" + (new Date().getTime().toString(16)));
	});
}]);
