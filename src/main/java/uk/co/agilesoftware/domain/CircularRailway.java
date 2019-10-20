package uk.co.agilesoftware.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

public class CircularRailway implements Railway {

    //TODO: make configurable
    public static final int NO_OF_STATIONS = 8;
    public static final int NO_OF_TRAINS = 4;
    static final int DISTANCE_BETWEEN_STATIONS_IN_KM = 100;

    /**
     * Shared Immutable State: No need to Synchronise
     */
    private final Map<Integer, Station> stations = new HashMap<>();
    /**
     * Shared Immutable State: No need to Synchronise
     */
    private final Map<Integer, Semaphore> trackLocks = new HashMap<>();

    private CircularRailway() {
        IntStream.range(0, NO_OF_STATIONS).forEach(index -> stations.put(index, new Station(index)));
        IntStream.range(0, NO_OF_STATIONS).forEach(index -> trackLocks.put(index, new Semaphore(1)));
    }

    @Override
    public Map<Integer, Station> stations() {
        return Collections.unmodifiableMap(stations);
    }

    @Override
    public Map<Integer, Semaphore> trackLocks() {
        return Collections.unmodifiableMap(trackLocks);
    }

    private static class SingletonHelper {
        private static final CircularRailway INSTANCE = new CircularRailway();
    }

    public static CircularRailway getInstance() {
        return SingletonHelper.INSTANCE;
    }
}
