package uk.co.agilesoftware;

import uk.co.agilesoftware.domain.CircularRailway;
import uk.co.agilesoftware.domain.Railway;
import uk.co.agilesoftware.domain.Station;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

public class TestRailway implements Railway {

    private final Map<Integer, Station> stations = new HashMap<>();
    private final Map<Integer, Semaphore> trackLocks = new HashMap<>();

    public TestRailway() {
        IntStream.range(0, CircularRailway.NO_OF_STATIONS).forEach(index -> stations.put(index, new Station("S"+(index))));
        IntStream.range(0, CircularRailway.NO_OF_STATIONS).forEach(index -> trackLocks.put(index, new Semaphore(1)));
    }

    @Override
    public Map<Integer, Station> stations() {
        return stations;
    }

    @Override
    public Map<Integer, Semaphore> trackLocks() {
        return trackLocks;
    }
}