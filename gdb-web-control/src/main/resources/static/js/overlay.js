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

window.Overlay = function($elementParam) {
	var me = this;

	console.assert($elementParam instanceof jQuery, '$elementParam must be an instance of jQuery!');

	var $element = $elementParam;
	var $overlay = $('<div></div>').addClass('overlay');

	me.attach = function() {
		$element.addClass('overlay-attached');
		$element.prepend($overlay);
	};
	me.detach = function() {
		$overlay.detach();
		$element.removeClass('overlay-attached');
	};
};
