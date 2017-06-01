<?php

	

	$host = "mysql.hostinger.com.ar";
	$user = "u917821234_user";
	$pw = "volkogwi";
	$db = "u917821234_db";
	$con = mysqli_connect($host, $user, $pw, $db);

	if ($con->connect_error) {
	  trigger_error('Database connection failed: '  . $con->connect_error, E_USER_ERROR);
	} else {
		$con->query("SET NAMES 'utf8'");
	}

 ?>
