import time
import datetime
import urllib.request

def request(url, applicationID, applicationKey):
	
	# Set the request authentication headers
	timestamp = datetime.datetime.fromtimestamp(time.time()).strftime('%Y%m%d %H:%M:%S')
	headers = {'DecibelAppID': applicationID,
			   'DecibelAppKey': applicationKey,
			   'DecibelTimestamp': timestamp}

	# Send the GET request
	req = urllib.request.Request(url, None, headers)

	# Read the response
	return urllib.request.urlopen(req).read()
	
def formatTime(seconds):
	
	return time.strftime('%H:%M:%S', time.gmtime(seconds))
	