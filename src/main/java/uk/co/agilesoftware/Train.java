package uk.co.agilesoftware;

import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Train implements Runnable {

    //TODO: Read from properties
    static final int NO_OF_TRAINS = 4;
    private final int CARGO_CAPACITY = 10;
    private final Duration MAX_CARGO_LOAD_TIME = Duration.ofMillis(10);
    private final Duration CARGO_ARRIVAL_WAIT_TIME = Duration.ofMillis(100);
    private final Duration MAX_TRAIN_STOP = CARGO_ARRIVAL_WAIT_TIME.multipliedBy(CARGO_CAPACITY).plusMillis(MAX_CARGO_LOAD_TIME.toMillis()*CARGO_CAPACITY);

    private final String name;
    private final BlockingQueue<CargoPackage> cargo = new LinkedBlockingQueue<>(CARGO_CAPACITY);

    private volatile AtomicInteger currentLocation = new AtomicInteger(-1);

    private synchronized int currentLocation() {
        return currentLocation.incrementAndGet() % (Railway.NO_OF_STATIONS - 1);
    }

    Train(String name) {
        this.name = name;
    }

    private Boolean isCargoFull() {
        return cargo.size() == CARGO_CAPACITY;
    }

    @Override
    public void run() {
        Railway railway = Railway.getInstance();
        while (true) {
            Station currentStation = railway.getStation(currentLocation());
            this.offLoadCargo(currentStation);
            try {
                this.loadCargo(currentStation);
            } catch (InterruptedException e) {
                throw new RuntimeException("Unable to load cargo on " + this + " at " + currentStation, e);
            }
        }
    }

    private void loadCargo(Station currentStation) throws InterruptedException {
        currentStation.acquireLock();

        long cargoLoadingStartTime = System.currentTimeMillis();

        while (!isCargoFull() && (System.currentTimeMillis() - cargoLoadingStartTime) >= MAX_TRAIN_STOP.toMillis()) {
            CargoPackage aPackage = currentStation.fetchPackage(CARGO_ARRIVAL_WAIT_TIME.toMillis(), TimeUnit.MILLISECONDS);
            if (aPackage == null) break;
            cargo.offer(aPackage, MAX_CARGO_LOAD_TIME.toMillis(), TimeUnit.MILLISECONDS);
        }

        currentStation.releaseLock();
    }

    private void offLoadCargo(Station currentStation) {
        cargo.removeIf(p -> p.belongTo(currentStation));
    }

    @Override
    public String toString() {
        return "Train " + name;
    }


}
