package utils;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {

    // Constants for HTTP Success Codes
    private static final int SUCCESS_CODE = 200;    // HTTP 200 OK
    private static final int ACCEPTED_CODE = 202;    // HTTP 202 Accepted

    /**
     * Sends an HTTP GET request to the provided server URL.
     *
     * @param serverUrl The URL to which the GET request will be sent.
     * @return A JSONObject representing the response from the server.
     * @throws IOException If there is a problem with the network or reading the response.
     * @throws ParseException If the response cannot be parsed into JSON.
     */
    public static JSONObject get(String serverUrl) throws IOException, ParseException {
        // Prepare the connection and send GET request
        HttpURLConnection connection = prepareConnection(serverUrl, "GET", null);
        // Handle and return the response from the server
        return handleResponse(connection);
    }

    /**
     * Sends an HTTP POST request with a payload to the provided server URL.
     *
     * @param serverUrl The URL to which the POST request will be sent.
     * @param payload   The JSON payload to be sent in the request body.
     * @return A JSONObject representing the response from the server.
     * @throws IOException If there is a problem with the network or reading the response.
     * @throws ParseException If the response cannot be parsed into JSON.
     */
    public static JSONObject post(String serverUrl, String payload) throws IOException, ParseException {
        // Prepare the connection and send POST request with payload
        HttpURLConnection connection = prepareConnection(serverUrl, "POST", payload);
        // Handle and return the response from the server
        return handleResponse(connection);
    }

    /**
     * Sends an HTTP PUT request with a payload to the provided server URL.
     *
     * @param serverUrl The URL to which the PUT request will be sent.
     * @param payload   The JSON payload to be sent in the request body.
     * @return A JSONObject representing the response from the server.
     * @throws IOException If there is a problem with the network or reading the response.
     * @throws ParseException If the response cannot be parsed into JSON.
     */
    public static JSONObject put(String serverUrl, String payload) throws IOException, ParseException {
        // Prepare the connection and send PUT request with payload
        HttpURLConnection connection = prepareConnection(serverUrl, "PUT", payload);
        // Handle and return the response from the server
        return handleResponse(connection);
    }

    /**
     * Sends an HTTP DELETE request to the provided server URL.
     *
     * @param serverUrl The URL to which the DELETE request will be sent.
     * @return A JSONObject representing the response from the server.
     * @throws IOException If there is a problem with the network or reading the response.
     * @throws ParseException If the response cannot be parsed into JSON.
     */
    public static JSONObject delete(String serverUrl) throws IOException, ParseException {
        // Prepare the connection and send DELETE request
        HttpURLConnection connection = prepareConnection(serverUrl, "DELETE", null);
        // Handle and return the response from the server
        return handleResponse(connection);
    }

    /**
     * Prepares the connection to the server by setting the necessary HTTP method and headers.
     *
     * @param serverUrl The server URL to connect to.
     * @param method    The HTTP method (GET, POST, PUT, DELETE).
     * @param payload   The payload to be sent with the request (used for POST and PUT).
     * @return A HttpURLConnection object representing the connection.
     * @throws IOException If there is a problem opening or configuring the connection.
     */
    private static HttpURLConnection prepareConnection(String serverUrl, String method, String payload) throws IOException {
        // Create a URL object from the provided server URL
        URL url = new URL(serverUrl);
        // Open the connection to the server
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // Set the HTTP method (GET, POST, PUT, DELETE)
        connection.setRequestMethod(method);
        // Set the Content-Type header to JSON
        connection.setRequestProperty("Content-Type", "application/json");

        // If the method is POST or PUT, send the payload in the request body
        if ("POST".equals(method) || "PUT".equals(method)) {
            connection.setDoOutput(true);  // Enable output for sending data
            try (OutputStream os = connection.getOutputStream()) {
                // Write the payload as bytes to the output stream
                os.write(payload.getBytes());
                os.flush();
            }
        }

        // Return the prepared connection
        return connection;
    }

    /**
     * Handles the server response, checks for success, and parses the response body.
     *
     * @param connection The HttpURLConnection object representing the connection.
     * @return A JSONObject containing the parsed response data.
     * @throws IOException If there is an issue with reading the response.
     * @throws ParseException If the response cannot be parsed into JSON.
     */
    private static JSONObject handleResponse(HttpURLConnection connection) throws IOException, ParseException {
        // Get the response code from the server
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        // Check if the response code is success (200 OK) or accepted (202)
        if (responseCode != SUCCESS_CODE && responseCode != ACCEPTED_CODE) {
            // If not, throw an exception with the response code
            throw new RuntimeException("Server response failed with code: " + responseCode);
        }

        // Read and parse the response body from the server
        String jsonResponse = readResponse(connection);
        return parseJson(jsonResponse);  // Parse the JSON response and return it
    }

    /**
     * Reads the response body from the server connection.
     *
     * @param connection The HttpURLConnection object representing the connection.
     * @return A String containing the response body.
     * @throws IOException If there is an issue with reading the response.
     */
    private static String readResponse(HttpURLConnection connection) throws IOException {
        // Create a BufferedReader to read the response
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        // Read each line of the response and append it to the StringBuilder
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();  // Close the input stream
        return response.toString();  // Return the full response body as a string
    }

    /**
     * Parses the given JSON response string into a JSONObject.
     *
     * @param response The JSON string to be parsed.
     * @return A JSONObject representing the parsed response.
     * @throws ParseException If the response cannot be parsed into JSON.
     */
    private static JSONObject parseJson(String response) throws ParseException {
        // Handle empty or null input
        if (response == null || response.trim().isEmpty()) {
            return new JSONObject();  // Return an empty JSONObject if the response is empty
        }

        // Use JSONParser to parse the response string
        JSONParser parser = new JSONParser();
        Object parsedObject = parser.parse(response);

        // Check if the parsed object is a JSONArray
        if (parsedObject instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) parsedObject;
            if (jsonArray.isEmpty()) {
                return new JSONObject();  // Return an empty object if the array is empty
            }
            return (JSONObject) jsonArray.get(jsonArray.size() - 1);  // Return the last item of the array
        } 
        // If the parsed object is a JSONObject, return it directly
        else if (parsedObject instanceof JSONObject) {
            return (JSONObject) parsedObject;
        } 
        // If neither, throw a JSONException
        else {
            throw new JSONException("Unable to parse the response body");
        }
    }
}
