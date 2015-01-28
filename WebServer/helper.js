/**
 * This is a class with helperfunctions.
 */

var fs = require('fs'); //IOstreamer

module.exports.logData = logData; // exports logdatafunction

/* Wirtes logdata into a logfile (logfile<Date>.txt) */
function logData(data) {
  var dataToWrite = "";
  
  dataToWrite = '----------------------------------------------\n' 
              + 'Datalog: ' + new Date() + '\n'
              + data + '\n'
              + '----------------------------------------------\n';
                
  fs.appendFile('logfile.txt', dataToWrite, function (err) {
  });
}
