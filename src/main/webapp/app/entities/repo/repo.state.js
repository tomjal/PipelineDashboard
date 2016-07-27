(function() {
    'use strict';

    angular
        .module('pipelinedashboardApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('repo', {
            parent: 'entity',
            url: '/repo?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Repos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/repo/repos.html',
                    controller: 'RepoController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('repo-detail', {
            parent: 'entity',
            url: '/repo/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Repo'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/repo/repo-detail.html',
                    controller: 'RepoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Repo', function($stateParams, Repo) {
                    return Repo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'repo',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('repo-detail.edit', {
            parent: 'repo-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/repo/repo-dialog.html',
                    controller: 'RepoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Repo', function(Repo) {
                            return Repo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('repo.new', {
            parent: 'repo',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/repo/repo-dialog.html',
                    controller: 'RepoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                owner: null,
                                name: null,
                                createdOn: null,
                                updatedOn: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('repo', null, { reload: true });
                }, function() {
                    $state.go('repo');
                });
            }]
        })
        .state('repo.edit', {
            parent: 'repo',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/repo/repo-dialog.html',
                    controller: 'RepoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Repo', function(Repo) {
                            return Repo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('repo', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('repo.delete', {
            parent: 'repo',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/repo/repo-delete-dialog.html',
                    controller: 'RepoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Repo', function(Repo) {
                            return Repo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('repo', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
