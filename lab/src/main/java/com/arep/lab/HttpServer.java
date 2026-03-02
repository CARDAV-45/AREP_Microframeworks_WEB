package com.arep.lab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    private static Map<String, WebMethod> endPoints = new HashMap<>();
    private static String staticFilesLocation = "/webroot/public";
    private static int port = 8080;
    
    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
            System.exit(1);
        }
        
        System.out.println("Server started on port " + port);
        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Ready to receive ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            
            String inputLine;
            boolean isFirstLine = true;
            String reqPath = "";
            String reqQuery = "";

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);

                if (isFirstLine) {
                    String[] flTokens = inputLine.split(" ");
                    if (flTokens.length >= 2) {
                        String method = flTokens[0];
                        String struriPath = flTokens[1];

                        URI uriPath = new URI(struriPath);
                        reqPath = uriPath.getPath();
                        reqQuery = uriPath.getQuery();

                        System.out.println("Path: " + reqPath);
                        System.out.println("Query: " + reqQuery);
                    }
                    isFirstLine = false;
                }
                
                if (!in.ready()) {
                    break;
                }
            }
            
            String outputLine = handleRequest(reqPath, reqQuery);
            out.println(outputLine);
            
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }
    
    private static String handleRequest(String path, String query) {
        WebMethod endpoint = endPoints.get(path);
        
        if (endpoint != null) {
            HttpRequest request = new HttpRequest(path, query);
            HttpResponse response = new HttpResponse();
            
            String result = endpoint.execute(request, response);
            
            return buildHttpResponse(200, response.getContentType(), result);
        }
        
        String staticContent = tryServeStaticFile(path);
        if (staticContent != null) {
            return staticContent;
        }
        
        return buildHttpResponse(404, "text/html", 
            "<html><body><h1>404 Not Found</h1><p>The requested resource was not found.</p></body></html>");
    }
    
    private static String tryServeStaticFile(String path) {
        try {
            String resourcePath = staticFilesLocation + path;
            InputStream inputStream = HttpServer.class.getResourceAsStream(resourcePath);
            
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                reader.close();
                
                String contentType = getContentType(path);
                return buildHttpResponse(200, contentType, content.toString());
            }
        } catch (IOException e) {
            System.err.println("Error serving static file: " + e.getMessage());
        }
        
        return null;
    }
    
    private static String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".gif")) return "image/gif";
        return "text/plain";
    }
    
    private static String buildHttpResponse(int statusCode, String contentType, String body) {
        String statusText = "OK";
        if (statusCode == 404) statusText = "Not Found";
        
        return "HTTP/1.1 " + statusCode + " " + statusText + "\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "\r\n"
                + body;
    }
    
    public static void get(String path, WebMethod wm) {
        endPoints.put(path, wm);
    }
    
    public static void staticfiles(String location) {
        staticFilesLocation = location;
    }
    
    public static void setPort(int newPort) {
        port = newPort;
    }
}
