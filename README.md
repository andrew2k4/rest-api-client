# rest-api-client Utility


The `ApiClient` utility is a simple, reusable HTTP client designed to send GET, POST, PUT, and DELETE requests to APIs, handle responses, and parse JSON data. This client is modular, allowing it to be used in any Java-based project where communication with RESTful APIs is required.

## Features
- Send HTTP GET, POST, PUT, and DELETE requests.
- Support for custom headers (e.g., authentication tokens).
- Handles response parsing with error handling.
- UTF-8 encoding for POST and PUT requests.
- Graceful handling of empty or null JSON responses.

## Usage

To use this client in your project, you can call the `get()`, `post()`, `put()`, or `delete()` methods to interact with an API. Here's how to use the client for different requests:

### 1. Sending a GET Request

```java
import utils.ApiClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import java.io.IOException;

public class ApiExample {
    public static void main(String[] args) {
        String url = "https://api.example.com/data";
        try {
            JSONObject response = ApiClient.get(url);
            System.out.println(response);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
```
### 2. Sending a Post Request

```java
import utils.ApiClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import java.io.IOException;

public class ApiExample {
    public static void main(String[] args) {
        String url = "https://api.example.com/submit";
        String payload = "{\"key\":\"value\"}";  // Your JSON payload

        try {
            JSONObject response = ApiClient.post(url, payload);
            System.out.println(response);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
```

### 3. Sending a Put Request

```java
import utils.ApiClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import java.io.IOException;

public class ApiExample {
    public static void main(String[] args) {
        String url = "https://api.example.com/update";
        String payload = "{\"key\":\"updated_value\"}";  // Your JSON payload

        try {
            JSONObject response = ApiClient.put(url, payload);
            System.out.println(response);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
```


### 3. Sending a Delete Request
```java
import utils.ApiClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import java.io.IOException;

public class ApiExample {
    public static void main(String[] args) {
        String url = "https://api.example.com/delete/1";  // ID of the resource to delete

        try {
            JSONObject response = ApiClient.delete(url);
            System.out.println(response);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
```

