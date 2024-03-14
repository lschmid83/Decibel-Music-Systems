require './util'
require './settings'
require 'open-uri'
require 'rubygems'
require 'json'

def beginWorkSearch(workName, applicationID, applicationKey)

	# Set the request URL
	url = $apiAddress + 'Works/?name=' + URI::encode(workName) + '&format=json'
		
	# Send the GET request
	resp = request(url, applicationID, applicationKey)

	# Interpret the JSON response 
	data = JSON.parse(resp.body)

	# Return the collection of track objects
	return data['ResultSet']
	
end

# Search for works by name
puts 'Please enter a work name:'
workName = gets.chomp
result = beginWorkSearch(workName, $applicationID, $applicationKey)

# Output the search results
result.each { 
	|work| puts work['Name'] + ' - ' + work['Composers']
}

puts 'Press Enter to continue...'
input = gets.chomp