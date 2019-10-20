package uk.co.agilesoftware.domain;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Station {

    static final String NAME_PREFIX = "S";

    //TODO: make configurable
    private final int CARGO_CAPACITY = 1000;

    final String name;
    private final BlockingQueue<CargoPackage> cargo = new LinkedBlockingQueue<>(CARGO_CAPACITY);

    private Station(String name) {
        this.name = name;
    }

    public Station(int index) {
        this(NAME_PREFIX + index);
    }

    public void deliverPackages(Collection<CargoPackage> packages) {
        packages.stream().filter(p -> !p.belongTo(this)).forEach(p -> {
            if (!cargo.offer(p)) {
                Logger.getLogger(getClass().getSimpleName()).info(String.format("%s Cargo full. Dropping %s", this, p));
            }
        });
    }

    CargoPackage fetchPackage(long waitFor, TimeUnit milliseconds) {
        try {
            return cargo.poll(waitFor, milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException("Unable to fetch package from " + this, e);
        }
    }

    Boolean isCargoFull() {
        return cargo.size() == CARGO_CAPACITY;
    }

    public int cargoSize() {
        return cargo.size();
    }

    /**
     * Used only for testing
     *
     * @return
     */
    public boolean isCargoEmpty() {
        return cargo.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return name.equals(station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Station " + name;
    }
}
