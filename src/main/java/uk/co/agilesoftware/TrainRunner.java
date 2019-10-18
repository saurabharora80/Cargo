package uk.co.agilesoftware;

public class TrainRunner implements Runnable {

    private final Train train;

    public TrainRunner(Train train) {
        this.train = train;
    }

    @Override
    public void run() {
        while(true) {
            train.goToNextStation().offLoadCargo().loadCargo();
        }
    }
}
