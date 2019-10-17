package uk.co.agilesoftware;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

class Railway {
    //TODO: Read from Property
    static final int NO_OF_STATIONS = 8;

    private final List<Station> stations = new LinkedList<>();

    private Railway() {
        IntStream.range(0, NO_OF_STATIONS).forEach(index -> stations.add(index, new Station("S"+(index+1))));
    }

    Station getStation(int stationNumber) {
        return stations.get(stationNumber);
    }

    List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    private static class SingletonHelper {
        private static final Railway INSTANCE = new Railway();
    }

    static Railway getInstance() {
        return SingletonHelper.INSTANCE;
    }
}
