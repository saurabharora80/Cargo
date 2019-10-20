package uk.co.agilesoftware;

import org.junit.Test;
import uk.co.agilesoftware.domain.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

public class TrainRunnerTest {

    @Test
    public void oneTrainShouldDeliverPackagesFromFirstStationToAllStations() throws Exception {
        ExecutorService trainRunner = Executors.newFixedThreadPool(1);
        try {
            Railway railway = new TestRailway();
            Station firstStation = railway.stations().get(0);
            firstStation.deliverPackages(IntStream.range(1, CircularRailway.NO_OF_STATIONS).mapToObj(CargoPackage::new).collect(Collectors.toList()));

            CountDownLatch stationsVisited = new CountDownLatch(CircularRailway.NO_OF_STATIONS);

            Train train = new Train("T1", 1000, railway);
            trainRunner.submit(new TrainRunner(train, stationsVisited));

            stationsVisited.await(3, TimeUnit.SECONDS);

            assertTrue(train.isEmpty());
            assertTrue(firstStation.isCargoEmpty());
        } finally {
            trainRunner.shutdown();
        }
    }

    @Test
    public void fourTrainShouldDeliverPackagesFromFirstStationToAllStations() throws Exception {
        ExecutorService trainRunner = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            Railway railway = new TestRailway();
            Station firstStation = railway.stations().get(0);

            //Deliver 5 package meant for each Station at Station0 = 5*7 = 35 packages
            IntStream.range(1, 6).forEach(ignore ->
                firstStation.deliverPackages(IntStream.range(1, CircularRailway.NO_OF_STATIONS).mapToObj(CargoPackage::new).collect(Collectors.toList()))
            );
            //Deliver 5 for packages = Total 40 packages
            firstStation.deliverPackages(IntStream.range(1, 6).mapToObj(CargoPackage::new).collect(Collectors.toList()));

            //I need a Tuple!
            List<Tuple> trainRunnerTuple = IntStream.range(0, CircularRailway.NO_OF_TRAINS).mapToObj(trainNo ->
                new Tuple(new Train("T" + trainNo, 1000, railway), new CountDownLatch(CircularRailway.NO_OF_STATIONS))).collect(Collectors.toList()
            );

            trainRunnerTuple.forEach(tuple -> trainRunner.submit(new TrainRunner(tuple.train, tuple.countDownLatch)));

            for (Tuple tuple : trainRunnerTuple) {
                tuple.countDownLatch.await(5, TimeUnit.SECONDS);
            }

            for (Tuple tuple : trainRunnerTuple) {
                assertTrue(tuple.train.isEmpty());
            }

            assertTrue(firstStation.isCargoEmpty());
        } finally {
            trainRunner.shutdown();
        }
    }

    private final class Tuple {

        final Train train;
        final CountDownLatch countDownLatch;

        Tuple(Train train, CountDownLatch countDownLatch) {
            this.train = train;
            this.countDownLatch = countDownLatch;
        }
    }
}