package uk.co.agilesoftware;

import java.util.UUID;

class CargoPackage {
    private final String belongsToStation;
    private final String id;

    @Override
    public String toString() {
        return String.format("Cargo Package(%s) for %s", id, belongsToStation);
    }

    CargoPackage(String belongsToStation) {
        this.id = UUID.randomUUID().toString();
        this.belongsToStation = belongsToStation;
    }

    boolean belongTo(Station station) {
        return station.name.equals(belongsToStation);
    }
}
