'use strict';

/**
 * Module dependencies
 */
var travisciPolicy = require('../policies/travisci.server.policy'),
  travisci = require('../controllers/travisci.server.controller');

module.exports = function(app) {
  // GitHub Routes
  app.route('/api/travisci/:user/:repo').all(travisciPolicy.isAllowed)
    .get(travisci.read);

};
