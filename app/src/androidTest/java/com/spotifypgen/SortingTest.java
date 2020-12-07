package com.spotifypgen;

import android.util.Log;

import junit.framework.TestCase;

import java.util.ArrayList;

public class SortingTest extends TestCase {

    public void testSortFeatures() {
        Sorting sorter = new Sorting();
        ArrayList<Features> features = new ArrayList<>();
        ArrayList<Features> outFeatures;
        ArrayList<Double> inputFeatures = new ArrayList<>();

        sorter.setFeaturePreferences(Sorting.Feat.LENGTH, 1000);

        for (int i=0; i<100; i++) {
            Features f = new Features();
            f.setDanceability(0.5);
            f.setEnergy(0.5);
            f.setLoudness(-10);
            f.setAcousticness(0.5);
            f.setInstrumentalness(0.5);
            f.setValence(0.5);
            f.setTempo(100);
            f.setDuration_ms(180000);

            features.add(f);
        }

        assertNotNull( (sorter.sortFeatures(features)).get(0) );
    }
}