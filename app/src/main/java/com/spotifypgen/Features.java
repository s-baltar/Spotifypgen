package com.spotifypgen;

public class Features {
    private double danceability;
    private double energy;
    private double loudness;
    private double speechiness;
    private double acousticness;
    private double instrumentalness;
    private double liveness;
    private double valence;
    private double tempo;
    private double duration_ms;
    private String id;

    private double distance;

    public double getDanceability() { return danceability; }

    public double getEnergy() { return energy; }

    public double getLoudness() { return loudness; }

    public double getSpeechiness() { return speechiness; }

    public double getAcousticness() { return acousticness; }

    public double getInstrumentalness() { return instrumentalness; }

    public double getLiveness() { return liveness; }

    public double getValence() { return valence; }

    public double getTempo() { return tempo; }

    public double getDuration_ms() { return duration_ms; }

    public String getId() {
        return id;
    }

    public double getDistance() { return distance; }

    public void setDistance(double d) { distance = d; }
}
