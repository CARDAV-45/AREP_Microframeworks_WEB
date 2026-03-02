package com.arep.lab.appexamples;

import java.io.IOException;
import java.net.URISyntaxException;

import com.arep.lab.HttpServer;
import static com.arep.lab.HttpServer.get;
import static com.arep.lab.HttpServer.setPort;
import static com.arep.lab.HttpServer.staticfiles;

public class MathService {

    public static void main(String[] args) throws IOException, URISyntaxException {
        setPort(8081);
        staticfiles("/webroot/public");
        
        get("/App/hello", (req, resp) -> "Hello " + req.getValues("name"));
        get("/App/pi", (req, resp) -> {
            return String.valueOf(Math.PI);
        });
        
        get("/hello", (req, resp) -> "Hello " + req.getValues("name"));
        get("/pi", (req, resp) -> String.valueOf(Math.PI));
        
        HttpServer.main(args);
    }
}
