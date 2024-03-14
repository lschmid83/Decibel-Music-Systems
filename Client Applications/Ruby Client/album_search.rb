require './util'
require './settings'
require 'open-uri'
require 'rubygems'
require 'json'

def beginAlbumSearch(albumTitle, applicationID, applicationKey)

	# Set the request URL
	url = $apiAddress + 'Albums/?albumTitle=' + URI::encode(albumTitle) + '&format=json'
		
	# Send the GET request
	resp = request(url, applicationID, applicationKey)

	# Interpret the JSON response 
	data = JSON.parse(resp.body)

	# Return the collection of album objects
	return data['ResultSet']
	
end

# Search for albums by title	
puts 'Please enter a album title:'
albumTitle = gets.chomp
result = beginAlbumSearch(albumTitle, $applicationID, $applicationKey)

# Output the search results
result.each() { 
	|album| puts album['Name'] + ' - ' + album['Artists']
}

puts 'Press Enter to continue...'
input = gets.chomp