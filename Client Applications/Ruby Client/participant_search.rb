require './util'
require './settings'
require 'open-uri'
require 'rubygems'
require 'json'

def beginParticipantSearch(participantName, applicationID, applicationKey)

	# Set the request URL
	url = $apiAddress + 'Participants/?name=' + URI::encode(participantName) + '&format=json'
		
	# Send the GET request
	resp = request(url, applicationID, applicationKey)

	# Interpret the JSON response 
	data = JSON.parse(resp.body)

	# Return the collection of participant objects
	return data['ResultSet']
	
end

# Search for participants by name 
puts 'Please enter a participant name:'
participantName = gets.chomp
result = beginParticipantSearch(participantName, $applicationID, $applicationKey)

# Output the search results
result.each() { 
	|participant| puts participant['Name'] + ' - ' + participant['DateBorn']['Name']
}

puts 'Press Enter to continue...'
input = gets.chomp