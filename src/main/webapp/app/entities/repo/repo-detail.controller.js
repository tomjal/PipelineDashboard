(function() {
    'use strict';

    angular
        .module('pipelineDashboardApp')
        .controller('RepoDetailController', RepoDetailController);

    RepoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Repo', 'Project'];

    function RepoDetailController($scope, $rootScope, $stateParams, entity, Repo, Project) {
        var vm = this;

        vm.repo = entity;

        var unsubscribe = $rootScope.$on('pipelineDashboardApp:repoUpdate', function(event, result) {
            vm.repo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
