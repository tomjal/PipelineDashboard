(function() {
    'use strict';

    angular
        .module('pipelineDashboardApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('repo', {
            parent: 'entity',
            url: '/repo',
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
            resolve: {
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
                }]
            }
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
                                createdOn: null,
                                owner: null,
                                name: null,
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
