package uk.co.agilesoftware;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StationCargoDelivery implements Runnable {

    private final Station station;
    private final Random random = new Random();

    public StationCargoDelivery(Station station) {
        this.station = station;
    }

    @Override
    public void run() {
        while(true) {
            List<CargoPackage> packages = IntStream.range(0, 100).asLongStream()
                    .mapToObj(i -> new CargoPackage("S" + random.nextInt(Railway.NO_OF_STATIONS)))
                    .collect(Collectors.toList());

            station.deliverPackages(packages);
        }
    }
}
