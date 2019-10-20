package uk.co.agilesoftware.domain;

import java.util.Objects;
import java.util.UUID;

public class CargoPackage {
    private final String belongsToStation;
    private final String id;

    CargoPackage(String belongsToStation) {
        this.id = UUID.randomUUID().toString();
        this.belongsToStation = belongsToStation;
    }

    public CargoPackage(int stationIndex) {
        this(Station.NAME_PREFIX + stationIndex);
    }

    boolean belongTo(Station station) {
        return station.name.equals(belongsToStation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CargoPackage that = (CargoPackage) o;
        return belongsToStation.equals(that.belongsToStation) &&
                id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(belongsToStation, id);
    }

    @Override
    public String toString() {
        return String.format("Cargo Package(%s) for %s", id, belongsToStation);
    }
}
