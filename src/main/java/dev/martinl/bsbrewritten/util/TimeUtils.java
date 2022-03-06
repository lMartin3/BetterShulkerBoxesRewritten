package dev.martinl.bsbrewritten.util;

public class TimeUtils {
    public static int[] formatToMinutesAndSeconds(int millis) {
        int minutes = millis / (1000 * 60);
        //Doing this just because codacy complains about it ¯\_(ツ)_/¯
        int nMillis = millis - (minutes * 1000 * 60);
        int seconds = nMillis / 1000;
        return new int[]{minutes, seconds};
    }
}
