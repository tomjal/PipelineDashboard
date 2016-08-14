'use strict';

/**
 * Module dependencies.
 */
var mongoose = require('mongoose'),
  Schema = mongoose.Schema;

/**
 * Project Schema
 */
var ProjectSchema = new Schema({
  name: {
    type: String,
    default: '',
    required: 'Please fill Project name',
    trim: true
  },
  description: {
    type: String,
    default: '',
    required: 'Please fill Project description',
    trim: true
  },
  repositories: [
    {
      url: String,
      date: {
        type: Date,
        default: Date.now
      } 
    }
  ],
  created: {
    type: Date,
    default: Date.now
  },
  user: {
    type: Schema.ObjectId,
    ref: 'User'
  }
});

mongoose.model('Project', ProjectSchema);
