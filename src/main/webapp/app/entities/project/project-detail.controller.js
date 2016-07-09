(function() {
    'use strict';

    angular
        .module('pipelineDashboardApp')
        .controller('ProjectDetailController', ProjectDetailController);

    ProjectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Project', 'Repo'];

    function ProjectDetailController($scope, $rootScope, $stateParams, entity, Project, Repo) {
        var vm = this;

        vm.project = entity;

        var unsubscribe = $rootScope.$on('pipelineDashboardApp:projectUpdate', function(event, result) {
            vm.project = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
