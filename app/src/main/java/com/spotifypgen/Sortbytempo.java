package com.spotifypgen;

import java.util.Comparator;

public class Sortbytempo implements Comparator<Features> {
    @Override
    public int compare(Features o1, Features o2) {
        return (o1.getTempo() >= o2.getTempo()) ? -1 : 1;
    }
}


