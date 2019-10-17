package uk.co.agilesoftware;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class CargoMoverTest {

    @Test
    public void mainTest() throws Exception {

        ExecutorService cargoDelivery = Executors.newFixedThreadPool(Railway.NO_OF_STATIONS);

        //Railway.getInstance().getStations().stream().ma



        ExecutorService trainRunner = Executors.newFixedThreadPool(Train.NO_OF_TRAINS);

        IntStream.range(1, Train.NO_OF_TRAINS+1).forEach(trainNo -> trainRunner.submit(new Train("T"+trainNo)));


    }
}
