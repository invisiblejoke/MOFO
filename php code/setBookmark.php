<?php


	require "init.php";

	//details of the user to set bookmark detail
	$email = $_POST["email"];
	$id = $_POST["id"];
	$name = $_POST["name"];


	$query = "select * from bookmark where email = '".$email."' and id ='".$id."' ;";

	$result = mysqli_query($con, $query);

	//if the bookmark exist 
	if(mysqli_num_rows($result)>0)
	{

		//delete it from user bookmark
		$query = "DELETE from `bookmark` WHERE `bookmark`.`email` = '".$email."' and `bookmark`.`id` ='".$id."' ;" ;

		$code = "false";
		$message = "deleted";
		
	}
	//if bookmark do not exist
	else
	{
		$query = "INSERT INTO `bookmark` 
		(`email` ,`id` ,`name`)
		VALUES ('".$email."','".$id."','".$name."') ;" ;

		$code = "true";
		$message = "added";

	}

	//code to initiate the update
	$result = mysqli_query($con,$query);

	//do i need this?
	$respond = array();
		//if nothing come back, error occure
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
