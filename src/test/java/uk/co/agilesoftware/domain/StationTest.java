package uk.co.agilesoftware.domain;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class StationTest {

    @Test
    public void shouldBeAbleToDeliverCargoToStation() {
        List<CargoPackage> packages = IntStream.range(1, 8).mapToObj(index -> new CargoPackage("S" + index)).collect(Collectors.toList());
        Station station = new Station("S0");
        station.deliverPackages(packages);

        Set<CargoPackage> deliveredPackages = new HashSet<>();

        while(!station.isCargoEmpty()) {
            deliveredPackages.add(station.fetchPackage(1, TimeUnit.MILLISECONDS));
        }

        assertThat(deliveredPackages.size(), is(7));
        assertThat(deliveredPackages, containsInAnyOrder(packages.toArray()));
    }

    @Test
    public void stationShouldRejectPackageMeantForIt() {
        List<CargoPackage> packages = IntStream.range(0, 8).mapToObj(index -> new CargoPackage("S" + index)).collect(Collectors.toList());
        Station station = new Station("S0");
        station.deliverPackages(packages);

        Set<CargoPackage> deliveredPackages = new HashSet<>();

        while(!station.isCargoEmpty()) {
            deliveredPackages.add(station.fetchPackage(1, TimeUnit.MILLISECONDS));
        }

        assertThat(deliveredPackages, not(contains(packages.get(0))));
    }

    @Test
    public void stationShouldRejectPackagesSilentlyIfCargoIsFull() {
        List<CargoPackage> packages = IntStream.rangeClosed(0, 1001).mapToObj(index -> new CargoPackage("S" + index)).collect(Collectors.toList());
        Station station = new Station("S0");
        station.deliverPackages(packages);

        assertTrue(station.isCargoFull());
    }

}