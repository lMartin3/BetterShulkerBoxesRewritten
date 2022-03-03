package dev.martinl.bsbrewritten.util;

public class TimeFormatter {
    public static int[] formatToMinutesAndSeconds(int millis) {
        int minutes = millis / (1000 * 60);
        millis -= minutes * 1000 * 60;
        int seconds = millis / 1000;
        return new int[]{minutes, seconds};
    }
}
