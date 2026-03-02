package com.arep.lab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class EndpointTest {

    @Test
    void testHelloEndpointWithParameter() {
        HttpRequest request = new HttpRequest("/App/hello", "name=Pedro");
        HttpResponse response = new HttpResponse();
        
        WebMethod helloEndpoint = (req, resp) -> "Hello " + req.getValues("name");
        String result = helloEndpoint.execute(request, response);
        
        assertEquals("Hello Pedro", result);
    }

    @Test
    void testHelloEndpointWithDifferentName() {
        HttpRequest request = new HttpRequest("/hello", "name=Maria");
        HttpResponse response = new HttpResponse();
        
        WebMethod helloEndpoint = (req, resp) -> "Hello " + req.getValues("name");
        String result = helloEndpoint.execute(request, response);
        
        assertEquals("Hello Maria", result);
    }

    @Test
    void testPiEndpoint() {
        HttpRequest request = new HttpRequest("/App/pi", null);
        HttpResponse response = new HttpResponse();
        
        WebMethod piEndpoint = (req, resp) -> String.valueOf(Math.PI);
        String result = piEndpoint.execute(request, response);
        
        assertEquals(String.valueOf(Math.PI), result);
    }

    @Test
    void testHelloEndpointWithEmptyName() {
        HttpRequest request = new HttpRequest("/hello", "");
        HttpResponse response = new HttpResponse();
        
        WebMethod helloEndpoint = (req, resp) -> "Hello " + req.getValues("name");
        String result = helloEndpoint.execute(request, response);
        
        assertEquals("Hello ", result);
    }

    @Test
    void testMultipleParameters() {
        HttpRequest request = new HttpRequest("/api", "name=John&greeting=Good%20Morning");
        HttpResponse response = new HttpResponse();
        
        WebMethod customEndpoint = (req, resp) -> {
            String name = req.getValues("name");
            String greeting = req.getValues("greeting");
            return greeting + " " + name;
        };
        
        String result = customEndpoint.execute(request, response);
        assertEquals("Good%20Morning John", result);
    }
}
