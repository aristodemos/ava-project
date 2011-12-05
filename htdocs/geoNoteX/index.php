<?php 
	//connect and select database
	mysql_connect("localhost", "root", "");
	mysql_select_db("geonotes");

	//run query
	$result = mysql_query("SELECT * FROM users");
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1?DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<title> Geo Notes !! epl 651</title>
	<meta http-equiv="content-type" content="text/html;charset=utf-8" />
</head>

<body>
	<h1> Geo Notes</h1>
	<table border="1" cellpadding="2" cellspacing="3"
		summarry="User Info">
		<tr>
			<th> ID </th>
			<th> Name </th>
			<th> Password </th>
		</tr>
	<?php
		//loop through all rows
		while ($row = mysql_fetch_array($result)) {
			echo"<tr>";
			echo"<td>" . $row["user_id"] . "<?td>";
			echo"<td>" . $row["user_name"] . "<?td>";
			echo"<td>" . $row["user_password"] . "<?td>";
			echo"</tr>";
			}
		//free result memory and close databse connection
		mysql_free_result($result);
		mysql_close();
	?>
	</table>
</body>
</html>