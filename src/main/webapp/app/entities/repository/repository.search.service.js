(function() {
    'use strict';

    angular
        .module('pipelineDashboardApp')
        .factory('RepositorySearch', RepositorySearch);

    RepositorySearch.$inject = ['$resource'];

    function RepositorySearch($resource) {
        var resourceUrl =  'api/_search/repositories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
