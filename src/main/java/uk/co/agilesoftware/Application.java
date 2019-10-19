package uk.co.agilesoftware;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Application {

    public static void main(String[] args) {
        Railway.getInstance().stations.get(0).deliverPackages(generatePackages());

        ExecutorService trainRunnerExecutor = Executors.newFixedThreadPool(4);

        IntStream.range(1, 5).forEach(index ->
                trainRunnerExecutor.submit(new TrainRunner(new Train("T" + index, index * 100)))
        );
    }

    private static List<CargoPackage> generatePackages() {
        return IntStream.range(0, Railway.NO_OF_STATIONS).asLongStream()
                .mapToObj(i -> new CargoPackage("S" + i))
                .collect(Collectors.toList());
    }
}
