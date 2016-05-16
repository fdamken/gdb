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
