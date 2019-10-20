package uk.co.agilesoftware;

import uk.co.agilesoftware.domain.CargoPackage;
import uk.co.agilesoftware.domain.CircularRailway;
import uk.co.agilesoftware.domain.Station;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class StationCargoDelivery implements Runnable {

    private static final Logger logger = Logger.getLogger("StationCargoDelivery");

    //TODO: make configurable
    private static final int NO_OF_PACKAGES_TO_DELIVER = 100;

    private final Station station;
    private final Random random = new Random();

    StationCargoDelivery(Station station) {
        this.station = station;
    }

    @Override
    public void run() {
        List<CargoPackage> packages = IntStream.range(0, NO_OF_PACKAGES_TO_DELIVER).asLongStream()
                .mapToObj(i -> new CargoPackage("S" + random.nextInt(CircularRailway.NO_OF_STATIONS)))
                .collect(Collectors.toList());

        station.deliverPackages(packages);

        logger.info(String.format("Delivered %s to %s", NO_OF_PACKAGES_TO_DELIVER, station));
    }
}
