package uk.co.agilesoftware;

import uk.co.agilesoftware.domain.Train;

import java.util.concurrent.CountDownLatch;

public class TrainRunner implements Runnable {

    private final Train train;
    /**
     * Need time to avoid using this for testing
     */
    private final CountDownLatch stationsVisited;

    TrainRunner(Train train) {
        this(train, null);
    }

    /**
     * Only used for unit testing
     * @param train
     * @param stationsVisited
     */
    TrainRunner(Train train, CountDownLatch stationsVisited) {
        this.train = train;
        this.stationsVisited = stationsVisited;
    }

    @Override
    public void run() {
        //Trains are always running
        while(true) {
            train.goToNextStation().offLoadCargo().loadCargo();
            if(stationsVisited!=null) stationsVisited.countDown();
        }
    }
}
