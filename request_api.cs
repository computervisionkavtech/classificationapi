using System;
using System.Net.Http;
using System.Threading.Tasks;
using Json.Net;
using Newtonsoft.Json.Linq;

namespace API
{
    class Program
    {
        static string BASE_URL = "";
		
        private static async Task<string> RequestEndpoint(string image_url , string key , int score=0)
        {
            var url = BASE_URL + "/predict?urls=" + image_url + "&score=" + Convert.ToString(score) + "&key=" + key;
            using (var client = new HttpClient())
            {
                client.BaseAddress = new Uri(url);
                HttpResponseMessage response = await client.GetAsync(url);
                if (response.IsSuccessStatusCode)
                {
                    string strResult = await response.Content.ReadAsStringAsync();
                    JObject objects = JObject.Parse(strResult);
                    return (string)objects["req_id"]; 
                }
                else
                {
                    return null;
                }
            }
        }

        private static async Task<int> PercentageEndpoint(string id)
        {
            var url = BASE_URL +  "/predict/percentage?req_id=" + id;
            using (var client = new HttpClient())
            {
                client.BaseAddress = new Uri(url);
                HttpResponseMessage response = await client.GetAsync(url);
                if (response.IsSuccessStatusCode)
                {
                    string strResult = await response.Content.ReadAsStringAsync();
                    JObject objects = JObject.Parse(strResult);
                    return (int)objects["percentage"];
                }
                else
                {
                    return 0;
                }
            }
        }

        private static async Task<string> OutputEndpoint(string id)
        {
            var url = BASE_URL + "/predict/output?req_id=" + id;
            using (var client = new HttpClient())
            {
                client.BaseAddress = new Uri(url);
                HttpResponseMessage response = await client.GetAsync(url);
                if (response.IsSuccessStatusCode)
                {
                    string strResult = await response.Content.ReadAsStringAsync();
                    JObject objects = JObject.Parse(strResult);

                    string labels = "";
                    foreach(var x in objects["prediction"]["output"])
                    {
                        labels += (string)x["id"] + " : " + (string)x["label"] + "\n";
                    }

                    return labels;
                }
                else
                {
                    return null;
                }
            }
        }

        static void Main(string[] args)
        {

            string key = "YOUR_KEY";
            string image_url = "https://www.example.com/example_1.jpg,https://www.example.com/example_2.jpg";

            var req_id = RequestEndpoint(image_url , key).Result;
            Console.WriteLine(req_id);

            var percentage = 0;
            while (Convert.ToInt32(percentage) != 100)
            {
                percentage = PercentageEndpoint(req_id).Result;
                Console.WriteLine(percentage);
            }
            var result = OutputEndpoint(req_id).Result;
            Console.WriteLine(result);
        }
    }
}
