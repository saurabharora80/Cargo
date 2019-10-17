package uk.co.agilesoftware;

class CargoPackage {
    private final int index;

    @Override
    public String toString() {
        return "Cargo Package " + index;
    }

    public CargoPackage(int index) {
        this.index = index;
    }
}
