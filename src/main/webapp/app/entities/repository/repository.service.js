(function() {
    'use strict';
    angular
        .module('pipelineDashboardApp')
        .factory('Repository', Repository);

    Repository.$inject = ['$resource'];

    function Repository ($resource) {
        var resourceUrl =  'api/repositories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
