<?php 
	include 'config.php';

	mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
	$konek = new Mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
	$konek->set_charset("utf8");

 ?>