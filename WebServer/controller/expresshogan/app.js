var express = require('express');
var bodyParser = require('body-parser'); //json bodydarstellen
var moment = require('moment'); //timestampparser
var request = require('request'); // requesthandler

// ---------------------------- Mysql DB config --------------------------------
var mysql      = require('mysql');
var connection = mysql.createConnection({
  host     : 'db.f4.htw-berlin.de:3306',
  user     : '',
  password : '',
  database : '_s0536746__TwitterDB'
});

// ---------------------------- Mysql DB config --------------------------------

var app = express(); //expressframworkobject

app.use(bodyParser.json()); // jsonparser
app.use(bodyParser.urlencoded({ extended: false })); // jsonparser

// app.use(express.static(path.join(__dirname, 'public'))); // um Client auszuliefern

// serverport auf dem gelauscht wird
var server = app.listen(3000, function() {
    console.log('Listening on port %d', server.address().port);
});

//------------------------- Hashtagtop10 request ------------------------------

//writeHashtagsIntoDatabase();


setInterval(function() {
    console.log('test');
    getData();
}, 60 * 1000); 
 
function getData() {
    var time = moment();
    var timeString = time.format('YYYY-MM-DD HH-mm-ss');
    
    var options = {
        "url": "http://localhost:3000/test",
        "json": {
            job: 'Hashtagtop10',
            time: timeString,
            ip: 'localhost',
            port: '3000'
        }
    };

    request(options, function (err, response, body){
        if (!err && response.statusCode === 200 && body.status === "ok") {
            //console.log(body);
            console.log ("NODE-Server: Data is prepared ...");
        } else {
            console.error ("An error occurred");
        }
    });
}
//------------------------- Hashtagtop10 request ------------------------------

//-------------------------- Hasttagstop10 response ----------------------------

app.post("/test", function ( req, res ) {
    console.log("Node-Server POST (Actual Data): ",req.body);
    res.send({"status": "ok"});
});

//-------------------------- Hasttagstop10 response ----------------------------

//-------------------------- DB Query -----------------------------------------

function writeHashtagsIntoDatabase() {

    connection.connect();

        var sql = 'insert into HasttagsTop10 (hasttag, anzahl, timestamp) values (hallo, 100, 2014-12-10 11-11-11)'
        /*
        for (var i=0; i<=hashTagData.hasttags.length; i++) {

            var sql = 'insert into' + connection.escape(table) + '(hasttags, anzahl, timestamp) values (' 
                                    + connection.escape(hashTagData.hashtags[i].hasttag) + ',' 
                                    + connection.escape(hashTagData.hashTagData[i].anzahl) + ',' 
                                    + connection.escape(hashTagData[i].time) + ');' 
        
        */
            connection.query(sql, function(err, results) {
                if (err) throw err;
            });
        //}
    connection.end();
}

//-------------------------- DB Query -----------------------------------------

//------------------------- simiulierter Partner / Java Server ----------------
app.get("/test", function ( req, res ){
    console.log("J-Server: 1st-GET: ",req.body);
    
    res.send({
        "status": "ok",
    });

    setTimeout(function() {
        var time = moment();
        var timeStringJava = time.format('YYYY-MM-DD HH-mm-ss');
        var options = {
            "url": "http://localhost:3000/test",
            "method": "POST",
            "json": {
                job: 'Hasttagstop10',
                time: timeStringJava,
                hasttags: [{hasttag: 'ebola', anzahl: 200}, {hasttag: 'fussball', anzahl: 130}, {hasttag : 'tennis', anzahl: 100}],
                allposts: '500'
            }
        };

        request(options, function (err, response, body){
            if (!err && response.statusCode === 200 && body.status === "ok") {
                //console.log(body);
                console.log ("J-Server: Data was received");
            } else {
                console.error ("An error occurred");
            }
        });
    },3000);
});

//------------------------- simiulierter Partner / Java Server ----------------
