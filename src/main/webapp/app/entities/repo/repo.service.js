(function() {
    'use strict';
    angular
        .module('pipelineDashboardApp')
        .factory('Repo', Repo);

    Repo.$inject = ['$resource', 'DateUtils'];

    function Repo ($resource, DateUtils) {
        var resourceUrl =  'api/repos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdOn = DateUtils.convertDateTimeFromServer(data.createdOn);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
