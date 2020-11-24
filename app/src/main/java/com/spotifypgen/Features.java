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
    private String id;

    public double getDanceability() { return danceability; }

    public double getEnergy() { return energy; }

    public double getLoudness() { return loudness; }

    public double getSpeechiness() { return speechiness; }

    public double getAcousticness() { return acousticness; }

    public double getInstrumentalness() { return instrumentalness; }

    public double getLiveness() { return liveness; }

    public double getValence() { return valence; }

    public double getTempo() { return tempo; }

    public String getId() {
        return id;
    }
}
