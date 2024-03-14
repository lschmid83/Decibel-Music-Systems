require './util'
require './settings'
require 'open-uri'
require 'rubygems'
require 'json'

def beginWorkRetrieve(workID, applicationID, applicationKey)

	# Set the request URL
	depth = 'Annotations;ChartsAwards;Genres;Movements;Names;Nationalities;Publications;Publishers;'
	url = $apiAddress + 'Works/?depth=' + depth + '&id=' + workID + '&format=json'

	# Send the GET request
	resp = request(url, applicationID, applicationKey)

	# Interpret the JSON response 
	data = JSON.parse(resp.body)

	# Return the work object
	return data['ResultSet'][0]
	
end

def beginWorkTrackAppearances(workID, applicationID, applicationKey)

	# Set the request URL
	depth = 'Annotations;ChartsAwards;Genres;Movements;Names;Nationalities;Publications;Publishers;'
	url = $apiAddress + 'Works/' + workID + '/Tracks' + '?depth=' + depth + '&format=json'

	# Send the GET request
	resp = request(url, applicationID, applicationKey)

	# Interpret the JSON response 
	data = JSON.parse(resp.body)

	# Return the collection of participant associate objects
	return data['ResultSet']

end

# Retrieve work with track appearances	
workID = 'a3ef26b4-0e82-426a-b1f8-18b23dbd22b2'
result = beginWorkRetrieve(workID, $applicationID, $applicationKey)
puts 'Name - ' + result['Name']
puts 'Composers - ' + result['Composers']
puts 'Catalogue - ' + result['Catalogue']

result = beginWorkTrackAppearances(workID, $applicationID, $applicationKey)
puts 'Track Appearances:';
result.each {
	|trackAppearance| puts trackAppearance['TrackName'] + ' - ' + trackAppearance['TrackArtistName'] + ' - ' + trackAppearance['AlbumName']
}
	
puts 'Press Enter to continue...'
input = gets.chomp