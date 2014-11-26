var net = require('net'); //socketverbindung
var moment = require('moment'); //timestampparser
var mysql = require('mysql'); // MySQL 

// ---------------------------- Mysql DB config ---------------------------------------

var connectionPool = mysql.createPool (
  {
    host     : 'db.f4.htw-berlin.de',
    port     : 3306,
    user     : 's0536746',
    password : '123xyz,.',
    database : '_s0536746__TwitterDB'
  }
);
// ---------------------------- Mysql DB config ---------------------------------------

//----------------------------------- Clientconfig ------------------------------------
var HOST = 'hadoop03.f4.htw-berlin.de';
var PORT = 5555;

var client = new net.Socket();

client.connect(PORT, HOST, function() {

    console.log('CONNECTED TO: ' + HOST + ':' + PORT);
    // Write a message to the socket as soon as the client is connected, the server will receive it as message from the client 
    
});
//----------------------------------- Clientconfig -----------------------------------

//----------------------------- sending data to server -------------------------------
client.on('connect', function() { 

    console.log('Connected');

    setInterval(function() {
        console.log('sending request every minute');
        sendHashtagRequestToServer();
    }, 60 * 100);

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

    var json = JSON.parse(data);
    console.log(json); 
    
    switch(json.job) {

      case 'hashtagtop10':
        //writeHashtagsIntoDatabase(data);
        break;

      case 'timezone':
        //writeTimezoneIntoDatabase(data);
        break;

      default:
        break;
}
    
    //client.destroy(); // Close the client socket completely
});
//----------------------------- recieving Data from JavaServer end ------------------- 

/* sends Hashtagtop10 request to javaServer */
function sendHashtagRequestToServer() {

    var time = moment();
    var timeString = time.format('YYYY-MM-DD HH-mm-ss');

    var hashtagtopten = {
        job: 'hashtagtop10',
	params: ['test','10'],
        time: timeString,
        ip: client.address().address,
        port: client.address().port
    }
    client.write(JSON.stringify(hashtagtopten) +'\n');
}
//----------------------------- DB Query ---------------------------------------------
function writeHashtagsIntoDatabase(hashTagData) {
    
  var json = JSON.parse(hashTagData);
  
  connectionPool.getConnection(function(err, connection) {
  
    for (var i=0; i<json.hashtags.length; i++) {

      var sqlQuery = 'INSERT INTO `Hashtagstop10` (hashtag, anzahl, time) VALUES ('
                  + connection.escape(json.hashtags[i].hashtag) + ','
                  + connection.escape(json.hashtags[i].anzahl) + ', '
                  + connection.escape(json.time) + ');' 
      
      console.log(sqlQuery);    
      
      connection.query(sqlQuery, function(err, rows, fields) {
        if (err) throw err;
      });    
    }
  connection.release();
  });
}

//readHashtagsFromDatabase();

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
      /*
      for (var i in rows) {
        console.log('Post Titles: ', rows[i].post_title);
    }*/
  });
    
  connection.release();
  });
}
//----------------------------- DB Query end -----------------------------------------

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
