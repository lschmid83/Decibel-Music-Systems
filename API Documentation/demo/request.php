<?php
/**
	Issues requests to the Decibel REST API and outputs the result.
	
	Usage example:
	request.php?search=AlbumInformation&id=25ec4f39-6411-4fa1-b280-23cd708d6f27
	
	@version 1.0
	@modified 19/10/2012
*/	

include 'admin.php';	
include 'util.php';

// Get the type of search
$search = trim($_GET['search']); 

// Get the Decibel ID
$id = trim($_GET['id']); 

// Construct the search URL 
if($search == "AlbumInformation")
	$url = "Albums/?depth=Tracks;Genres;Names;ExternalIdentifiers;Publications;Performances;Media;ImageThumbnail&format=json&id=" . $id;
else if($search == "ParticipantAssociates")
	$url = "Participants/" . $id . "/Associates" . "?format=json";
else if($search == "ParticipantTrackAppearances")
	$url = "Participants/" . $id . "/Tracks" . "?format=json";
else if($search == "WorkTrackAppearances")
	$url = "Works/" . $id . "/Tracks" . "?format=json";
	
// Issue the request to the Decibel Web Service
if(!empty($url)) {
	echo request($apiAddress . $url, $applicationID, $applicationKey);	
}
?>
