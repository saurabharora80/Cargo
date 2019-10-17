package uk.co.agilesoftware;

import org.junit.Test;

public class CargoMoverTest {

    @Test
    public void mainTest() throws Exception {
        Railway railway = Railway.getInstance();

        Train t1 = new Train("T1");

        while(true) {
            railway.nextStation();
        }
    }
}
