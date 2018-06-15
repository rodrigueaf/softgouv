(function() {
    'use strict';
    angular
        .module('softgouvApp')
        .factory('Cheferie', Cheferie);

    Cheferie.$inject = ['$resource'];

    function Cheferie ($resource) {
        var resourceUrl =  'api/cheferies/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
