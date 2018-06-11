<?php

	$email = $_POST["email"];
	$pass = $_POST["password"];

	require "init.php";

	$query1 = "select * from user where email ='".$email."' and password='".$pass."';";
	$query2 = "select * from user where email ='".$email."';";
	$query3 = "select * from userSetting where email ='".$email."';";
	$query4 = "select * from bookmark where email ='".$email."';";

	$result1 = mysqli_query($con, $query1);
	$result2 = mysqli_query($con, $query2);
	$result3 = mysqli_query($con, $query3);
	$result4 = mysqli_query($con, $query4);


	if(mysqli_num_rows($result1)>0)
	{
			
		$respond = array();
		$code = "login_true";
		$user = array(mysqli_fetch_array($result1));

		$message = "Login Success. Welcome";
		
		array_push($respond,array("code"=>$code,"message"=>$message));

		
		$user_setting = array(mysqli_fetch_array($result3,MYSQLI_ASSOC));


	        $json=array();
	        if(mysqli_num_rows($result4)>0)
	        {
	                
	            while($row = mysqli_fetch_assoc($result4)) 
	            {
	                $json[]=$row;        
	            }
		}
	        

	        echo json_encode(array("server_response"=>$respond, "user"=>$user, "user_setting"=>$user_setting, "bookmark"=>$json));	

		}
		
	else 
	{
		if(mysqli_num_rows($result2)>0)
		{
			$respond = array();
			$code = "login_false";
			$message = "Login Fail. Email & password mismatch.";
			array_push($respond,array("code"=>$code,"message"=>$message));
			echo json_encode(array("server_response"=>$respond));
		}
		else
		{
			$respond = array();
			$code = "login_false";
			$message = "Login Fail. Email dont exist.";
			array_push($respond,array("code"=>$code,"message"=>$message));
			echo json_encode(array("server_response"=>$respond));
			
		}
		mysqli_close($con);
	}
?>
										