package uk.co.agilesoftware.domain;

import java.util.Map;
import java.util.concurrent.Semaphore;

public interface Railway {
    Map<Integer, Station> stations();
    Map<Integer, Semaphore> trackLocks();
}
