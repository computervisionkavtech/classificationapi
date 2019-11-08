import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.*;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class API_Project {

    static String BASE_URL = "";
    
    public static String RequestEndpoint(String image_url , String key , int score) throws IOException , JSONException{
        
        String query = BASE_URL + "/predict?urls=" + image_url + "&score=" + Integer.toString(score) + "&key=" + key;
        URL url = new URL(query);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        
        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))){
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
            }
            String resp = response.toString();
            JSONObject jsonObject = new JSONObject(resp);
            return jsonObject.getString("req_id");   
        }
    }
    
    public static int PercentageEndpoint(String id) throws IOException , JSONException{
        
        String query = BASE_URL +  "/predict/percentage?req_id=" + id;
        URL url = new URL(query);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);

        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))){
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
            }
            String resp = response.toString();
            JSONObject jsonObject = new JSONObject(resp);
            return jsonObject.getInt("percentage");   
        }
    }
    
    public static String OutputEndpoint(String id) throws IOException , JSONException{
        
        String query = BASE_URL +  "/predict/output?req_id=" + id;
        URL url = new URL(query);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        int status = con.getResponseCode();
        System.out.println(status); 
        
        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))){
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
            }
            String resp = response.toString();
            JSONObject jsonObject = new JSONObject(resp);
            jsonObject = jsonObject.getJSONObject("prediction");
            JSONArray array = jsonObject.getJSONArray("output");
            responseLine = "";
            for(int i = 0 ; i < array.length() ; i++){
                responseLine += array.getJSONObject(i).getString("id")+ " : "+ array.getJSONObject(i).getString("label") + "\n";
            }
            return responseLine;
        }
    }

    public static void main(String[] args) throws IOException , JSONException{
        String key = "YOUR_KEY";
        String image_url = "https://www.example.com/example_1.jpg,https://www.example.com/example_2.jpg";

        String req_id = RequestEndpoint(image_url , key , 0);
        System.out.println(req_id);

        int percentage = 0;
        while (percentage != 100)
        {
            percentage = PercentageEndpoint(req_id);
            System.out.println(percentage);
        }
        var result = OutputEndpoint(req_id);
        System.out.println(result);
    }
    
}
