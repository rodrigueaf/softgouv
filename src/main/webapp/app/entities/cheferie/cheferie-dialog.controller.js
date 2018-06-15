(function() {
    'use strict';

    angular
        .module('softgouvApp')
        .controller('CheferieDialogController', CheferieDialogController);

    CheferieDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cheferie'];

    function CheferieDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cheferie) {
        var vm = this;

        vm.cheferie = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cheferie.id !== null) {
                Cheferie.update(vm.cheferie, onSaveSuccess, onSaveError);
            } else {
                Cheferie.save(vm.cheferie, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('softgouvApp:cheferieUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
