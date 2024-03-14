require './util'
require './settings'
require 'open-uri'
require 'rubygems'
require 'json'

def beginTrackRetrieve(trackID, applicationID, applicationKey)

	# Set the request URL
	depth = 'Names;Participations;ExternalIdentifiers;Genres;Performances;'
	url = $apiAddress + 'Tracks/?depth=' + depth + '&id=' + trackID + '&format=json'

	# Send the GET request
	resp = request(url, applicationID, applicationKey)

	# Interpret the JSON response 
	data = JSON.parse(resp.body)

	# Return the track object
	return data['ResultSet'][0]
	
end

# Retrieve track with participations
trackID = '0703d0ef-73bf-40a3-888e-bae8bc96ca7a'
result = beginTrackRetrieve(trackID, $applicationID, $applicationKey)

# Output the results
puts 'Track Information:'
puts 'Name - ' + result['Name']
puts 'Artists - ' + result['Artists']
puts 'Track Number - ' + result['TrackNumber']
puts 'Disc Number - ' + result['DiscNumber']
puts 'Duration - ' + formatTime(result['TotalSeconds'].to_f)

puts 'Participations:';
result['Participations'].each {
	|participant| puts participant['Name'];
}
	
puts 'Press Enter to continue...'
input = gets.chomp