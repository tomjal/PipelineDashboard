(function() {
    'use strict';

    angular
        .module('pipelineDashboardApp')
        .controller('RepositoryDialogController', RepositoryDialogController);

    RepositoryDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Repository', 'Project'];

    function RepositoryDialogController ($scope, $stateParams, $uibModalInstance, entity, Repository, Project) {
        var vm = this;
        vm.repository = entity;
        vm.projects = Project.query();
        vm.load = function(id) {
            Repository.get({id : id}, function(result) {
                vm.repository = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pipelineDashboardApp:repositoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.repository.id !== null) {
                Repository.update(vm.repository, onSaveSuccess, onSaveError);
            } else {
                Repository.save(vm.repository, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
