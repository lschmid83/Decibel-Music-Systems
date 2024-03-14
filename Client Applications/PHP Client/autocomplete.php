<?php
/**
	Issues a request to the Decibel REST API for autocomplete suggestions 
	based on the search term and dictionary. The results are encoded in JSON
	for use with the jQuery Autocomplete UI component.
	
	Usage example:
	autocomplete.php?term=volume&dictionary=Albums
	
	@version 1.0
	@modified 19/10/2012
*/	

include 'util.php';
include 'admin.php';

// Get the autocomplete dictionary
$dictionary = trim(strip_tags($_GET['dictionary'])); 

// Get the search term that jQuery autocomplete sends
$term = trim(strip_tags($_GET['term'])); 
	
// Set the request URL
$url = $apiAddress . 'Autocomplete?text=' . urlencode($term) . '&dictionary=' . $dictionary . '&limit=5&format=json';

// Issue the request to the Decibel Web Service
$response = request($url, $applicationID, $applicationKey);

// Decode the JSON response 
$result = json_decode($response);

// Build an array of suggestions in jQuery Autocomplete format
$suggestions = $result->$dictionary; 
foreach ($suggestions as $suggestion) {
    $row['value'] = stripslashes($suggestion->SuggestionValue);
    $row['id'] = (int)$suggestion->ItemID;
    $row_set[] = $row; 
}

// Encode the array and output the result
echo json_encode($row_set);
?>