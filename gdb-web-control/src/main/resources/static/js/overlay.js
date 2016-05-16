window.Overlay = function($elementParam) {
	var me = this;

	console.assert($elementParam instanceof jQuery, '$elementParam must be an instance of jQuery!');

	var $element = $elementParam;
	var $overlay = $('<div></div>').addClass('overlay');

	me.attach = function() {
		$element.prepend($overlay);
	};
	me.detach = function() {
		$overlay.detach();
	};
};
