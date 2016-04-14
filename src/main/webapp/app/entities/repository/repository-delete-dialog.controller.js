(function() {
    'use strict';

    angular
        .module('pipelineDashboardApp')
        .controller('RepositoryDeleteController',RepositoryDeleteController);

    RepositoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'Repository'];

    function RepositoryDeleteController($uibModalInstance, entity, Repository) {
        var vm = this;
        vm.repository = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Repository.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
