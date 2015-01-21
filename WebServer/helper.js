/**
 * This is a class with helperfunctions.
 */

var fs = require('fs'); //IOstreamer

module.exports.logData = logData; // exports logdatafunction

/* Wirtes logdata into a logfile (logfile<Date>.txt) */
function logData(data) {
  var dataToWrite = "";
  var logFileName = "";

  logFileName = 'logfile' + new Date() + '.txt';

  dataToWrite = '----------------------------------------------\n' 
              + 'Datalog: ' + new Date() + '\n'
              + data + '\n'
              + '----------------------------------------------\n';
                
  fs.appendFile(logFileName, dataToWrite, function (err) {
  });
}
/*
fs.open('filepath', 'a', 666, function( e, id ) {
  fs.write( id, 'string to append to file', null, 'utf8', function(){
    fs.close(id, function(){
      console.log('file closed');
    });
  });
});
*/