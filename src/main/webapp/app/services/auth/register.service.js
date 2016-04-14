(function () {
    'use strict';

    angular
        .module('pipelineDashboardApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
