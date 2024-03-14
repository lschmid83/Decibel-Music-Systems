require './util'
require './settings'
require 'open-uri'
require 'rubygems'
require 'json'

def beginParticipantRetrieve(participantID, applicationID, applicationKey)

	# Set the request URL
    depth = 'Names;GroupMembers;Nationalities;GeographicAreas;Annotations;ChartsAwards;Relationships;'
	url = $apiAddress + 'Participants/?depth=' + depth + '&id=' + participantID + '&format=json' 

	# Send the GET request
	resp = request(url, applicationID, applicationKey)

	# Interpret the JSON response 
	data = JSON.parse(resp.body)

	# Return the participant object
	return data['ResultSet'][0]
	
end

def beginParticipantAssociates(participantID, applicationID, applicationKey)

	# Set the request URL
	depth = 'Names;GroupMembers;Nationalities;GeographicAreas;Annotations;ChartsAwards;Relationships;' 
	url = $apiAddress + 'Participants/' + participantID + '/Associates?depth=' + depth + '&format=json'

	# Send the GET request
	resp = request(url, applicationID, applicationKey)

	# Interpret the JSON response 
	data = JSON.parse(resp.body)

	# Return the collection of participant associate objects
	return data['ResultSet']

end

def beginParticipantTrackAppearances(participantID, applicationID, applicationKey)
	
	# Set the request URL
	depth = 'Names;GroupMembers;Nationalities;GeographicAreas;Annotations;ChartsAwards;Relationships;'
	url = $apiAddress + 'Participants/' + participantID + '/Tracks?depth=' + depth + '&format=json'

	# Send the GET request
	resp = request(url, applicationID, applicationKey)

	# Interpret the JSON response 
	data = JSON.parse(resp.body)

	# Return the collection of track appearance objects
	return data['ResultSet']
	
end

# Retreive participant with associations and track appearances  
participantID =	'fa94c7a0-cd68-48f3-b888-78a99733b60e'
result = beginParticipantRetrieve(participantID, $applicationID, $applicationKey)
puts 'Participant Name - ' + result['Name']

result = beginParticipantAssociates(participantID, $applicationID, $applicationKey)
puts 'Associates:';
result.each {
	|associate| puts associate['Participant']['Name']
}

result = beginParticipantTrackAppearances(participantID, $applicationID, $applicationKey)
puts 'Track Appearances:';
result.each {
	|trackAppearance| puts trackAppearance['TrackName'] + ' - ' + trackAppearance['AlbumName']
}
	
puts 'Press Enter to continue...'
input = gets.chomp