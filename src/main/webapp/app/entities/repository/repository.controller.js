(function() {
    'use strict';

    angular
        .module('pipelineDashboardApp')
        .controller('RepositoryController', RepositoryController);

    RepositoryController.$inject = ['$scope', '$state', 'Repository', 'RepositorySearch'];

    function RepositoryController ($scope, $state, Repository, RepositorySearch) {
        var vm = this;
        vm.repositories = [];
        vm.loadAll = function() {
            Repository.query(function(result) {
                vm.repositories = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            RepositorySearch.query({query: vm.searchQuery}, function(result) {
                vm.repositories = result;
            });
        };
        vm.loadAll();
        
    }
})();
