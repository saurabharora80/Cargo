package uk.co.agilesoftware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.agilesoftware.domain.CargoPackage;
import uk.co.agilesoftware.domain.CircularRailway;
import uk.co.agilesoftware.domain.Station;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class StationCargoDelivery implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(StationCargoDelivery.class);

    //TODO: make configurable
    private static final int NO_OF_PACKAGES_TO_DELIVER = 100;

    private final Station station;
    private final CountDownLatch deliveriesDone;

    private final Random random = new Random();

    StationCargoDelivery(Station station) {
        this(station, null);
    }

    /**
     * Used for Unit testing
     * @param station
     * @param deliveriesDone
     */
    StationCargoDelivery(Station station, CountDownLatch deliveriesDone) {
        this.station = station;
        this.deliveriesDone = deliveriesDone;
    }

    private int someOtherStation() {
        int i = random.nextInt(CircularRailway.NO_OF_STATIONS);
        if(station.equals(new Station(i))) return someOtherStation();
        else return i;
    }

    @Override
    public void run() {
        List<CargoPackage> packages = IntStream.range(0, NO_OF_PACKAGES_TO_DELIVER)
                .mapToObj(i -> new CargoPackage(someOtherStation()))
                .collect(Collectors.toList());

        station.deliverPackages(packages);

        if(deliveriesDone!=null) deliveriesDone.countDown();

        logger.info(String.format("Delivered %s to %s", NO_OF_PACKAGES_TO_DELIVER, station));
    }
}
