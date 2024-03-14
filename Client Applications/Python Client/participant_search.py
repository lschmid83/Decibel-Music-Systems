import util
import json
import urllib
import settings

def beginParticipantSearch(participantName, applicationID, applicationKey):

	# Set the request URL
	url = settings.apiAddress + 'Participants/?name=' + urllib.parse.quote_plus(participantName) + '&format=json'
		
	# Send the GET request
	resp = util.request(url, applicationID, applicationKey)

	# Interpret the JSON response 
	data = json.loads(resp.decode('utf8'))

	# Return the collection of participant objects
	return data['ResultSet']

# Search for participants by name	
participantName = input('Please enter a participant name:')
result = beginParticipantSearch(participantName, settings.applicationID, settings.applicationKey)

# Output the search results
for participant in result:
	print(participant['Name'] + ' - ' + participant['DateBorn']['Name'])

input('Press Enter to continue...')
