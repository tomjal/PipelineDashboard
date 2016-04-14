(function() {
    'use strict';

    angular
        .module('pipelineDashboardApp')
        .controller('ProjectDetailController', ProjectDetailController);

    ProjectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Project', 'User'];

    function ProjectDetailController($scope, $rootScope, $stateParams, entity, Project, User) {
        var vm = this;
        vm.project = entity;
        vm.load = function (id) {
            Project.get({id: id}, function(result) {
                vm.project = result;
            });
        };
        var unsubscribe = $rootScope.$on('pipelineDashboardApp:projectUpdate', function(event, result) {
            vm.project = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
