<?php
/**
 * Makes a request using cURL with authentication headers and returns the response. 
 * @param url Decibel REST API URL.
 * @param applicationID Decibel Application ID.
 * @param applicationKey Decibel Application Key.
 * @return URL response.
 */	
function request($url, $applicationID, $applicationKey)
{	
	include 'admin.php';	
		
	// Check the referer is the host website	
	$referer = $_SERVER['HTTP_REFERER'];
	$referer_parse = parse_url($referer);
	if($referer_parse['host'] == $host) {	
	
		// Initialize the cURL session with the request URL
		$session = curl_init($url); 

		// Tell cURL to return the request data
		curl_setopt($session, CURLOPT_RETURNTRANSFER, true); 

		// Set the HTTP request authentication headers
		$headers = array(
			'DecibelAppID: ' . $applicationID,
			'DecibelAppKey: ' . $applicationKey,
			'DecibelTimestamp: ' . date('Ymd H:i:s', time())
		);
		curl_setopt($session, CURLOPT_HTTPHEADER, $headers);

		// Execute cURL on the session handle
		$response = curl_exec($session);
		
		return $response;
	}
	else {
		return NULL;
	}
}

/**
 * Converts seconds to hh:mm:ss.
 * @param sec Total number of seconds.
 * @return Seconds converted to a string in the format hh:mm:ss.
 */
function formatTime($sec, $padHours = false)
{
	$hms = '';
	
	// There are 3600 seconds in an hour, so if we
	// divide total seconds by 3600 and throw away
	// the remainder, we've got the number of hours
	$hours = intval(intval($sec) / 3600); 
	
	// Add to $hms, with a leading 0 if asked for
	$hms .= ($padHours) 
		  ? str_pad($hours, 2, "0", STR_PAD_LEFT). ':'
		  : $hours. ':';
	 
	// Dividing the total seconds by 60 will give us
	// the number of minutes, but we're interested in 
	// minutes past the hour: to get that, we need to 
	// divide by 60 again and keep the remainder
	$minutes = intval(($sec / 60) % 60); 

	// Then add to $hms (with a leading 0 if needed)
	$hms .= str_pad($minutes, 2, "0", STR_PAD_LEFT). ':';

	// Seconds are simple - just divide the total
	// seconds by 60 and keep the remainder
	$seconds = intval($sec % 60); 

	// Add to $hms, again with a leading 0 if needed
	$hms .= str_pad($seconds, 2, "0", STR_PAD_LEFT);

	return $hms;
}
?>