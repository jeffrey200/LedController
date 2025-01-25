package at.edu.c02.ledcontroller;

import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class should handle all HTTP communication with the server.
 * Each method here should correspond to an API call, accept the correct parameters and return the response.
 * Do not implement any other logic here - the ApiService will be mocked to unit test the logic without needing a server.
 */
public class ApiServiceImpl implements ApiService {

    private String secret;

    public ApiServiceImpl() throws IOException {
        try {
            secret = Files.lines(Paths.get("secret.txt"))
                    .findFirst()
                    .orElse("");
        } catch (IOException e) {
            secret = System.getenv("SECRETKEY");
        }
    }

    /**
     * This method calls the `GET /getLights` endpoint and returns the response.
     * TODO: When adding additional API calls, refactor this method. Extract/Create at least one private method that
     * handles the API call + JSON conversion (so that you do not have duplicate code across multiple API calls)
     *
     * @return `getLights` response JSON object
     * @throws IOException Throws if the request could not be completed successfully
     */
    @Override
    public JSONObject getLights() throws IOException
    {
        return apiCall("https://balanced-civet-91.hasura.app/api/rest/getLights");
    }

    @Override
    public JSONObject getLight(Integer id) throws IOException {
        return apiCall("https://balanced-civet-91.hasura.app/api/rest/lights/" + id);
    }
    @Override
    public JSONObject setLight(Integer id, Boolean state) throws IOException {
        String color = getLight(id).getJSONArray("lights").getJSONObject(0).getString("color");
        return apiCallPut("https://balanced-civet-91.hasura.app/api/rest/setLight", "{\"id\":" + id.toString() +",\"color\":\"" + color + "\",\"state\":" + state.toString() + "}");
    }

    @Override
    public JSONObject setLight(Integer id, Boolean state, String color) throws IOException {
        return apiCallPut("https://balanced-civet-91.hasura.app/api/rest/setLight", "{\"id\":" + id.toString() +",\"color\":\"" + color + "\",\"state\":" + state.toString() + "}");
    }

    private JSONObject apiCallPut(String urlString, String body) throws IOException {
        // Connect to the server
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // and send a GET request
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("X-Hasura-Group-ID", secret);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = body.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Read the response code
        int responseCode = connection.getResponseCode();
        if(responseCode != HttpURLConnection.HTTP_OK) {
            // Something went wrong with the request
            throw new IOException("Error: setLights request failed with response code " + responseCode);
        }

        // The request was successful, read the response
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        // Save the response in this StringBuilder
        StringBuilder sb = new StringBuilder();

        int character;
        // Read the response, character by character. The response ends when we read -1.
        while((character = reader.read()) != -1) {
            sb.append((char) character);
        }

        String jsonText = sb.toString();
        // Convert response into a json object
        return new JSONObject(jsonText);
    }

    private JSONObject apiCall(String urlString) throws IOException {
        // Connect to the server
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // and send a GET request
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Hasura-Group-ID", secret);
        // Read the response code
        int responseCode = connection.getResponseCode();
        if(responseCode != HttpURLConnection.HTTP_OK) {
            // Something went wrong with the request
            throw new IOException("Error: getLights request failed with response code " + responseCode);
        }

        // The request was successful, read the response
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        // Save the response in this StringBuilder
        StringBuilder sb = new StringBuilder();

        int character;
        // Read the response, character by character. The response ends when we read -1.
        while((character = reader.read()) != -1) {
            sb.append((char) character);
        }

        String jsonText = sb.toString();
        // Convert response into a json object
        return new JSONObject(jsonText);
    }
}
