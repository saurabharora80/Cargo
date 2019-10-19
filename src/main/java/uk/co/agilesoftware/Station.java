package uk.co.agilesoftware;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

class Station {

    private final int CARGO_CAPACITY = 1000;

    final String name;
    private final BlockingQueue<CargoPackage> cargo = new LinkedBlockingQueue<>(CARGO_CAPACITY);

    Station(String name) {
        this.name = name;
    }

    void deliverPackages(Collection<CargoPackage> packages) {
        packages.stream().filter(p -> !p.belongTo(this)).forEach(p -> {
            try {
                if (cargo.offer(p, 10, TimeUnit.MILLISECONDS)) {
                    //Logger.getLogger(getClass().getSimpleName()).info(String.format("Delivering %s to %s", p, this));
                } else {
                    Logger.getLogger(getClass().getSimpleName()).info(String.format("%s Cargo full. Dropping %s", this, p));
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(this + " couldn't accept " + p + " for delivery", e);
            }
        });
    }

    Boolean isCargoFull() {
        return cargo.size() == CARGO_CAPACITY;
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
}
