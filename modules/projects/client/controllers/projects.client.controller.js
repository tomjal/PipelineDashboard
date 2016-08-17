(function () {
  'use strict';

  // Projects controller
  angular
    .module('projects')
    .controller('ProjectsController', ProjectsController);

  ProjectsController.$inject = ['$scope', '$state', 'Authentication', 'projectResolve', '$http', '$interval'];

  function ProjectsController($scope, $state, Authentication, project, $http, $interval) {
    var vm = this;

    vm.authentication = Authentication;
    vm.project = project;
    vm.github = [];
    vm.travisci = [];
    vm.error = null;
    vm.form = {};
    vm.remove = remove;
    vm.save = save;
    vm.addRepository = addRepository;
    vm.removeRepository = removeRepository;
    vm.getGitHub = getGitHub;
    vm.getTravisCI = getTravisCI;

    var githubPromise = $interval(getAllGitHub, 60000);
    var travisPromise = $interval(getAllTravis, 60000);

    // Cancel interval on page changes
    $scope.$on('$destroy', function(){
      if (angular.isDefined(githubPromise)) {
        $interval.cancel(githubPromise);
        githubPromise = undefined;
      }
      if (angular.isDefined(travisPromise)) {
        $interval.cancel(travisPromise);
        travisPromise = undefined;
      }
    });

    function getAllGitHub() {
      angular.forEach(vm.project.repositories, function(value) {
        getGitHub(value.full_name);
      });
    }

    function getAllTravis() {
      angular.forEach(vm.project.repositories, function(value) {
        getTravisCI(value.full_name);
      });
    }

    // Remove existing Project
    function remove() {
      if (confirm('Are you sure you want to delete?')) {
        vm.project.$remove($state.go('projects.list'));
      }
    }

    // Save Project
    function save(isValid) {
      if (!isValid) {
        $scope.$broadcast('show-errors-check-validity', 'vm.form.projectForm');
        return false;
      }

      // TODO: move create/update logic to service
      if (vm.project._id) {
        vm.project.$update(successCallback, errorCallback);
      } else {
        vm.project.$save(successCallback, errorCallback);
      }

      function successCallback(res) {
        $state.go('projects.view', {
          projectId: res._id
        });
      }

      function errorCallback(res) {
        vm.error = res.data.message;
      }
    }

    // add repository
    function addRepository() {
      if (vm.project.repositories === undefined) {
        vm.project.repositories = [];
      }
      var newItemNo = vm.project.repositories.length + 1;
      vm.project.repositories.push({ 'id': newItemNo, 'full_name': '' });
    }

    // remove repository
    function removeRepository(full_name) {
      vm.project.repositories = vm.project.repositories.filter(function (item) {
        return item.full_name !== full_name;
      });
    }

    // get GitHub repository info
    function getGitHub(full_name) {
      $http({
        method: 'GET',
        url: '/api/github/' + full_name
      }).then(function successCallback(response) {
        vm.github[full_name] = response.data;
      });
    }

    // get TravisCI repository info
    function getTravisCI(full_name) {
      $http({
        method: 'GET',
        url: '/api/travisci/' + full_name
      }).then(function successCallback(response) {
        vm.travisci[full_name] = response.data;
      });
    }

  }
})();
