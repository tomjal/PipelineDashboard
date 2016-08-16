'use strict';

/**
 * Module dependencies.
 */
var path = require('path'),
  mongoose = require('mongoose'),
  errorHandler = require(path.resolve('./modules/core/server/controllers/errors.server.controller')),
  _ = require('lodash');

var request = require('request'),
  cachedRequest = require('cached-request')(request),
  cacheDirectory = 'tmp';

cachedRequest.setCacheDirectory(cacheDirectory);

var travisci = 'https://api.travis-ci.org';

/**
 * Show the current Travisci
 */
exports.read = function (req, res) {

  cachedRequest
    .get({
      ttl: 60*60,
      url: travisci + '/repos/' + req.params.user + '/' + req.params.repo,
      headers: {
        'User-Agent': 'PipelineDashboard'
      }
    }, function (error, response, body) {
      console.log(body);
      res.jsonp(JSON.parse(body));
    });

};
