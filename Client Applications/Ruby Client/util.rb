require 'net/https'
require "uri"

def request(url, applicationID, applicationKey)
	
	# Set the request URL
	uri = URI.parse(url)
	http = Net::HTTP.new(uri.host)
	request = Net::HTTP::Get.new(uri.request_uri)

	# Set the request authentication headers
	request.initialize_http_header({'DecibelAppID' => 'c45471dd', 
									'DecibelAppKey' => '62b53fc21e36ceb47bfc37a2565400c8', 
									'DecibelTimestamp' => '20130523 12:52:24'})

	# Send the GET request								
	return http.request(request)
end

def formatTime(seconds)
	
	return Time.at(seconds).strftime("%H:%M:%S")
	
end
