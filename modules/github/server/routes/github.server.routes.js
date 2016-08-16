'use strict';

/**
 * Module dependencies
 */
var githubPolicy = require('../policies/github.server.policy'),
  github = require('../controllers/github.server.controller');

module.exports = function(app) {
  // GitHub Routes
  app.route('/api/github/:user/:repo').all(githubPolicy.isAllowed)
    .get(github.read);

};
