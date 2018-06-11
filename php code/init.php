<?php

$host = "mysql.hostinger.in";
$user = "u245286763_admin";
$password = "qwer1234";
$dbname = "u245286763_user";

$con =  mysqli_connect($host, $user, $password, $dbname);

if(!$con)
{
	die("Error in database connection".mysqli_connect_error());
}else
{
	
}

?>