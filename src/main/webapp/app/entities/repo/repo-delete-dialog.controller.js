(function() {
    'use strict';

    angular
        .module('pipelinedashboardApp')
        .controller('RepoDeleteController',RepoDeleteController);

    RepoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Repo'];

    function RepoDeleteController($uibModalInstance, entity, Repo) {
        var vm = this;

        vm.repo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Repo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
