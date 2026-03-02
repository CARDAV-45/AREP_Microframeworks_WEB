package com.arep.lab;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String path;
    private String queryString;
    private Map<String, String> queryParams;
    
    public HttpRequest(String path, String queryString) {
        this.path = path;
        this.queryString = queryString;
        this.queryParams = new HashMap<>();
        parseQueryParams();
    }
    
    private void parseQueryParams() {
        if (queryString != null && !queryString.isEmpty()) {
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                } else if (keyValue.length == 1) {
                    queryParams.put(keyValue[0], "");
                }
            }
        }
    }
    
    public String getValues(String paramName) {
        return queryParams.getOrDefault(paramName, "");
    }
    
    public String getPath() {
        return path;
    }
    
    public String getQueryString() {
        return queryString;
    }
}
