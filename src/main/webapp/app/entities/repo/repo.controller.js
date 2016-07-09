(function() {
    'use strict';

    angular
        .module('pipelineDashboardApp')
        .controller('RepoController', RepoController);

    RepoController.$inject = ['$scope', '$state', 'Repo', 'RepoSearch'];

    function RepoController ($scope, $state, Repo, RepoSearch) {
        var vm = this;
        
        vm.repos = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Repo.query(function(result) {
                vm.repos = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            RepoSearch.query({query: vm.searchQuery}, function(result) {
                vm.repos = result;
            });
        }    }
})();
