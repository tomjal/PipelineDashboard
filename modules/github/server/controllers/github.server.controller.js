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

var github = 'https://api.github.com';

/**
 * Show the current Github
 */
exports.read = function (req, res) {

  cachedRequest
    .get({
      ttl: 60*60,
      url: github + '/repos/' + req.params.user + '/' + req.params.repo,
      headers: {
        'User-Agent': 'PipelineDashboard'
      }
    }, function (error, response, body) {
      res.jsonp(JSON.parse(body));
    });

};
