<?php 

// Get input parameter 
$sqlo = (isset($_GET['qlo']) ? $_GET['qlo'] : ''); 
$sqla = (isset($_GET['qla']) ? $_GET['qla'] : ''); 
$smsg = (isset($_GET['msg']) ? $_GET['msg'] : ''); 
$susr = (isset($_GET['usr']) ? $_GET['usr'] : ''); 

echo $sqla;
echo $smsg; 
echo $sqlo; 
echo $susr; 

	//connect and select database
	mysql_connect("localhost", "root", "");
	mysql_select_db("geonotes");

	//run query
$sql = "SELECT `user_id` FROM `users` WHERE user_name=\"otinane\"";
$usrid = (string)mysql_query($sql) or trigger_error(mysql_error().$sql);	
echo $usrid;
$rest = substr($usrid, -1);
echo $rest;
$usrid = mysql_real_escape_string($usrid);
//$sql = "INSERT INTO `notes`(`note_id`, `note`, `user_id`, `long`, `lat`) VALUES (NULL, \"el a kaotnta\",\"2\",2.22,3.33)";
//$result = mysql_query($sql) or trigger_error((mysql_error().$sql);
$sql2 = "INSERT INTO `notes`(`note_id`, `note`, `user_id`, `long`, `lat`) VALUES (NULL, \"$smsg\",$rest,$sqla,$sqlo)";
$test = mysql_query($sql2) or trigger_error(mysql_error().$sql2);
$result = mysql_query("SELECT * FROM notes");

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
		summarry="Notes">
		<tr>
			<th> note ID </th>
			<th> Note </th>
			<th> user ID </th>
			<th> Long </th>
			<th> Lat </th>
		</tr>
	<?php
		//loop through all rows
		while ($row = mysql_fetch_array($result)) {
			echo"<tr>";
			echo"<td>" . $row["note_id"] . "<?td>";
			echo"<td>" . $row["note"] . "<?td>";
			echo"<td>" . $row["user_id"] . "<?td>";
			echo"<td>" . $row["long"] . "<?td>";
			echo"<td>" . $row["lat"] . "<?td>";
			echo"</tr>";
			}
		//free result memory and close databse connection
		mysql_free_result($result);
		mysql_close();
	?>
	</table>
</body>
</html>