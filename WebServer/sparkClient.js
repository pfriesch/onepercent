/**
 * Manage the communication to the sparkserver.
 */

'use strict';

var net = require('net'); //Socketconnection
var config = require('./config.js'); //Configurationfile
var sparkClient = exports; // exports the sparkclientMethods

var client = new net.Socket(); // Creates Socket

var callbackToTATWebserver; // Callbackfunction to send the responsedata back to the OP_Webserver.

/* Connects to the Sparkserver with Configuationfile parameters per Websocket*/
client.connect(config.sparkServerPORT, config.sparkServerHOST, function() {
    console.log('CONNECTED TO: ' + config.sparkServerHOST + ':' + config.sparkServerPORT);
});
  
client.on('connect', function() {
  console.log('Sparkserver connected');
});

/* Gets the responsedata from Sparkserver and send it back to OP_Webserver by callbackfunction */
client.on('data', function(dataResponse) {
  var dataResponseJson = JSON.parse(dataResponse);
  if ('errorMessage' in dataResponseJson) {
    sparkErrorMessageResponse(dataResponseJson);
  }
  else {
    //console.log(dataResponseJson);
    callbackToTATWebserver(dataResponseJson);
  }
});

client.on('error', function(e) {
  console.log(new Date() + "Socket Error: " + e);
  if(e.code == 'ECONNREFUSED') {
    client.setTimeout(10000, function() {
      client.connect(config.sparkServerPORT, config.sparkServerHOST, function(){
        console.log('RECONNECTED TO: ' + config.sparkServerHOST + ':' + config.sparkServerPORT);
      });
    });
    console.log('Timeout for 10 seconds before trying to reconnect again');
  }
});

client.on('close', function() {
  console.log(new Date() + "Socket Connection closed trying to reconnect.");
  client.setTimeout(30000, function() {
    client.connect(config.sparkServerPORT, config.sparkServerHOST, function(){
      console.log('RECONNECTED TO: ' + config.sparkServerHOST + ':' + config.sparkServerPORT);
    });
  });
  console.log('Timeout for 30 seconds before trying to reconnect again');
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