(function() {
    'use strict';

    angular
        .module('pipelineDashboardApp')
        .controller('ProjectDeleteController',ProjectDeleteController);

    ProjectDeleteController.$inject = ['$uibModalInstance', 'entity', 'Project'];

    function ProjectDeleteController($uibModalInstance, entity, Project) {
        var vm = this;
        vm.project = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Project.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
