package com.spotifypgen;

public class Artist {
    private String id;
    private String uri;
    private String name;

    public Artist(String id, String  name) {
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getURI() {
        return this.uri;
    }

    public void setName(String name) {
        this.name = name;
    }

}
