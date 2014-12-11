var net = require('net'); //socketverbindung
var moment = require('moment'); //timestampparser
var mysql = require('mysql'); // MySQL
var sha1 = require('sha1'); // Hashcode
var express = require('express'); // Http req res for Client

// ---------------------------- Mysql DB config ---------------------------------------

var connectionPool = mysql.createPool (
  {
    host     : 'db.f4.htw-berlin.de',
    port     : 3306,
    user     : 's0536746',
    password : '123xyz,.',
    database : '_s0540031__Twitter'
  }
);
// ---------------------------- Mysql DB config ---------------------------------------

//----------------------------- http req res config -----------------------------------
var app = express(); 

app.use(express.static('../../view/TAT_Frontend'));

var server = app.listen(8080, function(){
  console.log("Server is up and running");
});

//----------------------------------- Clientconfig ------------------------------------
//var HOST = 'localhost';
//var PORT = 9000;

var HOST = 'hadoop03.f4.htw-berlin.de';
var PORT = 5556;

var client = new net.Socket();

client.connect(PORT, HOST, function() {

    console.log('CONNECTED TO: ' + HOST + ':' + PORT);
    // Write a message to the socket as soon as the client is connected, the server will receive it as message from the client 
    
});
//----------------------------------- Clientconfig -----------------------------------

//------------------------------ http Request from Client ----------------------------
//var data = readHashTagTopTenFromDatabase('toptentags');
//console.log(data);

app.get('/api/hourly/:table', function(req, res){

  console.log("reqParam: " + req.params.table);

  connectionPool.getConnection(function(err, connection) {

  var sqlQuery = "SELECT * FROM " + req.params.table + " ORDER BY count DESC";
  console.log(sqlQuery);

    connection.query(sqlQuery, function(err, rows, fields){
      if(err) throw err;
      
      for (var i in rows) {
            console.log("sqlRequestparams:" + rows[i].name);
      }
      //var data = 'fussball';
      res.send(rows);
    });
  connection.release();
  });
});

/*
app.get('/hash/:table', function(req, res){

  console.log("reqParam: " + req.params.table);

  var data = readHashTagTopTenFromDatabase(req.params.table);
  console.log(data);
  res.send(data);
});

function readHashTagTopTenFromDatabase(params) {

  var sqlResponseData = '';

  connectionPool.getConnection(function(err, connection) {

    var sqlQuery = "SELECT * FROM " + params;
    console.log(sqlQuery);

    connection.query(sqlQuery, function(err, rows, fields){
      if(err) throw err;
      
      for (var i in rows) {
        //console.log("sqlRequestparams:" + rows[i].name);
      }
      sqlResponseData = rows;
      });
    connection.release();
  });
  return sqlResponseData;
}*/
//------------------------------ http Request from Client end-------------------------

//----------------------------- sending data to server -------------------------------
client.on('connect', function() { 

    console.log('Connected');
/*
    setInterval(function() {
        console.log('sending request every minute');
        sendHashtagRequestToServer();
    }, 60 * 1000);
*/
sendHashtagRequestToServer();
    //client.destroy(); // Close the client socket completely
});

client.on('error', function(e) {
  console.log("Error: " + e.message);
});

// Add a 'close' event handler for the client socket
client.on('close', function() {
    console.log('Connection closed');
});
//----------------------------- sending data to server end --------------------------

//----------------------------- recieving Data from JavaServer ---------------------- 
client.on('data', function(data) {

  //console.log(data);
  var dataResponse = JSON.parse(data);
  //console.log(dataResponse);

  if ('error' in dataResponse) {
    console.log('Error-Message: ' + dataResponse.errormsg[0].errorMessage);
    console.log('Error-Message: ' + dataResponse.errormsg[0].errorCode);
  }

  else {

    switch(getNameFromCollectionArrayById(dataResponse)) {

      case 'TopHashtagJob':
        console.log('switch case Hashtagjob');
        writeHashtagsIntoDatabase(dataResponse);
        removeHashIdFromCollection(dataResponse); // warten bis daten in sql geschrieben wurden!!
        break;

      case 'timezone':
        //writeTimezoneIntoDatabase(data);
        break;

      default:
        console.log('invalid jobID');
        break;
    }
  }
    //client.destroy(); // Close the client socket completely
});

function getNameFromCollectionArrayById (responseData) {

  if (checkIfElementIsInArray(responseData, hashTagTopTenCollection) == true) {
    var elementPos = hashTagTopTenCollection.map(function(hashTagTopTenCollection) {
    return hashTagTopTenCollection.id;
  }).indexOf(hashTagTopTenCollection.id);

  var fieldValue = hashTagTopTenCollection[elementPos].name;
  }
  return fieldValue;
}
//----------------------------- recieving Data from JavaServer end ------------------- 

//----------------------------- hash collection --------------------------------------

var hashTagTopTenCollection = new Array(); 

function addHashIdToCollection (hashTagTopTen) {

  var ElementInArray = checkIfElementIsInArray(hashTagTopTen, hashTagTopTenCollection);

  if (ElementInArray == true) {
    console.log('Element already in List');
  }

  else {
    hashTagTopTenCollection.push(hashTagTopTen);
    //console.log('Element added:' + hashTagTopTen);
    //console.log(JSON.stringify(hashTagTopTen));
  }
}
    
function checkIfElementIsInArray (newElement, elementArray) {
  var i;

  for (i = 0; i < elementArray.length; i++) {
      if (elementArray[i].id === newElement.id) {
          return true;
      }
  }
  return false;
}

  
function removeHashIdFromCollection(hashTagTopTen) {

  var elementPos = hashTagTopTenCollection.map(function(hashTagTopTenCollection) {
    return hashTagTopTenCollection.id;
  }).indexOf(hashTagTopTenCollection.id);
  
  var objectFound = hashTagTopTenCollection[elementPos];

  if (hashTagTopTenCollection[elementPos] != -1) {
    hashTagTopTenCollection.splice(hashTagTopTenCollection[elementPos], 1);
    console.log('Element removed');
  }

  else {
    console.log('ID not in List');
  }
}

function logHashIndex() {

  for (var i in hashTagTopTenCollection) {
    console.log(hashTagTopTenCollection[i]);
  }
}
//----------------------------- hash collection end-----------------------------------

/* sends Hashtagtop10 request to javaServer */
function sendHashtagRequestToServer() {

    var time = moment();
    var timeStringHour = time.format('YYYY-MM-DD HH');
    var timeString = time.format('YYYY-MM-DD HH-mm-ss');
    var hashtime = new Date();
    var id = sha1(hashtime.getTime());

    var hashTagTopTen = {
      jobID: id,
      name: 'TopHashtagJob',
      params: ['2014-11-30 05:00:00','hdfs://hadoop03.f4.htw-berlin.de:8020/studenten/s0540031/tweets/',10],
      time: timeString,
    }

    addHashIdToCollection(hashTagTopTen);
    //console.log('Element added:', hashTagTopTen);

    client.write(JSON.stringify(hashTagTopTen) +'\n');
}
//----------------------------- DB Query ---------------------------------------------
function writeHashtagsIntoDatabase(dataResponse) {
  
  console.log(dataResponse);
  var timestamp = getDateFromCollectionArrayById(dataResponse);
  
  connectionPool.getConnection(function(err, connection) {
  
    for (var i=0; i<dataResponse.jobResult.topHashtags.length; i++) {

      var sqlQuery = 'INSERT INTO `toptentags` (name, timestamp, count) VALUES ('
                  + connection.escape(dataResponse.jobResult.topHashtags[i].hashtag) + ','
                  + connection.escape(timestamp) + ', '
                  + connection.escape(dataResponse.jobResult.topHashtags[i].anzahl) + ');' 
      
      console.log(sqlQuery);    
      
      connection.query(sqlQuery, function(err, rows, fields) {
        if (err) throw err;
      });    
    }
  connection.release();
  });
}

function getDateFromCollectionArrayById (responseData) {

  logHashIndex();
  if (checkIfElementIsInArray(responseData, hashTagTopTenCollection) == true) {
    var elementPos = hashTagTopTenCollection.map(function(hashTagTopTenCollection) {
    return hashTagTopTenCollection.id;
  }).indexOf(hashTagTopTenCollection.id);

  var fieldValue = hashTagTopTenCollection[elementPos].time;
  }
  return fieldValue;
}
//----------------------------- DB Query end -----------------------------------------
//readHashtagsFromDatabase();
/*
function readHashtagsFromDatabase() {

  connectionPool.getConnection(function(err, connection) {
  
    var sqlQuery = 'SELECT * FROM Hashtagstop10';
  
    console.log(sqlQuery);

    connection.query(sqlQuery, function(err, rows, fields) {
      if (err) throw err;
      
      for (var i in rows) {
        console.log(rows[i].Hashtag);
    }
      //console.log(rows);
      //console.log(fields);
      
      for (var i in rows) {
        console.log('Post Titles: ', rows[i].post_title);
    }
  });
    
  connection.release();
  });
}


/*
//------------------------simulate string from javaserver --------------------
    var time = moment();
    var timeString = time.format('YYYY-MM-DD HH-mm-ss');

    var json = {
        job: 'Hasttagstop10',
        time: timeString,
        hasttags: [{hasttag: 'ebola', anzahl: 200}, {hasttag: 'fussball', anzahl: 130}, {hasttag : 'tennis', anzahl: 100}],
        allposts: '500'
    }
//------------------------simulate string from javaserver --------------------
*/
