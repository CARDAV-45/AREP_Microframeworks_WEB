package com.arep.lab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class HttpRequestTest {

    @Test
    void testGetValuesWithValidParameter() {
        HttpRequest request = new HttpRequest("/hello", "name=Pedro");
        assertEquals("Pedro", request.getValues("name"));
    }

    @Test
    void testGetValuesWithMultipleParameters() {
        HttpRequest request = new HttpRequest("/api", "name=Maria&age=25&city=Bogota");
        assertEquals("Maria", request.getValues("name"));
        assertEquals("25", request.getValues("age"));
        assertEquals("Bogota", request.getValues("city"));
    }

    @Test
    void testGetValuesWithNonexistentParameter() {
        HttpRequest request = new HttpRequest("/hello", "name=Pedro");
        assertEquals("", request.getValues("nonexistent"));
    }

    @Test
    void testGetValuesWithEmptyQueryString() {
        HttpRequest request = new HttpRequest("/hello", "");
        assertEquals("", request.getValues("name"));
    }

    @Test
    void testGetValuesWithNullQueryString() {
        HttpRequest request = new HttpRequest("/hello", null);
        assertEquals("", request.getValues("name"));
    }

    @Test
    void testGetPath() {
        HttpRequest request = new HttpRequest("/App/hello", "name=Pedro");
        assertEquals("/App/hello", request.getPath());
    }

    @Test
    void testGetQueryString() {
        String queryString = "name=Pedro&age=30";
        HttpRequest request = new HttpRequest("/hello", queryString);
        assertEquals(queryString, request.getQueryString());
    }
}
