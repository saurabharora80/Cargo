package uk.co.agilesoftware;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class RailwayTest {
    @Test
    public void railwaysShouldOnlyBeInstantiatedOnce() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        Callable<String> createRailwayInstance = () -> Railway.getInstance().toString();

        List<Future<String>> railwayObjectIds = executor.invokeAll(new ArrayList<Callable<String>>(){{
            add(createRailwayInstance);
            add(createRailwayInstance);
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

        Assert.assertThat(ids.size(), CoreMatchers.is(1));
    }
}
