package uk.co.agilesoftware;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Train {
    private final int CARGO_CAPACITY = 100;

    private final String name;
    private final BlockingQueue<CargoPackage> cargo = new LinkedBlockingQueue<>(CARGO_CAPACITY);

    public Train(String name) {
        this.name = name;
    }

    void loadCargo(CargoPackage cargoPackage) {
        try {
            if (!cargo.offer(cargoPackage, 10, TimeUnit.MILLISECONDS)) {
                Logger.getLogger(getClass().getSimpleName()).info(this + " cargo is full. Dropping package " + cargoPackage);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(this + " couldn't accept " + cargoPackage + " for delivery", e);
        }
    }

    Boolean isCargoFull() {
        return cargo.size() == CARGO_CAPACITY;
    }

    @Override
    public String toString() {
        return "Train " + name;
    }
}
