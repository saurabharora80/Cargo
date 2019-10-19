package uk.co.agilesoftware;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import java.util.stream.IntStream;

class Railway {
    //TODO: Read from Property
    static final int NO_OF_STATIONS = 8;
    static final int TRACK_LENGTH_IN_KM = 1000;

    /**
     * Shared Immutable State: No need to Synchronise
     */
    final Map<Integer, Station> stations = new HashMap<>();
    /**
     * Shared Immutable State: No need to Synchronise
     */
    final Map<Integer, Semaphore> trackLocks = new HashMap<>();

    private Railway() {
        IntStream.range(0, NO_OF_STATIONS).forEach(index -> stations.put(index, new Station("S"+(index))));
        IntStream.range(0, NO_OF_STATIONS).forEach(index -> trackLocks.put(index, new Semaphore(1)));
    }

    private static class SingletonHelper {
        private static final Railway INSTANCE = new Railway();
    }

    static Railway getInstance() {
        return SingletonHelper.INSTANCE;
    }
}
