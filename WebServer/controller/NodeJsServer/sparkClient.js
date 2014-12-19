/**
 * Manage the communication to the sparkserver.
 */

'use strict';

var net = require('net'); //Socketconnection
var config = require('./config.js'); //Configurationfile
var sparkClient = exports; // exports the sparkclientMethods

var client = new net.Socket(); // Creates Socket

var callbackToTATWebserver; // Callbackfunction to send the responsedata back to the TAT_Webserver.

/* Connects to the Sparkserver with Configuationfile parameters per Websocket*/
client.connect(config.sparkServerPORT, config.sparkServerHOST, function() {
    console.log('CONNECTED TO: ' + config.sparkServerHOST + ':' + config.sparkServerPORT);
  });
  
  client.on('connect', function() { 
    console.log('Sparkserver connected');
  });

  /* Gets the responsedata from Sparkserver and send it back to TAT_Webserver by callbackfunction */
  client.on('data', function(dataResponse) {
    var dataResponseJson = JSON.parse(dataResponse);
    if ('error' in dataResponseJson) {
      sparkErrorMessageResponse(dataResponseJson);
    }
    else {
      callbackToTATWebserver(dataResponseJson);
    }
  });

  client.on('error', function(e) {
    console.log("Error: " + e);
  });

  client.on('close', function() {
      console.log('Connection closed');
  });

/* Sends the jobData to sparkserver */
sparkClient.sendJobDataToServer = function sendJobRequestToSparkServer(jobData, callback) {
  callbackToTATWebserver = callback;
  console.log(JSON.stringify(jobData));
  client.write(JSON.stringify(jobData) +'\n');
}

/* if there is a errormesage in the responseString this method logs the errorcode and errormessage*/
function sparkErrorMessageResponse(sparkServerDataResponse) {
  console.log('Error-Message: ' + dataResponse.errormsg[0].errorMessage);
  console.log('Error-Message: ' + dataResponse.errormsg[0].errorCode);
}