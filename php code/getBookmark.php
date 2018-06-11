<?php

	require "init.php";

	//details of the user to retrieve bookmark from
	$email = $_POST["email"];

	//find this setting of this user
	$query = "select * from bookmark where email ='".$email."';";

	$result = mysqli_query($con,$query);	

	//set the array format
	$json=array();

	//check if there is any row returned 
	if(mysqli_num_rows($result)>0)
	{		
		//retrieve all data and save into the list
		while($row = mysqli_fetch_assoc($result4)) 
		{
            $json[]=$row;         
        }
	}
	
	echo json_encode(array("bookmark"=>$json));	
	
	mysqli_close($con);

?>

				