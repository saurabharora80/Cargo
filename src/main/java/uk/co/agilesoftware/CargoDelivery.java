package uk.co.agilesoftware;

public class CargoDelivery implements Runnable {

    @Override
    public void run() {
        Railway.getInstance().getStations();
    }
}
