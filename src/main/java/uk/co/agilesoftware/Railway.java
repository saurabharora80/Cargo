package uk.co.agilesoftware;

import java.util.LinkedList;
import java.util.stream.IntStream;

public class Railway {
    //TODO: Read from Property
    private final int NO_OF_STATIONS = 8;

    private final LinkedList<Station> stations = new LinkedList<>();

    private Railway() {
        IntStream.range(0, NO_OF_STATIONS).forEach(index -> stations.add(index, new Station("S"+(index+1))));
    }

    private static class SingletonHelper {
        private static final Railway INSTANCE = new Railway();
    }

    public static Railway getInstance() {
        return SingletonHelper.INSTANCE;
    }
}
