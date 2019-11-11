var request = require("request");

var BASE_URL = "";
var reqq_id = '';

function RequestEndpoint(image_url, key, score = 0) {
    var url = BASE_URL + "/predict?urls=" + image_url + "&score=" + score + "&key=" + key;

    request.get({
            headers: {
                "Content-Type": "application/json",
            },
            url: url,
            json: true
        },
        function(error, response, body) {
            if (error) {
                console.log(error);
            } else {
                console.log(body.req_id);
                body = JSON.parse(JSON.stringify(body));
                reqq_id = body.req_id;

            }
        }
    );
}

function PercentageEndpoint(req_id) {
    var url = BASE_URL + "/predict/percentage?req_id=" + req_id;

    request.get({
            headers: {
                "Content-Type": "application/json",
            },
            url: url,
            json: true
        },
        function(error, response, body) {
            if (error) {
                console.log(error);
            } else {
                console.log(body.percentage);
                var status = true;
                console.log('percen: ' + req_id);
            }
        }
    );
}

function OutputEndpoint(req_id) {
    var url = BASE_URL + "/predict/output?req_id=" + req_id;

    request.get({
            headers: {
                "Content-Type": "application/json",
            },
            url: url,
            json: true
        },
        function(error, response, body) {
            if (error) {
                console.log(error);
            } else {
                var arr = body.prediction.output;
                console.log("arr: " + JSON.stringify(arr[1].label));
                var labels = "";
                for (var i = 0; i < arr.length; i++) {
                    labels += arr[i].id + " : " + arr[i].label + "\n";
                }
                console.log(labels);
            }
        }
    );
}

var key = "YOUR_KEY";
var image_url = "https://www.example.com/example_1.jpg,https://www.example.com/example_2.jpg";
RequestEndpoint(image_url, key);

