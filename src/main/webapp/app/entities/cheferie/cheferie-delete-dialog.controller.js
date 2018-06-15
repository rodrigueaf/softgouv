(function() {
    'use strict';

    angular
        .module('softgouvApp')
        .controller('CheferieDeleteController',CheferieDeleteController);

    CheferieDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cheferie'];

    function CheferieDeleteController($uibModalInstance, entity, Cheferie) {
        var vm = this;

        vm.cheferie = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cheferie.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
