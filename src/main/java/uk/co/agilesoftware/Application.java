package uk.co.agilesoftware;

import uk.co.agilesoftware.domain.CircularRailway;
import uk.co.agilesoftware.domain.Train;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Application {

    public static void main(String[] args) {
        ScheduledExecutorService cargoDeliveryExecutor = Executors.newScheduledThreadPool(1);
        CircularRailway.getInstance().stations().values().forEach(station ->
            cargoDeliveryExecutor.scheduleWithFixedDelay(new StationCargoDelivery(station), 1, 1000, TimeUnit.MILLISECONDS)
        );

        ExecutorService trainRunnerExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        IntStream.range(0, CircularRailway.NO_OF_TRAINS).forEach(index ->
            trainRunnerExecutor.submit(new TrainRunner(new Train("T" + index, (index+1) * 100, CircularRailway.getInstance())))
        );
    }

}
