(function() {
    'use strict';

    angular
        .module('softgouvApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cheferie', {
            parent: 'entity',
            url: '/cheferie?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Cheferies'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cheferie/cheferies.html',
                    controller: 'CheferieController',
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
        .state('cheferie-detail', {
            parent: 'cheferie',
            url: '/cheferie/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Cheferie'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cheferie/cheferie-detail.html',
                    controller: 'CheferieDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Cheferie', function($stateParams, Cheferie) {
                    return Cheferie.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cheferie',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cheferie-detail.edit', {
            parent: 'cheferie-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cheferie/cheferie-dialog.html',
                    controller: 'CheferieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cheferie', function(Cheferie) {
                            return Cheferie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cheferie.new', {
            parent: 'cheferie',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cheferie/cheferie-dialog.html',
                    controller: 'CheferieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                prenom: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cheferie', null, { reload: 'cheferie' });
                }, function() {
                    $state.go('cheferie');
                });
            }]
        })
        .state('cheferie.edit', {
            parent: 'cheferie',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cheferie/cheferie-dialog.html',
                    controller: 'CheferieDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cheferie', function(Cheferie) {
                            return Cheferie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cheferie', null, { reload: 'cheferie' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cheferie.delete', {
            parent: 'cheferie',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cheferie/cheferie-delete-dialog.html',
                    controller: 'CheferieDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cheferie', function(Cheferie) {
                            return Cheferie.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cheferie', null, { reload: 'cheferie' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
