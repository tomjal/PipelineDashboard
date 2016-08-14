(function () {
  'use strict';

  // Projects controller
  angular
    .module('projects')
    .controller('ProjectsController', ProjectsController);

  ProjectsController.$inject = ['$scope', '$state', 'Authentication', 'projectResolve'];

  function ProjectsController ($scope, $state, Authentication, project) {
    var vm = this;

    vm.authentication = Authentication;
    vm.project = project;
    vm.error = null;
    vm.form = {};
    vm.remove = remove;
    vm.save = save;
    vm.addRepository = addRepository;
    vm.removeRepository = removeRepository;

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
      var newItemNo = vm.project.repositories.length+1;
      vm.project.repositories.push({'id':newItemNo, 'url':''});
    };

    // remove repository
    function removeRepository(url) {
      vm.project.repositories = vm.project.repositories.filter(function(item) {
        return item.url != url;
      });
    };
  }
})();
