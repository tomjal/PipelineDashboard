(function() {
    'use strict';

    angular
        .module('pipelinedashboardApp')
        .controller('RepoDialogController', RepoDialogController);

    RepoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Repo'];

    function RepoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Repo) {
        var vm = this;

        vm.repo = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.repo.id !== null) {
                Repo.update(vm.repo, onSaveSuccess, onSaveError);
            } else {
                Repo.save(vm.repo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pipelinedashboardApp:repoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdOn = false;
        vm.datePickerOpenStatus.updatedOn = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
