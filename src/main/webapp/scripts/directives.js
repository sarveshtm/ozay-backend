'use strict';

angular.module('ozayApp')
.directive('activeMenu', function($translate, $locale, tmhDynamicLocale) {
	return {
		restrict: 'A',
		link: function(scope, element, attrs, controller) {
			var language = attrs.activeMenu;

			scope.$watch(function() {
				return $translate.use();
			}, function(selectedLanguage) {
				if (language === selectedLanguage) {
					tmhDynamicLocale.set(language);
					element.addClass('active');
				} else {
					element.removeClass('active');
				}
			});
		}
	};
})
.directive('activeLink', function(location) {
	return {
		restrict: 'A',
		link: function(scope, element, attrs, controller) {
			var clazz = attrs.activeLink;
			var path = attrs.href;
			path = path.substring(1); //hack because path does bot return including hashbang
			scope.location = location;
			scope.$watch('location.path()', function(newPath) {
				if (path === newPath) {
					element.addClass(clazz);
				} else {
					element.removeClass(clazz);
				}
			});
		}
	};
}).directive('passwordStrengthBar', function() {
	return {
		replace: true,
		restrict: 'E',
		template: '<div id="strength">' +
		'<small translate="global.messages.validate.newpassword.strength">Password strength:</small>' +
		'<ul id="strengthBar">' +
		'<li class="point"></li><li class="point"></li><li class="point"></li><li class="point"></li><li class="point"></li>' +
		'</ul>' +
		'</div>',
		link: function(scope, iElement, attr) {
			var strength = {
					colors: ['#F00', '#F90', '#FF0', '#9F0', '#0F0'],
					mesureStrength: function (p) {

						var _force = 0;
						var _regex = /[$-/:-?{-~!"^_`\[\]]/g; // "

						var _lowerLetters = /[a-z]+/.test(p);
						var _upperLetters = /[A-Z]+/.test(p);
						var _numbers = /[0-9]+/.test(p);
						var _symbols = _regex.test(p);

						var _flags = [_lowerLetters, _upperLetters, _numbers, _symbols];
						var _passedMatches = $.grep(_flags, function (el) { return el === true; }).length;

						_force += 2 * p.length + ((p.length >= 10) ? 1 : 0);
						_force += _passedMatches * 10;

						// penality (short password)
						_force = (p.length <= 6) ? Math.min(_force, 10) : _force;

						// penality (poor variety of characters)
						_force = (_passedMatches == 1) ? Math.min(_force, 10) : _force;
						_force = (_passedMatches == 2) ? Math.min(_force, 20) : _force;
						_force = (_passedMatches == 3) ? Math.min(_force, 40) : _force;

						return _force;

					},
					getColor: function (s) {

						var idx = 0;
						if (s <= 10) { idx = 0; }
						else if (s <= 20) { idx = 1; }
						else if (s <= 30) { idx = 2; }
						else if (s <= 40) { idx = 3; }
						else { idx = 4; }

						return { idx: idx + 1, col: this.colors[idx] };
					}
			};
			scope.$watch(attr.passwordToCheck, function(password) {
				if (password) {
					var c = strength.getColor(strength.mesureStrength(password));
					iElement.removeClass('ng-hide');
					iElement.find('ul').children('li')
					.css({ "background": "#DDD" })
					.slice(0, c.idx)
					.css({ "background": c.col });
				}
			});
		}
	}
})
.directive('showValidation', function() {
	return {
		restrict: "A",
		require:'form',
		link: function(scope, element, attrs, formCtrl) {
			element.find('.form-group').each(function() {
				var $formGroup=$(this);
				var $inputs = $formGroup.find('input[ng-model],textarea[ng-model],select[ng-model]');

				if ($inputs.length > 0) {
					$inputs.each(function() {
						var $input=$(this);
						scope.$watch(function() {
							return $input.hasClass('ng-invalid') && $input.hasClass('ng-dirty');
						}, function(isInvalid) {
							$formGroup.toggleClass('has-error', isInvalid);
						});
					});
				}
			});
		}
	};
})
.directive('resize', function ($window, $location) {
	return function (scope, element, attr) {
		$('#side-menu').metisMenu();
		var url = $location.$$path;

		$(document).on('click', '.navbar-collapse a', function(){
            if($(this).attr('href') != '#'){
                $('.sidebar-nav').removeClass('in');
            }
		});

		var  topOffset = 0;
		var height = (($window.innerHeight > 0) ? $window.innerHeight : this.screen.height) - 1;
		height = height - topOffset;
		if (height < 1) height = 1;
		if (height > topOffset) {
			$("#page-wrapper").css("min-height", (height) + "px");
		}

		var element = $('ul.nav a').filter(function() {
			var res = this.href.split('#');
			var currentUrl = res[res.length - 1];
			var result = false;
			if(url != '/'){
				result = currentUrl ==  url;
			}
			
			return result;
		}).addClass('active').parent().parent().addClass('in').parent();
		if (element.is('li')) {
			element.addClass('active');
		}
	}
})
.directive('unitvalidation', function() {
	return {
		require: 'ngModel',
		link: function(scope, elm, attrs, ctrl) {

			var action = function() {
				if(attrs.management == 'true' || attrs.staff == 'true'){
					ctrl.$setValidity('unitvalidation', true);
				} else{
					if(ctrl.$modelValue === undefined || ctrl.$modelValue.length == 0){
						ctrl.$setValidity('unitvalidation', false);

					} else {
						ctrl.$setValidity('unitvalidation', true);
					}
				}
			}

			attrs.$observe('management', action);
			attrs.$observe('staff', action);

			ctrl.$validators.unitvalidation = function(modelValue, viewValue) {
				if(modelValue !== undefined && modelValue.length > 0){
					ctrl.$setValidity('unitvalidation', true);

					return true;
				} else {
					var management = scope.$eval(attrs.management);

					if(management == true ){
						return false;
					} else {

						return false;
					}
				}
				return true;
			};
		}
	};
});
