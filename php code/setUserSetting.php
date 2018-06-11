<?php

$email = $_POST["email"];
$distance = $_POST["distance"];
$userSetting = $_POST["userSetting"];

require "init.php";

$query = "select * from userSetting where email ='".$email."';";

$result = mysqli_query($con,$query);

if(mysqli_num_rows($result)>0)
{
	$query = "UPDATE `userSetting` SET `distance` =  '".$distance."',
	`userSetting` = '".$userSetting."' WHERE  `email` = '".$email."';";
	$code = "true";
	$message = "update success";
	

}
else
{

	$query = "insert into userSetting value('".$email."','".$distance."','".$userSetting."');";
	$code = "true";
	$message = "input success";

}
mysqli_query($con,$query);

$respond = array();

if(!$result)
{
	$respond = array();
	$code = "false";
	$message = "Server error occurred. Please try again later.";
}


array_push($respond,array("code"=>$code,"message"=>$message));
echo json_encode(array("server_response"=>$respond)); 
	
mysqli_close($con);

?>