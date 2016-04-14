(function() {
    'use strict';

    angular
        .module('pipelineDashboardApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('repository', {
            parent: 'entity',
            url: '/repository',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Repositories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/repository/repositories.html',
                    controller: 'RepositoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('repository-detail', {
            parent: 'entity',
            url: '/repository/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Repository'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/repository/repository-detail.html',
                    controller: 'RepositoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Repository', function($stateParams, Repository) {
                    return Repository.get({id : $stateParams.id});
                }]
            }
        })
        .state('repository.new', {
            parent: 'repository',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/repository/repository-dialog.html',
                    controller: 'RepositoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                url: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('repository', null, { reload: true });
                }, function() {
                    $state.go('repository');
                });
            }]
        })
        .state('repository.edit', {
            parent: 'repository',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/repository/repository-dialog.html',
                    controller: 'RepositoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Repository', function(Repository) {
                            return Repository.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('repository', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('repository.delete', {
            parent: 'repository',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/repository/repository-delete-dialog.html',
                    controller: 'RepositoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Repository', function(Repository) {
                            return Repository.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('repository', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
