package uk.co.agilesoftware;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class StationTest {

    @Test
    public void shouldBeAbleToDeliverCargoToStation() throws Exception {
        final HashSet<CargoPackage> cargoPackages = new HashSet<CargoPackage>() {{
            IntStream.range(1, 50).forEach(index -> add(new CargoPackage(index)));
        }};

        final Station station = new Station("S1");

        ExecutorService executor = Executors.newFixedThreadPool(5);

        Runnable deliverPackages = () -> station.deliverPackages(cargoPackages);

        executor.submit(deliverPackages).get(2, TimeUnit.SECONDS);
        executor.submit(deliverPackages).get(2, TimeUnit.SECONDS);
        executor.submit(deliverPackages).get(2, TimeUnit.SECONDS);

        Assert.assertTrue(station.isCargoFull());

    }

}