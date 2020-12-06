package com.spotifypgen;

/*
    This is the Playlist class
    Class stores a playlist's id and name
 */
public class Playlist {
    private String id;
    private String name;

    // constructor
    public void playlist(String id, String  name) {
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

    public void setName(String name) {
        this.name = name;
    }

}
