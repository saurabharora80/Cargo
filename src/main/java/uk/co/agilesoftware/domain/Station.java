package uk.co.agilesoftware.domain;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Station {
    //TODO: make configurable
    private final int CARGO_CAPACITY = 1000;

    final String name;
    private final BlockingQueue<CargoPackage> cargo = new LinkedBlockingQueue<>(CARGO_CAPACITY);

    public Station(String name) {
        this.name = name;
    }

    public void deliverPackages(Collection<CargoPackage> packages) {
        packages.stream().filter(p -> !p.belongTo(this)).forEach(p -> {
            if (!cargo.offer(p)) {
                Logger.getLogger(getClass().getSimpleName()).info(String.format("%s Cargo full. Dropping %s", this, p));
            }
        });
    }

    @Override
    public String toString() {
        return "Station " + name;
    }

    CargoPackage fetchPackage(long waitFor, TimeUnit milliseconds) {
        try {
            return cargo.poll(waitFor, milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException("Unable to fetch package from " + this, e);
        }
    }

    public Boolean isCargoFull() {
        return cargo.size() == CARGO_CAPACITY;
    }

    /**
     * Used only for testing
     *
     * @return
     */
    public boolean isCargoEmpty() {
        return cargo.isEmpty();
    }
}
