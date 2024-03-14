require './util'
require './settings'
require 'open-uri'
require 'rubygems'
require 'json'

def beginAlbumRetrieve(albumID, applicationID, applicationKey)

	# Set the request URL
    depth = 'ExternalIdentifiers;Tracks;ImageThumbnail;Media;Names;Genres;Publications;Performances;'
	url = $apiAddress + 'Albums/?depth=' + depth + '&id=' + albumID + '&format=json'

	# Send the GET request
	resp = request(url, applicationID, applicationKey)

	# Interpret the JSON response 
	data = JSON.parse(resp.body)

	# Return the album object
	return data['ResultSet'][0]
	
end

# Retrieve album with track information		
result = beginAlbumRetrieve('d9675b59-7b88-40aa-9909-4e638839c799', $applicationID, $applicationKey)

# Output the search results
puts 'Name - ' + result['Name']
puts 'Artists - ' + result['Artists']
puts 'Disc Count - ' + result['DiscCount'].to_s
puts 'Track Count - ' + result['TrackCount'].to_s
puts 'Duration - ' + formatTime(result['TotalSeconds'].to_f)

begin
	puts 'Format - ' + result['Discs'][0]['MusicMedium']['Name']
rescue Exception=>e
end

begin
	begin
		externalIdentifiers = result['ExternalIdentifiers']
		externalIdentifiers.each {
			|externalIdentifier|
			if externalIdentifier['ExternalDatabase']['Name'] == 'UPC Barcode'
				puts 'Barcode - ' + externalIdentifier['Identifier']
				break
			end
		}
	rescue Exception=>e
	end
rescue Exception=>e
end

begin
    genreValues = ''
    genres = result['Genres']
    genres.each {
        |genre| genreValues += genre['Genre']['Name'] + ', '	
	}
	puts 'Genres - ' + genreValues.chomp(', ')
rescue Exception=>e
	end
	
begin
	puts 'Track List:'
    tracks = result['Tracks']
    tracks.each {
		|track| puts track['SequenceNo'].to_s + ') ' + track['Name'] + ' (' + formatTime(track['TotalSeconds'].to_f) + ')'
	}
	rescue Exception=>e
	end
    
puts 'Press Enter to continue...'
input = gets.chomp