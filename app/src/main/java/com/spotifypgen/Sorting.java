package com.spotifypgen;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

import static java.lang.Math.pow;
import static java.lang.Math.random;
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
    private boolean isSavedTracks = false;

    final double DISTANCE_CUTTOFF = 4;

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
        if (audioFeature == Feat.LENGTH)
            featurePref.put( audioFeature, value );
        else if (audioFeature == Feat.LOUDNESS) {
            double db = value; // TODO: Scale from [0,100] to [-60,0] dB
            featurePref.put(audioFeature, db);
        } else
            featurePref.put( audioFeature, value / 100 );
    }

    // Get desired user preferred audio feature.
    public double setFeaturePreferences(Feat audioFeature) {
        return featurePref.get(audioFeature);
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

    //  Info: Sort distances in ascending. Only to be used for saved tracks.
    // Param: preferences - User's desired audio features.
    //   Ret: ArrayList<Features> - features that are closest to preferences.
    public void sortDistance() {

        // Calculate all song 'distances'
        for (int i = 0; i < features.size(); i++)
            features.get(i).setDistance(calcDistance(features.get(i)));

        // Sort in ascending order.
        Collections.sort(features, new Sortbydistance());
    }

    //  Info: Filters out songs that exceed a certain 'distance'.
    //        Distributes songs into bucket levels based on the songs tempo.
    //        Randomly picks songs from each bucket without exceeding user's desired playlist
    //        duration.
    //   Ret: ArrayList of song IDs and features in sorted order to be added to playlist.
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ArrayList<Features> sortBuckets() {

        ArrayList<Features> bucket = new ArrayList<Features>();
        ArrayList<Features> bucket_high = new ArrayList<Features>();
        ArrayList<Features> bucket_moderate = new ArrayList<Features>();
        ArrayList<Features> bucket_low = new ArrayList<Features>();
        ArrayList<Features> gen_playlist = new ArrayList<Features>();

        if (isSavedTracks) {
            // Filter out songs
            for (int i = 0; i < features.size(); i++) {
                if (features.get(i).getDistance() > DISTANCE_CUTTOFF)
                    break;
                bucket.add(features.get(i));
            }
        } else // Otherwise use given features
            bucket = features;

        Collections.sort(bucket, new Sortbytempo());
        int tenthOfBucket = (int) (bucket.size()/10);

        for (int i=0; i<tenthOfBucket ; i++)
            bucket_high.add(bucket.get(i));

        for (int i=tenthOfBucket; i< tenthOfBucket*3; i++)
            bucket_moderate.add(bucket.get(i));

        for (int i=tenthOfBucket*3; i< tenthOfBucket*5; i++)
            bucket_low.add(bucket.get(i));

        double duration = featurePref.get(Feat.LENGTH) * (60 * 1000);


        int randomNum = 0;

        for (int i=0; i<=duration/10*2 && bucket_moderate.size() > 0;) {

            randomNum = ( bucket_moderate.size() != 1 )
                    ? ThreadLocalRandom.current().nextInt(0, bucket_moderate.size()-1)
                    : 0;

            i += bucket_moderate.get(randomNum).getDuration_ms();
            gen_playlist.add(bucket_moderate.get(randomNum));

            bucket_moderate.remove(randomNum);
        }

        for (int i=0; i<=duration/10*5 && bucket_high.size() > 0;) {

            randomNum = ( bucket_high.size() != 1 )
                    ? ThreadLocalRandom.current().nextInt(0, bucket_high.size()-1)
                    : 0;

            i += bucket_high.get(randomNum).getDuration_ms();
            gen_playlist.add(bucket_high.get(randomNum));

            bucket_high.remove(randomNum);
        }

        for (int i=0; i<=duration/10*2 && bucket_moderate.size() > 0;) {

            randomNum = ( bucket_moderate.size() != 1 )
                    ? ThreadLocalRandom.current().nextInt(0, bucket_moderate.size()-1)
                    : 0;

            i += bucket_moderate.get(randomNum).getDuration_ms();
            gen_playlist.add(bucket_moderate.get(randomNum));

            bucket_moderate.remove(randomNum);
        }

        for (int i=0; i<=duration/10 && bucket_low.size() > 0;) {

            randomNum = ( bucket_low.size() != 1 )
                    ? ThreadLocalRandom.current().nextInt(0, bucket_low.size()-1)
                    : 0;

            i += bucket_low.get(randomNum).getDuration_ms();
            gen_playlist.add(bucket_low.get(randomNum));

            bucket_low.remove(randomNum);
        }

        return gen_playlist;
    }

    // Sorts songs based on audio features.
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ArrayList<Features> sortFeatures(ArrayList<Features> f) {
        if (isSavedTracks)
            sortDistance();
        setFeatures(f);
        return sortBuckets();
    }
}
