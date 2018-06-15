(function() {
    'use strict';

    angular
        .module('softgouvApp')
        .controller('CheferieDetailController', CheferieDetailController);

    CheferieDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cheferie'];

    function CheferieDetailController($scope, $rootScope, $stateParams, previousState, entity, Cheferie) {
        var vm = this;

        vm.cheferie = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('softgouvApp:cheferieUpdate', function(event, result) {
            vm.cheferie = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
