package uk.co.agilesoftware;

class CargoPackage {
    private final String belongsToStation;

    @Override
    public String toString() {
        return "Cargo Package for " + belongsToStation;
    }

    CargoPackage(String belongsToStation) {
        this.belongsToStation = belongsToStation;
    }

    boolean belongTo(Station station) {
        return station.name.equals(belongsToStation);
    }
}
