package com.project.structures;

import java.util.Comparator;

public class TimeSorter implements Comparator<FinalResult> {
    @Override
    public int compare(FinalResult object1, FinalResult object2) {
        Integer intTime1 = (int) object1.getTime();
        Integer intTime2 = (int) object2.getTime();
        return intTime1.compareTo(intTime2);
    }
}