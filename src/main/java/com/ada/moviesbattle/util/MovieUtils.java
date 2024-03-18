package com.ada.moviesbattle.util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MovieUtils {

    private static final Random random = new Random();
    public static List<String> IMDB_IDS = Arrays.asList(
            "tt0050083", "tt0060196", "tt0068646", "tt0071562", "tt0108052",
            "tt0110912", "tt0111161", "tt0111162", "tt0167260", "tt0468569"
    );

    public static String getRandomImdbId() {
        int index = random.nextInt(IMDB_IDS.size());
        return IMDB_IDS.get(index);
    }
}
