package uk.co.agilesoftware;

import org.junit.Test;
import uk.co.agilesoftware.domain.CircularRailway;
import uk.co.agilesoftware.domain.Railway;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StationCargoDeliveryTest {

    private Railway railway = new TestRailway();

    @Test
    public void deliverCargoToAllStation() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            CountDownLatch deliveries = new CountDownLatch(CircularRailway.NO_OF_STATIONS);
            railway.stations().values().forEach(station -> executorService.submit(new StationCargoDelivery(station, deliveries)));
            deliveries.await(2, TimeUnit.SECONDS);

            railway.stations().values().forEach(station -> assertThat(station.cargoSize(), is(100)));

        } finally {
            executorService.shutdown();
        }


    }

}