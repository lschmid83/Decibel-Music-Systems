require './util'
require './settings'
require 'open-uri'
require 'rubygems'
require "base64"
require 'json'

def beginAlbumArtRetrieve(albumID, applicationID, applicationKey)

	# Set the request URL
	url = $apiAddress + 'Albums/' + albumID + '/Image';

	# Send the GET request
	resp = request(url, applicationID, applicationKey)

	return '<img src="data:image/jpeg;base64,' + Base64.encode64(resp.body) + '">'

end
	
result = beginAlbumArtRetrieve('5f754b70-c069-466e-a9c5-ae83ed638794', $applicationID, $applicationKey)

File.open('album_art_retrieve.html', 'w') { |file| file.write(result) }
	
puts 'Album art written to album_art_retrieve.html'
    
puts 'Press Enter to continue...'
input = gets.chomp


