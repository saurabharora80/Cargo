package uk.co.agilesoftware;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrainRunnerTest {
    @Test
    public void runTrains() {

        Railway.getInstance().stations.values().forEach(station -> station.deliverPackages(new ArrayList<>()));
        ExecutorService executor = Executors.newFixedThreadPool(4);

        executor.submit(new TrainRunner(new Train("T1", 100)));
        executor.submit(new TrainRunner(new Train("T2", 100)));
        executor.submit(new TrainRunner(new Train("T3", 100)));
        executor.submit(new TrainRunner(new Train("T4", 100)));
    }
}
