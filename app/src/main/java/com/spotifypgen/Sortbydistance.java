package com.spotifypgen;

import java.util.Comparator;

public class Sortbydistance implements Comparator<Features> {
    @Override
    public int compare(Features o1, Features o2) {
        return (o1.getDistance() >= o2.getDistance()) ? 1 : -1;
    }
}
