package uk.co.agilesoftware;

import org.junit.Test;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TrainTest {

    @Test
    public void trainShouldProceedToNextStationAtTheGivenSpeed() {
        Train train = new Train("T1", 100);

        long timeTakenToTravel = duration(Train::goToNextStation, train).toMillis();

        assertThat(train.currentStationName(), is(station(0).name));
        assertThat(timeTakenToTravel, is(greaterThanOrEqualTo(1000L)));
    }

    @Test
    public void trainMustLoadPackagesUntilItsCargoIsFull() {
        station(0).deliverPackages(generatePackagesFor(100, station(7)));

        Train train = goToStation(new Train("T1", 1000), 0);
        long cargoLoadTime = duration(Train::loadCargo, train).toMillis();

        assertTrue(train.isFull());
        assertThat(cargoLoadTime, is(greaterThanOrEqualTo(Train.CARGO_CAPACITY * Train.perPackageLoadTimeInMillis())));
    }

    @Test
    public void trainMustLoadAllAvailablePackagesAndMoveOnEvenIfCargoIsNotFull() {
        //Don't have a way to reset Cargo at a particular Station(which are singletons); so use S1 instead of S0
        int noOfPackages = 5;
        station(1).deliverPackages(generatePackagesFor(noOfPackages, station(7)));

        Train train = goToStation(new Train("T1", 1000), 1);

        long cargoLoadTime = duration(Train::loadCargo, train).toMillis();

        assertTrue(!train.isFull());

        assertThat(cargoLoadTime, is(greaterThanOrEqualTo(noOfPackages * Train.perPackageLoadTimeInMillis())));
        assertThat(cargoLoadTime, is(lessThan((noOfPackages + 1) * Train.perPackageLoadTimeInMillis())));
    }


    @Test
    public void trainMustOffLoadTheCargoForAStation() {
        int noOfPackages = 5;
        station(2).deliverPackages(generatePackagesFor(noOfPackages, station(3)));

        Train train = goToStation(new Train("T1", 1000), 2);
        train.loadCargo();
        train.goToNextStation();

        long cargoOffLoadTime = duration(Train::offLoadCargo, train).toMillis();

        assertTrue(train.isEmpty());

        assertThat(cargoOffLoadTime, is(greaterThanOrEqualTo(noOfPackages * Train.perPackageOffLoadTimeInMillis())));
        assertThat(cargoOffLoadTime, is(lessThan((noOfPackages + 1) * Train.perPackageOffLoadTimeInMillis())));
    }

    @Test
    public void trainMustOffLoadOnlyTheCargoMeantForAStation() {
        int noOfPackages = 5;
        station(3).deliverPackages(generatePackagesFor(noOfPackages, station(4)));
        station(3).deliverPackages(generatePackagesFor(noOfPackages, station(5)));

        Train train = goToStation(new Train("T1", 1000), 3);
        train.loadCargo();
        train.goToNextStation();

        long cargoOffLoadTime = duration(Train::offLoadCargo, train).toMillis();

        assertTrue(!train.isEmpty());

        assertThat(cargoOffLoadTime, is(greaterThanOrEqualTo(noOfPackages * Train.perPackageOffLoadTimeInMillis())));
        assertThat(cargoOffLoadTime, is(lessThan((noOfPackages + 1) * Train.perPackageOffLoadTimeInMillis())));
    }

    private Station station(int i) {
        return Railway.getInstance().stations.get(i);
    }

    private List<CargoPackage> generatePackagesFor(int noOfPackagesAtStation, Station station) {
        return IntStream.range(1, noOfPackagesAtStation + 1).mapToObj(i -> new CargoPackage(station.name)).collect(Collectors.toList());
    }

    private Duration duration(Consumer<Train> f, Train t) {
        long startTime = System.currentTimeMillis();
        f.accept(t);
        return Duration.ofMillis(System.currentTimeMillis() - startTime);
    }

    private Train goToStation(Train t, int stationNumber) {
        if (stationNumber == -1) return t;
        else return goToStation(t.goToNextStation(), stationNumber - 1);
    }

}