package com.example.kapyshintestapp;

public class WebSite {
    private String name;
    private String url;

    public WebSite(String Name, String Url){
        name = Name;
        url = Url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
