angular.module('gdbApp').controller('loginController', ['$http', '$scope', 'csrf', function($http, $scope, csrf) {
	$scope.username = null;
	$scope.password = null;
	$scope.filled = false;
	$scope.authenticated = false;

	$scope.onCredentialChange = function() {
		if ($scope.username && $scope.password) {
			$http.post('/auth', {
				username : $scope.username,
				password : $scope.password
			}, {
				headers : {
					'X-CSRF-TOKEN' : csrf.token
				}
			}).then(function(response) {
				$scope.filled = true;
				$scope.authenticated = response.data.authenticated;
			}, function(response) {
				$scope.authenticated = false;
				$scope.filled = true;
			});
		} else {
			$scope.filled = false;
			$scope.authenticated = false;
		}
	};
	$scope.$watch('username', $scope.onCredentialChange);
	$scope.$watch('password', $scope.onCredentialChange);

	$scope.$watch('authenticated', function() {
		if ($scope.authenticated) {
			$('#login-form').submit();
		}
	})
}]);
