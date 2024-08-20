import java.io.*;
import java.net.*;

public class Main {

    public static void main(String[] args) {
        try {
            // Create a server socket that listens on port 3020
            ServerSocket serverSocket = new ServerSocket(3020);
            System.out.println("Server is listening on port 3020");

            while (true) {
                // Wait for a client to connect
                Socket clientSocket = serverSocket.accept();
                
                // Handle the client request in a new thread
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            // Read the request from the client
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read the first line of the request (the request line)
            String requestLine = in.readLine();
            System.out.println("Request: " + requestLine);

            // Check if the request is a GET request for the /get-user endpoint
            if (requestLine != null && requestLine.startsWith("GET /get-user?id=")) {
                // Extract the user ID from the request
                String[] parts = requestLine.split("\\s+");
                String query = parts[1];
                String[] queryParts = query.split("=");
                String userId = queryParts[1];

                // Create a mock JSON response
                // String jsonResponse = "{ \"id\": " + userId + ", \"name\": \"John Doe\" }";
                String jsonResponse = new JsonHelper()
                         .add("id", Integer.parseInt(userId))
                         .add("name", "D3XT3R")
                         .build();

                // Send the HTTP response
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: application/json");
                out.println("Content-Length: " + jsonResponse.length());
                out.println();
                out.println(jsonResponse);
            } else {
                // Handle other cases (e.g., 404 Not Found)
                
                // Create JSON response for 404 Not Found
                String jsonResponse = new JsonHelper()
                         .add("code", 404)
                         .add("status", false)
                         .add("msg", "404 Not Found")
                         .build();

                out.println("HTTP/1.1 404 Not Found");
                out.println("Content-Type: application/json");
                out.println("Content-Length: " + jsonResponse.length());
                out.println();
                out.println(jsonResponse);
                // out.println("Content-Type: text/plain");
                // out.println();
                // out.println("404 Not Found");
            }

            // Close the client connection
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
