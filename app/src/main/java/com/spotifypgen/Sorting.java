package com.spotifypgen;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;


public class Sorting {

    enum Feat {
        DANCEABILITY,
        ENERGY,
        LOUDNESS,
        ACCOUSTICNESS,
        INSTRUMENTALNESS,
        VALENCE,
        LENGTH
    }

    private HashMap<Feat, Double> featurePref = new HashMap<Feat, Double>();
    private ArrayList<Features> features = new ArrayList<>();

    public Sorting() {
        featurePref.put(Feat.DANCEABILITY, 0.5);
        featurePref.put(Feat.ENERGY, 0.5);
        featurePref.put(Feat.LOUDNESS, 0.5);
        featurePref.put(Feat.ACCOUSTICNESS, 0.5);
        featurePref.put(Feat.INSTRUMENTALNESS, 0.5);
        featurePref.put(Feat.VALENCE, 0.5);
        featurePref.put(Feat.LENGTH, 60.0);
    }

    // Set desired user preferred audio feature.
    // Param: Feat  - e.g. Feat.ENERGY
    //        value - Value of audio feature.
    public void setFeaturePreferences(Feat audioFeature, double value) {
        featurePref.put(audioFeature, value);
    }

    //  Info: Get desired user preferred audio feature.
    // Param: Feat - e.g. Feat.ENERGY
    public double getFeaturePreferences(Feat audioFeature) {
        return featurePref.get(audioFeature);
    }

    //  Info: Set ArrayList of features that need to be sorted.
    public void setFeatures(ArrayList<Features> f) {
        features = f;
    }

    //  Info: Calculate distance between user preferred features and given features of a song.
    //   Ret: Double value of distance.
    private double calcDistance(Features point) {
        double sqrtArg = pow(featurePref.get(Feat.DANCEABILITY)     - point.getDanceability(), 2) +
                         pow(featurePref.get(Feat.ENERGY)           - point.getDanceability(), 2) +
                         pow(featurePref.get(Feat.LOUDNESS)         - point.getLoudness(), 2) +
                         pow(featurePref.get(Feat.ACCOUSTICNESS)    - point.getAcousticness(), 2) +
                         pow(featurePref.get(Feat.INSTRUMENTALNESS) - point.getInstrumentalness(), 2) +
                         pow(featurePref.get(Feat.VALENCE)          - point.getValence(), 2);

        return sqrt(sqrtArg);
    }

    //  Info: Sort distances in ascending.
    // Param: preferences - User's desired audio features.
    //   Ret: ArrayList<Features> - features that are closest to preferences.
    public void sortDistance() {
        Log.d("SB", "sortDistance()"); // SB: tmp
        if (features == null) {
            Log.d("SB", "features is null");
            return;
        } else if (features.size() <= 0) { // SB: tmp
            Log.d("SB", "features is empty");
            return;
        } else { // SB: tmp
            Log.d("SB", "not null");
            Log.d("SB", features.get(0).getId());
        }

        // Calculate all song 'distances'
        for (int i = 0; i < features.size(); i++)
            features.get(i).setDistance(calcDistance(features.get(i)));

        // Sort in ascending order.
        Collections.sort(features, new Sortbydistance());
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ArrayList<Features> sortBuckets() {
        ArrayList<Features> bucket = new ArrayList<Features>();
        ArrayList<Features> bucket_high = new ArrayList<Features>();
        ArrayList<Features> bucket_moderate = new ArrayList<Features>();
        ArrayList<Features> bucket_low = new ArrayList<Features>();
        ArrayList<Features> gen_playlist = new ArrayList<Features>();

        // Filter out songs
        for (int i=0; i<features.size(); i++) {
            if (features.get(i).getDistance() > 4) { // SB: Play with this number
                break;
            }
            bucket.add(features.get(i));
        }

        if (bucket.size() <= 0) { // SB: tmp
            Log.d("SB", "bucket is empty");
            return null;
        }

        Collections.sort(bucket, new Sortbytempo());
        int tenthOfBucket = (int) (bucket.size()/10);

        for (int i=0; i<tenthOfBucket ; i++)
            bucket_high.add(bucket.get(i));

        for (int i=tenthOfBucket; i< tenthOfBucket*3; i++)
            bucket_moderate.add(bucket.get(i));

        for (int i=tenthOfBucket*3; i< tenthOfBucket*4; i++)
            bucket_low.add(bucket.get(i));

        double duration = featurePref.get(Feat.LENGTH) * (60 * 1000);


        int randomNum = 0;

        for (int i=0; i<=duration/10*2; i+=bucket_moderate.get(randomNum).getDuration_ms()) {
            randomNum = ThreadLocalRandom.current().nextInt(0, bucket_moderate.size()-1);
            i += bucket_moderate.get(randomNum).getDuration_ms();
            gen_playlist.add(bucket_moderate.get(randomNum));
        }

        for (int i=0; i<=duration/10*5; i+=bucket_high.get(randomNum).getDuration_ms()) {
            randomNum = ThreadLocalRandom.current().nextInt(0, bucket_high.size()-1);
            i += bucket_high.get(randomNum).getDuration_ms();
            gen_playlist.add(bucket_high.get(randomNum));
        }

        for (int i=0; i<=duration/10*2; i+=bucket_moderate.get(randomNum).getDuration_ms()) {
            randomNum = ThreadLocalRandom.current().nextInt(0, bucket_moderate.size()-1);
            i += bucket_moderate.get(randomNum).getDuration_ms();
            gen_playlist.add(bucket_moderate.get(randomNum));
        }

        for (int i=0; i<=duration/10; i+=bucket_low.get(randomNum).getDuration_ms()) {
            randomNum = ThreadLocalRandom.current().nextInt(0, bucket_low.size()-1);
            i += bucket_low.get(randomNum).getDuration_ms();
            gen_playlist.add(bucket_low.get(randomNum));
        }

        // SB: tmp
        for (int i=0; i<gen_playlist.size(); i++) {
            Log.d("SB", gen_playlist.get(i).getId());
        }

        return gen_playlist;
    }

}
