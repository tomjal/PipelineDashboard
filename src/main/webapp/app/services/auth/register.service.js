(function () {
    'use strict';

    angular
        .module('pipelinedashboardApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
