package com.spotifypgen;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

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

    private final HashMap<Feat, Double> featurePref = new HashMap<Feat, Double>();

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
            featurePref.put(audioFeature, (value * 0.6) - 60);
        } else // Scale vale down by 100.
            featurePref.put( audioFeature, value / 100 );
    }

    //  Info: Get desired user preferred audio feature.
    // Param: Feat - e.g. Feat.ENERGY
    public double getFeaturePreferences(Feat audioFeature) {
        return featurePref.get(audioFeature);
    }


    //  Info: Filters out songs that exceed a certain 'distance'.
    //        Distributes songs into bucket levels based on the songs tempo.
    //        Randomly picks songs from each bucket without exceeding user's desired playlist
    //        duration.
    //        Minimum amount of songs in the generated playlist is four. One for each 'bucket'.
    //        List of songs increase and then decrease in tempo.
    // Param: f - list of songs and their features to be sorted.
    //   Ret: ArrayList of song IDs and features in sorted order to be added to playlist.
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ArrayList<Features> sortFeatures(ArrayList<Features> features) {

        ArrayList<Features> bucket;
        ArrayList<Features> bucket_high = new ArrayList<Features>();        // High tempo songs
        ArrayList<Features> bucket_moderate = new ArrayList<Features>();    // Moderate tempo songs
        ArrayList<Features> bucket_low = new ArrayList<Features>();         // Low tempo songs
        ArrayList<Features> gen_playlist = new ArrayList<Features>();       // Final list of sorted songs.

        bucket = features;

        Collections.sort(bucket, new Sortbytempo()); // Sort bucket by tempo.

        // Distribute songs into buckets.
        int tenthOfBucket = (int) (bucket.size()/10);
        for (int i=0; i<tenthOfBucket*2 ; i++)  // Add top 20% songs to bucket.
            bucket_high.add(bucket.get(i));

        for (int i=tenthOfBucket*2; i< tenthOfBucket*4; i++) // Add top 20%-40% songs to bucket.
            bucket_moderate.add(bucket.get(i));

        for (int i=tenthOfBucket*4; i< bucket.size(); i++) // Add bottom 60% songs to bucket.
            bucket_low.add(bucket.get(i));

        int duration = (int)(featurePref.get(Feat.LENGTH) * (60 * 1000)); // min to ms


        int randomNum = 0;
        int i = bucket_moderate.get(randomNum).getDuration_ms();
        do { // Add moderate tempo songs to playlist

            gen_playlist.add(bucket_moderate.get(randomNum)); // Add song to final sorted list.

            bucket_moderate.remove(randomNum);  // Remove, so no repeated songs.

            if (bucket_moderate.isEmpty())
                break;

            // Generate randomNum for index of bucket.
            randomNum = (bucket_moderate.size() > 1)
                    ? ThreadLocalRandom.current().nextInt(0, bucket_moderate.size() - 1)
                    : 0;

            i += bucket_moderate.get(randomNum).getDuration_ms(); // Increment current playlist length.
        } while (i<=duration/5); // While current length of playlist <= fifth of desired duration


        randomNum = 0;
        i = bucket_high.get(randomNum).getDuration_ms();
        do { // Add high tempo songs to playlist
            gen_playlist.add(bucket_high.get(randomNum));

            bucket_high.remove(randomNum);

            if (bucket_high.isEmpty())
                break;

            randomNum = (bucket_high.size() > 1)
                    ? ThreadLocalRandom.current().nextInt(0, bucket_high.size() - 1)
                    : 0;
            i += bucket_high.get(randomNum).getDuration_ms();
        } while (i<=duration/2); // While current bucket duration <= half of desired duration


        randomNum = 0;
        i = (!bucket_moderate.isEmpty()) ? bucket_moderate.get(randomNum).getDuration_ms() : -1;
        do { // Add moderate tempo songs to playlist
            if (bucket_moderate.isEmpty())
                break;

            gen_playlist.add(bucket_moderate.get(randomNum));

            bucket_moderate.remove(randomNum);

            if (bucket_moderate.isEmpty())
                break;

            randomNum = (bucket_moderate.size() > 1)
                    ? ThreadLocalRandom.current().nextInt(0, bucket_moderate.size() - 1)
                    : 0;

            i += bucket_moderate.get(randomNum).getDuration_ms();
        } while (i<=duration/5);


        randomNum = 0;
        i = bucket_low.get(randomNum).getDuration_ms();
        do { // Add low tempo songs to playlist
            gen_playlist.add(bucket_low.get(randomNum));

            bucket_low.remove(randomNum);

            if (bucket_low.isEmpty())
                break;

            randomNum = (bucket_low.size() > 1)
                    ? ThreadLocalRandom.current().nextInt(0, bucket_low.size() - 1)
                    : 0;

            i += bucket_low.get(randomNum).getDuration_ms();
        } while (i<=duration/10); // While current bucket duration <= tenth of desired duration

        return gen_playlist;
    }

}
