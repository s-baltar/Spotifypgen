package com.spotifypgen;

public class Song {
    private String id;
    private String uri;
    private String name;

    private double duration_ms;
    private double acousticness;
    private double danceability;
    private double energy;
    private double instrumentalness;
    private double liveness;
    private double loudness;
    private double speechiness;
    private double valence;
    private double tempo;


    public Song(String id, String  name) {
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

    public double getDuration() { return this.duration_ms;}

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(double d) {this.duration_ms = d;}

    public void setAcousticness(double d) {this.acousticness = d;}

    public void setDanceability(double d) {this.danceability = d;}

}
