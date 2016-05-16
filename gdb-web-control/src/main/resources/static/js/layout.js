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

var layoutFactory = null;

var LayoutPart = function(config) {
	var me = this;

	var config = $.extend({}, {
		id : null,
		title : null,
		side : 'left',
		$element : null
	}, config);

	console.assert(typeof config.id === 'string', 'ID must be a string!');
	console.assert(typeof config.title === 'string', 'Title must be a string!');
	console.assert(typeof config.side === 'string', 'Side must be a string!');
	console.assert(config.$element instanceof jQuery, 'Element must be an instance of jQuery!');

	var _id = config.id;
	var _title = config.title;
	var _side = config.side;
	var _$element = config.$element;

	var _prepared = false;
	var _shown = _side === 'left';

	me.prepare = function() {
		if (_prepared) {
			throw 'Layout part is already prepared!';
		}

		if (_$element.hasClass('overlay-parent')) {
			_$element.wrap($('<div></div>').addClass('overlay-parent'));
			_$element.removeClass('overlay-parent');
		}
		_$element.attr('id', _id);
		_$element.attr('data-layout', _id);
		_$element.addClass('layout');
		_$element.addClass('layout-' + _side);
		_$element.addClass('layout-' + (_side === 'left' ? 'visible' : 'hidden'));
		_$element.children('div').addClass('layout-body');

		var $controller = $(''
				+ '<div class="layout-controller">'
				+ '  <label class="layout-label">' + _title + '</label>'
				+ '  <span class="layout-btn layout-btn-left glyphicon glyphicon-chevron-left">&nbsp;</span>'
				+ '  <span class="layout-btn layout-btn-right glyphicon glyphicon-chevron-right">&nbsp;</span>'
				+ '</div>'
				+ '<span class="layout-separator">&nbsp;</span>'
				+ '');
		_$element.prepend($controller);

		$controller.click(function() {
			if (_shown) {
				layoutFactory.hide(_id);
			} else {
				layoutFactory.show(_id);
			}
		});
	};
	me.showOrHide = function(config, show) {
		config = $.extend({}, {
			animate : true,
			callback : function() { },
			duration : 50
		}, config);

		console.assert(typeof config.callback === 'function', 'Callback must be a function!');
		console.assert(typeof config.duration === 'number', 'Duration must be a number!');

		var animate = Boolean(config.animate);
		var callback = config.callback;
		var duration = config.duration;
		show = Boolean(show);
		var contentWidth = $('#content').outerWidth();

		var doAnimation = function(show, callback) {
			setTimeout(function() {
				var step = (contentWidth / duration) | 0;
				var pixelToMove = (contentWidth - (contentWidth % step));
				var interval = setInterval(function() {
					if (pixelToMove < 0) {
						clearInterval(interval);

						callback();
					} else {
						_$element.css(_side === 'right' ? 'left' : 'right', show ? pixelToMove : (contentWidth - pixelToMove));

						pixelToMove = pixelToMove - step;
					}
				}, 1);
			}, 0);
		};

		if (show) {
			if (animate) {
				doAnimation(true, function() {
					_$element.removeClass('layout-hidden').addClass('layout-visible');

					callback();
				});
			} else {
				if (_side === 'right') {
					_$element.css('left', 0);
				} else {
					_$element.css('right', 0);
				}
				_$element.removeClass('layout-hidden').addClass('layout-visible');
			}
		} else {
			if (animate) {
				doAnimation(false, function() {
					_$element.removeClass('layout-visible').addClass('layout-hidden');

					callback();
				});
			} else {
				if (_side === 'right') {
					_$element.css('left', contentWidth);
				} else {
					_$element.css('right', contentWidth);
				}
				_$element.removeClass('layout-visible').addClass('layout-hidden');
			}
		}

		_shown = show;
	};
	me.show = function(config) {
		return me.showOrHide(typeof config === 'boolean' ? {
			animate : config
		} : config, true);
	};
	me.hide = function(config) {
		return me.showOrHide(typeof config === 'boolean' ? {
			animate : config
		} : config, false);
	};
	me.focus = function() {
		location.hash = '/#' + _id;
	};

	me.getId = function() {
		return _id;
	};
	me.getTitle = function() {
		return _title;
	};
	me.getSide = function() {
		return _side;
	};
	me.getElement = function() {
		return _element;
	};
	me.isShown = function() {
		return _shown;
	};
};

gdbApp.factory('layout', [function() {
	var layoutPartsBySide = {
		right : [],
		left : []
	};
	var layoutPartsById = {};

	layoutFactory = {
		addLayoutPart : function(layoutPart) {
			console.assert(layoutPart instanceof LayoutPart, "The layout part must be an instance of LayoutPart!");

			layoutPartsBySide[layoutPart.getSide()].push(layoutPart);
			layoutPartsById[layoutPart.getId()] = layoutPart;
		},
		removeLayoutPart : function(side, id) {
			var layoutParts = layoutPartsBySide[side];
			for (var i = i; i < layoutParts.length; i++) {
				if (layoutParts[i].getId() === id) {
					delete layoutParts[i];
					break;
				}
			}
			delete layoutPartsById[id];
		},
		show : function(id, p_animateOrElementId, p_animate) {
			console.assert(layoutPartsById[id] !== null, 'ID is not a valid layout part ID!');

			var elementId = null;
			var animate = false;
			if (typeof p_animateOrElementId === 'string') {
				elementId = p_animateOrElementId;
				animate = p_animate;
			} else {
				animate = p_animateOrElementId;
			}

			var layoutPart = layoutPartsById[id];
			var side = layoutPart.getSide();
			var showParts = layoutPartsBySide[side];
			var hideParts = layoutPartsBySide[side === 'right' ? 'left' : 'right'];

			if (!layoutPart.isShown()) {
				for (var i = 0; i < showParts.length; i++) {
					showParts[i].show(animate);
				}
				for (var i = 0; i < hideParts.length; i++) {
					hideParts[i].hide(animate);
				}
			}

			if (elementId === null) {
				layoutPart.focus();
			} else {
				location.hash = '/#' + elementId;
			}
		},
		hide : function(id, animate) {
			console.assert(layoutPartsById[id] !== null, 'ID is not a valid layout part ID!');

			var side = layoutPartsById[id].getSide();
			var hideParts = layoutPartsBySide[side];
			var showParts = layoutPartsBySide[side === 'right' ? 'left' : 'right'];

			for (var i = 0; i < showParts.length; i++) {
				showParts[i].show(animate);
			}
			for (var i = 0; i < hideParts.length; i++) {
				hideParts[i].hide(animate);
			}
		}
	};

	return layoutFactory;
}]);

gdbApp.directive('gdbLayout', [function() {
	return {
		link : function($scope, element, attributes) {
			$scope.layout = $.extend({}, {
				id : null,
				title : null,
				side : 'left'
			}, $scope.$eval(attributes.gdbLayout));

			console.assert(typeof $scope.layout.id === 'string', 'Layout ID must be a string!');
			console.assert(typeof $scope.layout.title === 'string', 'Layout title must be a string!');
			console.assert($scope.layout.side === 'left' || $scope.layout.side === 'right', 'Layout side must either be "left" or "right"!');

			var layoutPart = new LayoutPart({
				id : $scope.layout.id,
				title : $scope.layout.title,
				side : $scope.layout.side,
				$element : $(element)
			});
			layoutPart.prepare();

			if ($scope.layout.side === 'left') {
				layoutPart.show(false);
			} else {
				layoutPart.hide(false);
			}

			layoutFactory.addLayoutPart(layoutPart);
		}
	}
}]);
