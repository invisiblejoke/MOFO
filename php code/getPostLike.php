<?php

require "init.php";

	//details of the id which are going to be retrieve
	$id = $_POST["id"];
	$username = $_POST["username"];

	//set the query used to retrieve data
	$query = "select * from postLike where id = '".$id."';";

	//set the code to run the query
	$result = mysqli_query($con,$query);	

	//check if there is any row returned 
	if(mysqli_num_rows($result)>0)
	{
		//initial username exist in list to false
		$code = "false";	
		
		//set the array format
		$respond = array();
		
		//set returned result as data
		$data = mysqli_fetch_array($result);

		// split all data to by comma
		$user_id = explode("," , $data["username"]);

		//find username 
		foreach($user_id as $index => &$userID) {
 			if($userID == $username) {

  				//if user name is in the list, exist equal to true
  				$code = true;
 				break;
			}
		}

		$total_like = count($user_id);

	}
	//if the id is not in the table
	else
	{
		//set up admin
		$admin="admin";

		//let admin be the 1st to like the post
		$query = "insert into postLike values('".$id."','".$admin."');";
		
		//run the query
		mysqli_query($con,$query);

		//set username in the list to false
		$code = "false";
		$total_like = "1";
		$respond = array();

		//if error occur
		if(!$result)
		{
			$respond = array();
			$code = "false";
			$total_like = "not available";

		}	
	
	}

	//return
	array_push($respond,array("total_like"=>$total_like,"code"=>$code));
	echo json_encode(array("server_response"=>$respond));

	mysqli_close($con);

?>

		
