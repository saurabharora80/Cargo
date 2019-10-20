package uk.co.agilesoftware.domain;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThat;

public class RailwayTest {
    @Test
    public void railwaysShouldOnlyBeInstantiatedOnce() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        try {
            Callable<String> createRailwayInstance = () -> CircularRailway.getInstance().toString();

            List<Future<String>> railwayObjectIds = executor.invokeAll(new ArrayList<Callable<String>>(){{
                add(createRailwayInstance);
                add(createRailwayInstance);
                add(createRailwayInstance);
            }});

            List<String> ids = railwayObjectIds.stream().map(f -> {
                try {
                    return f.get(1, TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).distinct().collect(Collectors.toList());

            assertThat(ids.size(), CoreMatchers.is(1));
        } finally {
            executor.shutdown();
        }

    }
}
