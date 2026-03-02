package com.arep.lab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class HttpResponseTest {

    @Test
    void testDefaultValues() {
        HttpResponse response = new HttpResponse();
        assertEquals("", response.getBody());
        assertEquals("text/html", response.getContentType());
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void testSetBody() {
        HttpResponse response = new HttpResponse();
        String body = "Hello World";
        response.setBody(body);
        assertEquals(body, response.getBody());
    }

    @Test
    void testSetContentType() {
        HttpResponse response = new HttpResponse();
        response.setContentType("application/json");
        assertEquals("application/json", response.getContentType());
    }

    @Test
    void testSetStatusCode() {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(404);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void testSetMultipleProperties() {
        HttpResponse response = new HttpResponse();
        response.setBody("Not Found");
        response.setContentType("text/plain");
        response.setStatusCode(404);
        
        assertEquals("Not Found", response.getBody());
        assertEquals("text/plain", response.getContentType());
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void testEmptyBody() {
        HttpResponse response = new HttpResponse();
        response.setBody("");
        assertEquals("", response.getBody());
    }
}
