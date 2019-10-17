package uk.co.agilesoftware;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

class Station {
    private final int CARGO_CAPACITY = 100;

    private final String name;
    private final BlockingQueue<CargoPackage> cargo = new LinkedBlockingQueue<>(CARGO_CAPACITY);

    Station(String name) {
        this.name = name;
    }

    void deliverPackages(Collection<CargoPackage> packages) {
        packages.forEach(p -> {
            try {
                if(!cargo.offer(p, 10, TimeUnit.MILLISECONDS)) {
                    Logger.getLogger(getClass().getSimpleName()).info(this + " cargo is full. Dropping package " + p);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(this + " couldn't accept "+ p + " for delivery", e);
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
}
