<?php
/**
	Issues a request to the Decibel REST API for albums, participants, tracks and works.
	The results are encoded in JSON format for use with the jQuery Flexigrid UI component.
	
	Usage example:
	flexigrid-request.php?url=http://decibel-sample.cloudapp.net/v1/Albums/?artist=miles%20davis&search=artist
	
	@version 1.0
	@modified 22/10/2012
*/	
	
include 'admin.php';	
include 'util.php';
	
// Set the content type to JSON
header("Content-type: application/json");

// Get the results per page
$rp = trim($_GET['rp']); 

// Get the current page
$page = trim($_GET['page']); 

// Get the URL
$url = ltrim(http_build_query($_GET), 'url=');
$url = urldecode($url);
$url = str_replace(' ', '%20', $url);

// Set the page size parameter
$url .= '&pageSize=' . $rp;

// Determine the type of search from the URL
if (strlen(strstr($url, $apiAddress . 'Albums/')) > 0) 
	$search = "albums";
else if (strlen(strstr($url, $apiAddress . 'Participants/')) > 0) 
	$search = "participants";
else if (strlen(strstr($url, $apiAddress . 'Tracks/')) > 0) 	
	$search = "tracks";
else if (strlen(strstr($url, $apiAddress . 'Works/')) > 0) 	
	$search = "works";

// Start a session
session_start();

// Construct the search URL with page number
if($page != 1) {
	$url = $apiAddress . $search . '/Pages/' . $_SESSION['queryResultID'] . '?pageNumber=' . ($page-1);
}

// Issue the request to the Decibel Web Service
$response = request($url, $applicationID, $applicationKey);

// Interpret the XML response 
$result = simplexml_load_string($response);

// Set the result count
$resultCount = $result->ResultCount;   

// If this is the first page of results store the QueryResultID
if($page == 1) {
    $_SESSION['queryResultID'] = stripslashes($result->QueryResultID);
}

// Close and write session
session_write_close();

// Construct the Flexigrid JSON data array
$jsonData = array('page'=>$page,'total'=>stripslashes($resultCount),'rows'=>array());	

if($response == null) {
	echo json_encode($jsonData);
	exit();
}

$count = 1;
if($search == "albums")
{	
	// Get the collection of album objects
	$albums = $result->ResultSet->Album;
	
	// Construct the data rows
	foreach ($albums as $album) {
		$publications = $album->Publications->Publication;
			$entry = array('id'=>(int)$count,
				'cell'=>array(
						'id'=>stripslashes($album->ID),
						'index'=>(int)$count,
						'name'=>stripslashes($album->Name),
						'artists'=>stripslashes($album->Artists),
						'label'=>stripslashes($publications[0]->Publisher->Name),
						'length'=>stripslashes(formatTime($album->TotalSeconds)),
						'trackCount'=>stripslashes($album->TrackCount)
				),
		);
		$jsonData['rows'][] = $entry; 
		$count++;
	}
}
else if($search == "participants")
{
	$participants = $result->ResultSet->Participant;
	foreach ($participants as $participant) {
			$entry = array('id'=>(int)$count,
				'cell'=>array(
						'id'=>stripslashes($participant->ID),
						'index'=>(int)$count,
						'name'=>stripslashes($participant->Name),
						'gender'=>stripslashes($participant->Gender),
						'dateBorn'=>stripslashes($participant->DateBorn->Name),
						'dateDied'=>stripslashes($participant->DateDied->Name)
				),
		);
		$jsonData['rows'][] = $entry; 
		$count++;
	}		
}	
else if($search == "tracks")
{
	$tracks = $result->ResultSet->Track;
	foreach ($tracks as $track) {
			$entry = array('id'=>(int)$count,
				'cell'=>array(
						'trackID'=>stripslashes($track->ID),
						'albumID'=>stripslashes($track->AlbumID),
						'index'=>(int)$count,
						'name'=>stripslashes($track->Name),
						'artists'=>stripslashes($track->Artists),
						'number'=>stripslashes($track->SequenceNo),
						'length'=>stripslashes(formatTime($track->TotalSeconds))
				),
		);
		$jsonData['rows'][] = $entry; 
		$count++;
	}		
}	
else if($search == "works")
{
	$works = $result->ResultSet->Work;
	foreach ($works as $work) {
			$entry = array('id'=>(int)$count,
				'cell'=>array(
						'id'=>stripslashes($work->ID),
						'index'=>(int)$count,
						'name'=>stripslashes($work->Name),
						'composers'=>stripslashes($work->Composers)
				),
		);
		$jsonData['rows'][] = $entry; 
		$count++;
	}		
}		

// Encode the array and output the result
echo json_encode($jsonData);	
?>