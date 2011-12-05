<?php 
/**  
* Geo!Suggest 
*  
* @package Geo!Suggest 
* @version 1.0 
*  
* This is a server side script of Geo!Suggest tool. 
* The main responsibility for this script is communication with Yahoo Geocodingg API and sending 
* results back to the client application (Geo!Suggest)  
*  
* History  
* 11/12/2005 A. Bidochko  
* - Created.  
*/ 

// Get input parameter 
$sAddress = (isset($_GET['address']) ? $_GET['address'] : ''); 

// We will send xml file back to the server 
Header("Content-type: text/xml; charset=UTF-8"); 

//do we have an address? 
if (empty($sAddress)) 
{ 
    // Return error 
    $sResponse = "<Error>The following errors were detected:<Message>Location is empty</Message></Error>"; 
    // Output the result XML 
    echo $sResponse; 
    // Succesfully terminate this script 
    exit(0); 
} 

################################################################################ 
# Talk to Yahoo Geocoding Service 
################################################################################ 

// Set misc values needed for the curl command 
$sPathToCurl        = 'curl' ; 
$sPostToURL         = 'http://api.local.yahoo.com/MapsService/V1/geocode' ; 
$aPostData          = array() ;  // This will hold the fieldname/value pairs 
$sEncapChar         = '"'; 
$aData['appid']     = 'mapbuilder.net'; 

//Create fieldname/value pairs 
foreach($aData as $sVar => $sValue) 
{ 
    // Double quotes are removed from the data and replaced with '*'. 
    // except for the encapsulation character 
    $sValue = str_replace($sEncapChar, '*', $sValue); 

    //URLencode the key and value and make them a name/value pair 
    $aPostData[] = urlencode($sVar) . '=' . urlencode($sValue); 
} 

//Seperate by & each of the name/value pairs 
$sPostData = implode('&', $aPostData); 

//Append Location 
$sPostData .= '&location=' . rawurlencode($sAddress); 

//Execute curl command 
$sPostString = $sPathToCurl . ' -s  --get --data ' . escapeshellarg($sPostData) . ' ' . escapeshellarg($sPostToURL); 
$sResponse = shell_exec($sPostString); 
// Output the result XML 
echo $sResponse; 

?>