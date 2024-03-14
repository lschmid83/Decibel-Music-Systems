import util
import json
import urllib
import settings

def beginAlbumSearch(albumTitle, applicationID, applicationKey):

	# Set the request URL
	url = settings.apiAddress + 'Albums/?albumTitle=' + urllib.parse.quote_plus(albumTitle) + '&format=json'
		
	# Send the GET request
	resp = util.request(url, applicationID, applicationKey)

	# Interpret the JSON response 
	data = json.loads(resp.decode('utf8'))

	# Return the collection of album objects
	return data['ResultSet']

# Search for albums by title	
albumTitle = input('Please enter a album title:')
result = beginAlbumSearch(albumTitle, settings.applicationID, settings.applicationKey)

# Output the search results
for album in result:
	print(album['Name'] + ' - ' + album['Artists'])

input('Press Enter to continue...')

