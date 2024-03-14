<?php
/**
	Issues a request to the Decibel REST API for album images. 

	Usage example:
	album-image.php?albumID=25ec4f39-6411-4fa1-b280-23cd708d6f27&type=Thumbnail
	
	@version 1.0
	@modified 23/10/2012
*/		
	
include 'admin.php';	
include 'util.php';
	
// Get the album ID
$albumID = trim($_GET['albumID']); 

// Get the image type
$type = trim($_GET['type']); 

// Set the default image type
if(empty($type))
	$type = "Image";		

// Set the request URL
$url = $apiAddress . "Albums/" . $albumID . "/" . $type;

// Issue the request to the Decibel Web Service
$response = request($url, $applicationID, $applicationKey);
	
// Encodes the data with MIME base64
echo 'data:image/jpeg;base64,' . base64_encode($response);
?>
