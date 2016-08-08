#!/usr/bin/env node --harmony

'use strict';
// load express, morgan and path modules for further usage
var
    express = require('express'),
    morgan = require('morgan'),
    path = require('path'),
    fs = require('fs');

var app = express();

var homepagePath = '/homepage';

app.use(morgan('dev'));
// set path to static files to deliver to client
app.use(express.static(__dirname + homepagePath));

// open endpoints
app.get('/', function (req, res) {

    res.sendFile(path.join(__dirname + 'index.html'));

});

app.get('/backendPort', function (req, res) {
    res.contentType('json');
    res.send({ backendPort: (process.argv[2] || 4040) });
});

app.get('/admin', function (req, res) {
    res.sendFile(path.join(__dirname  + homepagePath + '/admin.html'));
});

app.get('/api', function (req, res) {
    res.sendFile(path.join(__dirname + homepagePath + '/api.html'));
});

/*app.post('/upload', function (req, res) {
    var path = req.files.file.path;
    var name = req.files.file.name;
    fs.readFile(path, function (err, data) {
        var newPath = __dirname + "/upload/" + name;
        fs.rename(path, newPath, function (err) {
            console.log('rename callback ', err);
        });
    });
    res.sendFile(path.join(__dirname + 'index.html'));
});*/

app.get('/upload', function (req, res) {
    res.sendFile(path.join(__dirname + homepagePath + '/upload.html'));
});

// For Testpurpose: Community endpoints
app.get('/community', function (req, res) {
    res.sendFile(path.join(__dirname + homepagePath + '/community.html'));
});

//The 404 Route (ALWAYS Keep this as the last route)
app.get('*', function(req, res){
    res.status(404).sendFile(path.join(__dirname  + homepagePath + '/404.html'));
});

// start server
app.listen(4141, function () {
    console.log("ready for MArtIn operation.");
    console.log('backend port: ' + (process.argv[2] || 4040));
});
