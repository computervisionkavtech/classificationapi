import requests

BASE_URL = ''

def RequestEndpoint(image_url , key , score):       	
    URL = BASE_URL + '/predict?urls=' + image_url + '&score=' + str(score) + '&key=' + key
    r = requests.get(URL)
    data = r.json()
    return data['req_id'] 

def PercentageEndpoint(id):
    URL = BASE_URL + '/predict/percentage?req_id=' + id
    r = requests.get(URL)
    data = r.json()
    return data['percentage']

def OutputEndpoint(id):
    URL = BASE_URL + '/predict/output?req_id=' + id
    r = requests.get(URL)
    data = r.json()
    labels = ''
    for x in data['prediction']['output']:
        labels += x['id'] + " : " + x['label'] + "\n"
    return labels

if __name__== "__main__":
    
	key = "YOUR_KEY";
	image_url = "https://www.example.com/example_1.jpg,https://www.example.com/example_2.jpg";
	req_id = RequestEndpoint(image_url , key , 0);
	print(req_id);
	percentage = 0;
	while percentage != 100:
		percentage = PercentageEndpoint(req_id)
		print(percentage);	
	result = OutputEndpoint(req_id)
	print(result)
