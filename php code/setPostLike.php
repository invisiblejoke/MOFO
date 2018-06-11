<?php

require "init.php";

	//details of the id which are going to be retrieve
	$username = $_POST["username"];
	$id = $_POST["id"];

	//set the query used to retrieve data
	$query = "select * from postLike where id = '".$id."';";

	//set the code to run the query
	$result = mysqli_query($con,$query);	

	//check if there is any row returned 
	if(mysqli_num_rows($result)>0)
	{
		//initialize the exist to false
		$exist = false;

		//set the array format
		$respond = array();
		
		//set returned result as data
		$data = mysqli_fetch_array($result);

		// split all data to by comma
		$user_id = explode("," , $data["username"]);


		//find username and delete it if exist
		foreach($user_id as $index => &$userID) {
 			if($userID == $username) {
  				unset($user_id[$index]);

  				//if exist, exist equal to true
  				$exist = true;
 				break;
			}
		}

		//deleted
		if ($exist)
		{

			//show its no longer in list
			$code = "false";	
		}

		//do not exist
		else
		{

		//add user to user_id
		$user_id[] = $username;

		//show its added in list
		$code = "true";	

		}

		//count the current likes
		$total_like = count($user_id);

		//change string back to array by changing spacebar to comma
		$user_id = implode(",", $user_id);

		//code to update the list
		$query1 = "UPDATE  `postLike` 
		SET `username` = '".$user_id."' 
		WHERE  `id` =  '".$id."' ;" ;

		//code to initiate the update
		mysqli_query($con,$query1);

		

		//if nothing come back, error occur
		if(!$result)
		{
			$respond = array();
			$code = "false";
			$total_like = "Something is wrong with the server. Please try again later.";

		}
		
		array_push($respond,array("code"=>$code,"total_like"=>$total_like));
		echo json_encode(array("server_response"=>$respond));
		

	}
	//if this id do not exist
	else
	{

		//should not happen

		$query = "insert into postLike values('".$id."','".$username."');";
		$respond = array();
		$total_like = "0";

		$code = "true";
		$message = "Something is wrong. this is creating now.";

		mysqli_query($con,$query);

		if(!$result)
		{
			$respond = array();
			$code = "false";
			$total_like = "Something is wrong with the server. Please try again later.";

		}

		array_push($respond,array("code"=>$code,"total_like"=>$total_like));
		echo json_encode(array("server_response"=>$respond));
		
	}
	
	
	mysqli_close($con);

?>

				