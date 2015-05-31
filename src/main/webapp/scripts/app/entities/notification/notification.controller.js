'use strict';

angular.module('ozayApp')
.controller('NotificationController', function ($scope, $filter, $rootScope, $cookieStore, Notification, UserDetail) {
	$scope.button = true;
	$scope.showSuccessAlert = false;
	$scope.managementList = [];
	$scope.staffList = [];
	$scope.boardList = [];
	$scope.residentList = [];
	$scope.getAll = function (method, id) {
		UserDetail.get({method:method, id: id}, function(result) {
			$scope.example11data = [];

			var managementList = result[0].userDetailList;
			var staffList = result[1].userDetailList;
			var boardList = result[2].userDetailList;
			var residentList = result[3].userDetailList;
			for(var i = 0; i<managementList.length; i++){
				$scope.example11data.push({id: managementList[i].id, label: managementList[i].firstName + " " + managementList[i].lastName, role: '1'});
			}

			for(var i = 0; i<staffList.length; i++){
				$scope.example11data.push({id: staffList[i].id, label: staffList[i].firstName + " " + staffList[i].lastName, role: '2'});
			}

			for(var i = 0; i<boardList.length; i++){
				$scope.example11data.push({id: boardList[i].id, label: boardList[i].firstName + " " + boardList[i].lastName, role: '3'});
			}

			for(var i = 0; i<residentList.length; i++){
				$scope.example11data.push({id: residentList[i].id, label: residentList[i].firstName + " " + residentList[i].lastName, role: '4'});
			}


		});
	};

	$scope.example11settings = {
			enableSearch: true,
			scrollableHeight: '400px',
			scrollable: true,
			groupByTextProvider: function(groupValue) { if (groupValue === '1') { return 'Management'; }else if (groupValue === '2') { return 'Staff'; } else if (groupValue === '3') { return 'Board'; } else { return 'Resident'; } } };
	$scope.startProcess = function (method, id) {
		UserDetail.count({method:method, id: id}, function(result) {
			console.log(result);
			var result = result.response;
			$scope.showSuccessAlert = false;
			$scope.showErrorAlert = false;
			var message = "Do you want to send to " + result + " recipients";
			if(confirm(message)){
				$scope.notification.buildingId = $rootScope.selectedBuilding;
				$scope.notification.individuals = [];

				for(var i = 0; i<$scope.selectedUsers.length;i++){
					$scope.notification.individuals.push($scope.selectedUsers[i].id);
				}

				Notification.save($scope.notification,
						function (data) {
					$scope.showSuccessAlert = true;
					$scope.successTextAlert = data.response;
					$scope.notification.notice = "";
				}, function (error){
					$scope.showErrorAlert = true;
					$scope.errorTextAlert = "Error! Please try later.";
					$scope.button = true;
				});
			}
			$scope.button = true;
		});
	};

	var building = $rootScope.selectedBuilding;
	if(building === undefined){
		building = $cookieStore.get('selectedBuilding');
	}
	$scope.getAll('building', building);

	$scope.create = function () {
		$scope.button = false;
		$scope.startProcess('building_user_count', $rootScope.selectedBuilding);
	};

	$scope.update = function (id) {
		Notification.get({id: id}, function(result) {
			$scope.notification = result;
			$('#saveNotificationModal').modal('show');
		});
	};

	$scope.delete = function (id) {
		Notification.get({id: id}, function(result) {
			$scope.notification = result;
			$('#deleteNotificationConfirmation').modal('show');
		});
	};

	$scope.confirmDelete = function (id) {
		Notification.delete({id: id},
				function () {
			$scope.loadAll();
			$('#deleteNotificationConfirmation').modal('hide');
			$scope.clear();
		});
	};

	$scope.roleList = [{
		name: 'management',
		label:'Management'
	},{
		name: 'staff',
		label:'Staff'
	},{
		name: 'board',
		label:'Board'
	},{
		name: 'resident',
		label:'Resident'
	},{
		name: 'individual',
		label:'Individual'
	}];

	$scope.clear = function () {
		$scope.notification = {buildingId: null, notice: null, issueDate: null, createdBy: null, createdDate: null, id: null};
	};
	$scope.notification = {};
	$scope.notification.issueDate = new Date();

	$scope.minDate = new Date();

	$scope.selectedUsers = [];


}).controller('NotificationArchiveController', function ($scope, $filter, $rootScope, $cookieStore, Notification, UserDetail) {

	$scope.predicate = '-createdDate';
	$scope.notifications = [];
	$scope.loadAll = function() {
		var method= 'building';
		Notification.get({method:method, id:$rootScope.selectedBuilding}, function(result) {
			$scope.notifications = result;
		});
	};
	$rootScope.$watch('selectedBuilding', function() {
		if($rootScope.selectedBuilding !== undefined){
			$scope.loadAll();
		}
	});
});
