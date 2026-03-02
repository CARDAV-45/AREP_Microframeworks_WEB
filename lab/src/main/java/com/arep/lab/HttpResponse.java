package com.arep.lab;

public class HttpResponse {
    private String body;
    private String contentType;
    private int statusCode;
    
    public HttpResponse() {
        this.body = "";
        this.contentType = "text/html";
        this.statusCode = 200;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
}
