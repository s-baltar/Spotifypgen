package com.spotifypgen;

/*
    This is the Song class
    Class stores a track's id, name and uri
 */
public class Song {
    private String id;
    private String uri;
    private String name;

    public Song(String id, String  name) {
        this.name = name;
        this.id = id;
    }

    // getter and setter method
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

    public void setName(String name) { this.name = name; }
}
