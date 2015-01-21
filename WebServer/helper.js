var fs = require('fs');

module.exports.logData = logData;

/* Logs Data*/
function logData(data) {
  var dataToWrite = "";
  dataToWrite = '----------------------------------------------\n' 
              + 'Datalog: ' + new Date() + '\n'
              + data + '\n'
              + '----------------------------------------------\n';
                
  fs.appendFile('log.txt', dataToWrite, function (err) {
  });
}