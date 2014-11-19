<?php

	// Quelle: https://spinspire.com/article/creating-restful-api-using-slim-php-framework
	//			http://coenraets.org/blog/2011/12/restful-services-with-jquery-php-and-the-slim-framework/
	require 'Slim/Slim.php';
	\Slim\Slim::registerAutoloader();
	
	$app = new \Slim\Slim();
	$app->contentType('application/json');
	$app->get('/hourly/:table', 'getTimestamps');
	$app->get('/hourly/:table/:date', 'getAllByDate');
	$app->get('/hourly/:table/:date/:time', 'getAllByTime');
	//$app->post('/user', 'addUser');
	//$app->put('/user/:id', 'updateUser');
	//$app->delete('/user/:id', 'deleteUser');
	$app->run();
	
   function getConnection() {
		$dbhost="db.f4.htw-berlin.de";
		$dbuser="s0540031";
		$dbpass="webtech14";
		$dbname="_s0540031__Twitter";
		$dbh = new PDO("mysql:host=$dbhost;dbname=$dbname;charset=UTF8", $dbuser, $dbpass);
		$dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		return $dbh;
	}
	
	function checkTable($table) {
		$whitelist = array (
					"toptentags",
					);
		
		$table = filter_var($table, FILTER_SANITIZE_STRING);
		
		if(in_array($table, $whitelist)){
			return $table;
		}
		
		return NULL;
	}
	
	function convertDate($date, $add, $time = "00") {
		$time = filter_var($time, FILTER_SANITIZE_STRING);
		$timestamp = new DateTime($date . " " .$time . ":00:00", new DateTimeZone('GMT'));
		

		if($add != NULL) {
			$add = filter_var($add, FILTER_SANITIZE_STRING);
			$timestamp = $timestamp->add(new DateInterval($add));

		}
		// mit Zeitzonen angabe
		//$timestamp = $timestamp->format('Y-m-d H:i:sP');
		$timestamp = $timestamp->format('Y-m-d H:i:s');
		
		return $timestamp;
	}
	
	function getTimestamps($table) {
		
		$table = checktable($table);
		
		if($table == NULL) {
			return NULL;
		}
		
		$sql = "SELECT timestamp FROM $table GROUP BY timestamp";
		
		try {
			$db = getConnection();
			$stmt = $db->prepare($sql);
			$stmt->execute();
			$entries = $stmt->fetchAll(PDO::FETCH_OBJ);
			$db = null;
			echo json_encode($entries);
		}
		catch(PDOException $e) {
			echo json_encode($e->getMessage());
		}
	}
	
	function getAllByDate($table, $inputDate) {
	
		$table = checktable($table);
		
		if($table == NULL) {
			return NULL;
		}
		
		$date = convertDate($inputDate, NULL);
		$nextdate = convertDate($inputDate, 'P1D');
		
		$sql = "SELECT * FROM $table WHERE timestamp >= :date AND timestamp < :nextdate";
		
		try {
			$db = getConnection();
			$stmt = $db->prepare($sql);
			$stmt->bindParam(":date", $date);
			$stmt->bindParam(":nextdate", $nextdate);
			$stmt->execute();
			$entries = $stmt->fetchAll(PDO::FETCH_OBJ);
			$db = null;
			echo json_encode($entries);
		}
		catch(PDOException $e) {
			echo json_encode($e->getMessage());
		}
	}
	
	function getAllByTime($table, $inputDate, $inputTime) {
		$table = checktable($table);
		
		if($table == NULL) {
			return NULL;
		}
		
		$date = convertDate($inputDate, NULL , $inputTime);
		$nextdate = convertDate($inputDate, 'PT1H', $inputTime);
		
		$sql = "SELECT * FROM $table WHERE timestamp >= :date AND timestamp < :nextdate ORDER BY count DESC";
		
		try {
			$db = getConnection();
			$stmt = $db->prepare($sql);
			$stmt->bindParam(":date", $date);
			$stmt->bindParam(":nextdate", $nextdate);
			$stmt->execute();
			$entries = $stmt->fetchAll(PDO::FETCH_OBJ);
			$db = null;
			echo json_encode($entries);
		}
		catch(PDOException $e) {
			echo json_encode($e->getMessage());
		}
	}
?>
