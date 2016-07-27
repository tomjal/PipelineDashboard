(function() {
    'use strict';
    angular
        .module('pipelinedashboardApp')
        .factory('Project', Project);

    Project.$inject = ['$resource', 'DateUtils'];

    function Project ($resource, DateUtils) {
        var resourceUrl =  'api/projects/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdOn = DateUtils.convertDateTimeFromServer(data.createdOn);
                        data.updatedOn = DateUtils.convertDateTimeFromServer(data.updatedOn);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
