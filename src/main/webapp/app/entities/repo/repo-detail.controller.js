(function() {
    'use strict';

    angular
        .module('pipelinedashboardApp')
        .controller('RepoDetailController', RepoDetailController);

    RepoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Repo'];

    function RepoDetailController($scope, $rootScope, $stateParams, previousState, entity, Repo) {
        var vm = this;

        vm.repo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pipelinedashboardApp:repoUpdate', function(event, result) {
            vm.repo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
