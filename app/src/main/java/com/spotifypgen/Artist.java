package com.spotifypgen;

/*
    This is the Artist class
    Class stores an artist's id, name and uri
 */
public class Artist {
    private String id;
    private String uri;
    private String name;

    // constructor
    public Artist(String id, String  name) {
        this.name = name;
        this.id = id;
    }

    // getter and setter methods
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

    public void setURI(String u) {
        this.uri = u;
    }

}
