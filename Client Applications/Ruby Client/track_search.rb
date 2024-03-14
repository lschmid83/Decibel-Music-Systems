require './util'
require './settings'
require 'open-uri'
require 'rubygems'
require 'json'

def beginTrackSearch(trackTitle, applicationID, applicationKey)

	# Set the request URL
	url = $apiAddress + 'Tracks/?trackTitle=' + URI::encode(trackTitle) + '&format=json'
		
	# Send the GET request
	resp = request(url, applicationID, applicationKey)

	# Interpret the JSON response 
	data = JSON.parse(resp.body)

	# Return the collection of track objects
	return data['ResultSet']
	
end

# Search for tracks by name
puts 'Please enter a track name:'
trackName = gets.chomp
result = beginTrackSearch(trackName, $applicationID, $applicationKey)

# Output the search results
result.each { 
	|track| puts track['Name'] + ' - ' + track['Artists']
}

puts 'Press Enter to continue...'
input = gets.chomp