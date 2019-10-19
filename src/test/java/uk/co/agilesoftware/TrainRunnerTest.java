package uk.co.agilesoftware;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainRunnerTest {
    @Test
    public void runTrains() {

        List<CargoPackage> packages = IntStream.range(0, Railway.NO_OF_STATIONS).asLongStream()
                .mapToObj(i -> new CargoPackage("S" + i))
                .collect(Collectors.toList());

        //Railway.getInstance().stations.values().forEach(station -> station.deliverPackages(packages));

        ExecutorService trainRunnerExecutor = Executors.newFixedThreadPool(4);

        IntStream.range(1, 5).forEach(index ->
                trainRunnerExecutor.submit(new TrainRunner(new Train("T" + index, index * 10)))
        );
    }
}
