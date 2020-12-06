package com.spotifypgen;

public class Features {
    private double danceability;
    private double energy;
    private double loudness;
    private double acousticness;
    private double instrumentalness;
    private double valence;
    private double tempo;
    private int duration_ms;
    private String id;
    private String uri;

    public double getDanceability() { return danceability; }

    public double getEnergy() { return energy; }

    public double getLoudness() { return loudness; }

    public double getAcousticness() { return acousticness; }

    public double getInstrumentalness() { return instrumentalness; }

    public double getValence() { return valence; }

    public double getTempo() { return tempo; }

    public int getDuration_ms() { return duration_ms; }

    public String getId() {
        return id;
    }

    public String getURI() {
        return uri;
    }

    public void setDanceability(double d) { danceability = d; }

    public void setEnergy(double d) { energy = d; }

    public void setLoudness(double d) { loudness = d; }

    public void setAcousticness(double d) { acousticness = d; }

    public void setInstrumentalness(double d) { instrumentalness = d; }

    public void setValence(double d) { valence = d; }

    public void setTempo(double d) { tempo = d; }

    public void setDuration_ms(int i) { duration_ms = i; }

    public void setId(String s) { id = s; }

    public void setURI(String s) {  uri = s; }

}
