(function() {
    'use strict';

    angular
        .module('pipelinedashboardApp')
        .factory('RepoSearch', RepoSearch);

    RepoSearch.$inject = ['$resource'];

    function RepoSearch($resource) {
        var resourceUrl =  'api/_search/repos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
