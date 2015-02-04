/**
 * Manage the communication to the sparkserver.
 */

'use strict';

var net = require('net'); //Socketconnection
var config = require('./config.js'); //Configurationfile
var dataLogger = require('./helper.js'); // helperfunctions
var sparkClient = exports; // exports the sparkclientMethods

var client = new net.Socket(); // Creates Socket

var callbackToTATWebserver; // Callbackfunction to send the responsedata back to the OP_Webserver.

client.setMaxListeners(0);
/* Connects to the Sparkserver with Configuationfile parameters per Websocket*/
client.connect(config.sparkServerPORT, config.sparkServerHOST, function() {
    dataLogger.logData('CONNECTED TO: ' + config.sparkServerHOST + ':' + config.sparkServerPORT);
});
  
client.on('connect', function() {
  dataLogger.logData('Sparkserver connected');
});

/* Gets the responsedata from Sparkserver and send it back to OP_Webserver by callbackfunction */
client.on('data', function(dataResponse) {
  var dataResponseJson = JSON.parse(dataResponse);
  if ('errorMessage' in dataResponseJson) {
    sparkErrorMessageResponse(dataResponseJson);
  }
  else {
    callbackToTATWebserver(dataResponseJson);
  }
});

/* errorhandling if connection fails with sparkserver, client try to reconnect after 10sec if the server is not reachable */
client.on('error', function(e) {
  dataLogger.logData(new Date() + "Socket Error: " + e);
  if(e.code == 'ECONNREFUSED') {
    client.setTimeout(10000, function() {
      client.connect(config.sparkServerPORT, config.sparkServerHOST, function(){
        dataLogger.logData('RECONNECTED TO: ' + config.sparkServerHOST + ':' + config.sparkServerPORT);
      });
    });
    dataLogger.logData('Timeout for 10 seconds before trying to reconnect again');
  }
});

/* errorhandling if connection is closed, try to reconnect to server after 30sec. */
client.on('close', function() {
  dataLogger.logData(new Date() + "Socket Connection closed trying to reconnect.");
  client.setTimeout(30000, function() {
    client.connect(config.sparkServerPORT, config.sparkServerHOST, function(){
      dataLogger.logData('RECONNECTED TO: ' + config.sparkServerHOST + ':' + config.sparkServerPORT);
    });
  });
  dataLogger.logData('Timeout for 30 seconds before trying to reconnect again');
});

/* Sends the jobData to sparkserver */
sparkClient.sendJobDataToServer = function sendJobRequestToSparkServer(jobData, callback) {
  callbackToTATWebserver = callback;
  client.write(JSON.stringify(jobData) +'\n');
}

/* if there is a errormesage in the responseString this method logs the errorcode and errormessage*/
function sparkErrorMessageResponse(sparkServerDataResponse) {
  dataLogger.logData('Error-Message: ' + dataResponse.errormsg[0].errorMessage);
  dataLogger.logData('Error-Message: ' + dataResponse.errormsg[0].errorCode);
}