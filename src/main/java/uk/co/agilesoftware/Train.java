package uk.co.agilesoftware;

import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Train {

    private static final Logger logger = Logger.getLogger("Train");

    //TODO: Read from properties
    private final int CARGO_CAPACITY = 10;
    private final Duration MAX_PER_PACKAGE_LOAD_TIME = Duration.ofSeconds(1);
    private final Duration MAX_TRAIN_STOP = Duration.ofSeconds(10);
    private final Duration PER_PACKAGE_OFFLOAD_TIME = Duration.ofSeconds(1);

    private final String name;
    private final int speedInKmPerHr;
    private final BlockingQueue<CargoPackage> cargo = new LinkedBlockingQueue<>(CARGO_CAPACITY);

    private volatile AtomicInteger currentStationNumber = new AtomicInteger(-1);
    private Station currentStation;

    /**
     * This is a circular counter which resets to 0 if NextStation >= NO_OF_STATIONS
     */
    private synchronized int nextStation() {
        return currentStationNumber.incrementAndGet() % (Railway.NO_OF_STATIONS);
    }

    Train(String name, int speedInKmPerHr) {
        this.name = name;
        this.speedInKmPerHr = speedInKmPerHr;
    }

    void loadCargo() {
        while (true) {
            CargoPackage aPackage = currentStation.fetchPackage(MAX_TRAIN_STOP.toMillis(), TimeUnit.MILLISECONDS);
            if (aPackage == null) break;
            try {
                boolean isSpaceAvailable = cargo.offer(aPackage, MAX_PER_PACKAGE_LOAD_TIME.toMillis(), TimeUnit.MILLISECONDS);
                if(!isSpaceAvailable) break;
                logger.info(String.format("Loading %s on $%s at %s", aPackage, this, currentStation));
            } catch (InterruptedException e) {
                throw new RuntimeException(String.format("Unable to load %s onto %s", aPackage, this), e);
            }
        }
    }

    Train offLoadCargo() {
        cargo.iterator().forEachRemaining(p -> {
            if(p.belongTo(currentStation)) {
                try {
                    TimeUnit.MILLISECONDS.sleep(PER_PACKAGE_OFFLOAD_TIME.toMillis());
                } catch (InterruptedException e) {
                    throw new RuntimeException(String.format("Interrupted while offloading %s from %s at %s", p, this, currentStation));
                }
                logger.info(String.format("Offloading %s from $%s at %s", p, this, currentStation));
                cargo.remove(p);
            }
        });
        return this;
    }

    @Override
    public String toString() {
        return "Train " + name;
    }

    /**
     * Only 1 Train can be on the section of track between any 2 stations which makes the track between stations
     * a 'Shared Resource'.
     * This Shared Resource needs to be locked so that only 1 train can use it at any given time.
     * @return
     */
    private Train goToStation(int nextStation) {
        Semaphore trackLock = Railway.getInstance().trackLocks.get(nextStation);
        try {
            trackLock.acquire();
            TimeUnit.MILLISECONDS.sleep((Railway.TRACK_LENGTH_IN_KM/speedInKmPerHr)*100);
            currentStation = Railway.getInstance().stations.get(nextStation);
            logger.info(String.format("%s has arrived at %s", this, currentStation));
            return this;
        } catch (InterruptedException exception) {
            throw new RuntimeException("Unable to proceed to station " + nextStation);
        } finally {
            trackLock.release();
        }
    }

    Train goToNextStation() {
        return goToStation(nextStation());
    }

}
