<?php

	$username = $_POST["username"];
	$email = $_POST["email"];
	$pass = $_POST["password"];

	require "init.php";

	$query1 = "select * from user where email like'".$email."';";
	$query2 = "select * from user where username like'".$username."';";

	$result1 = mysqli_query($con,$query1);
	$result2 = mysqli_query($con,$query2);


	//check for existing email
	if(mysqli_num_rows($result1)>0)
	{

		$respond = array();
		$code = "reg_false";
		$message = "Email Already Exist. Please try another email.";
		array_push($respond,array("code"=>$code,"message"=>$message));
		echo json_encode(array("server_response"=>$respond));
		
	}
	//check for existing username
	elseif (mysqli_num_rows($result2)>0) 
	{
		
		$respond = array();
		$code = "reg_false";
		$message = "Username Already Exist. Please try another username.";
		array_push($respond,array("code"=>$code,"message"=>$message));
		echo json_encode(array("server_response"=>$respond));
		
	}
	else
	{
		$query = "insert into user values('".$username."','".$email."','".$pass."');";
		$result = mysqli_query($con,$query);

		//preset the user setting for the user accordingly: email, distance, category
		$userSetting ="insert into userSetting values('".$email."','3','');";
		$newUS = mysqli_query($con,$userSetting);
		
		if(!$result)
		{
		
			$respond = array();
			$code = "reg_false";
			$message = "Server error occurred. Please try again later.";
	    	array_push($respond,array("code"=>$code,"message"=>$message));
			echo json_encode(array("server_response"=>$respond));
		
		}
		else
		{
			
			$respond = array();
			$code = "reg_true";
			$message = "Registration Success.";
			
			if($newUS)
			{
				$message = "Registration Success,input true";
			}
		
			array_push($respond,array("code"=>$code,"message"=>$message));
			echo json_encode(array("server_response"=>$respond));
		
		}
		
		mysqli_close($con);
	}

?>	