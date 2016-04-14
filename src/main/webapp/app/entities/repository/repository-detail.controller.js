(function() {
    'use strict';

    angular
        .module('pipelineDashboardApp')
        .controller('RepositoryDetailController', RepositoryDetailController);

    RepositoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Repository', 'Project'];

    function RepositoryDetailController($scope, $rootScope, $stateParams, entity, Repository, Project) {
        var vm = this;
        vm.repository = entity;
        vm.load = function (id) {
            Repository.get({id: id}, function(result) {
                vm.repository = result;
            });
        };
        var unsubscribe = $rootScope.$on('pipelineDashboardApp:repositoryUpdate', function(event, result) {
            vm.repository = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
