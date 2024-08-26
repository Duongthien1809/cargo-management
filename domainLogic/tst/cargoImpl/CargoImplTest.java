package cargoImpl;

import administration.Customer;
import cargo.Cargo;
import cargo.Hazard;
import manager.CustomerImpl;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CargoImplTest {
    private Cargo cargoTest;

    @Test
    void getInsertionDate() {
        //init
        Date date = new Date();
        Customer customer = new CustomerImpl("Test");
        Collection<Hazard> hazards = Arrays.asList(Hazard.flammable, Hazard.toxic);
        cargoTest = new CargoImpl("cargo", customer, new BigDecimal(5), hazards);
        cargoTest.setStorageLocation(1);

        assertEquals(cargoTest.getInsertionDate().getTime(), date.getTime());
    }

    /**
     * test function
     */
    @Test
    void getOwner() {
        //init
        Customer customer = new CustomerImpl("Test");
        Collection<Hazard> hazards = Arrays.asList(Hazard.flammable, Hazard.toxic);
        cargoTest = new CargoImpl("cargo", customer, new BigDecimal(5), hazards);
        cargoTest.setStorageLocation(1);

        assertEquals(cargoTest.getCustomer().name(), "Test");
    }

    @Test
    void getDurationOfStorage() {
        //init
        Customer customer = new CustomerImpl("Test");
        Collection<Hazard> hazards = Arrays.asList(Hazard.flammable, Hazard.toxic);
        cargoTest = new CargoImpl("cargo", customer, new BigDecimal(5), hazards);
        cargoTest.setStorageLocation(1);

        Date currentDate = new Date();
        Date insertionDate = cargoTest.getInsertionDate();
        long durationInSeconds = TimeUnit.MILLISECONDS.toSeconds(currentDate.getTime() - insertionDate.getTime());

        assertEquals(cargoTest.getDurationOfStorage(), Duration.ofSeconds(durationInSeconds));
    }


    @Test
    void getStorageLocation() {
        //init
        Customer customer = new CustomerImpl("Test");
        Collection<Hazard> hazards = Arrays.asList(Hazard.flammable, Hazard.toxic);
        cargoTest = new CargoImpl("cargo", customer, new BigDecimal(5), hazards);
        cargoTest.setStorageLocation(1);

        assertEquals(cargoTest.getStorageLocation(), 1);
    }

    @Test
    void setStorageLocation() {
        //init
        Customer customer = new CustomerImpl("Test");
        Collection<Hazard> hazards = Arrays.asList(Hazard.flammable, Hazard.toxic);
        cargoTest = new CargoImpl("cargo", customer, new BigDecimal(5), hazards);
        cargoTest.setStorageLocation(1);

        assertEquals(cargoTest.getStorageLocation(), 1);
    }

    @Test
    void setLastInspectionDate() {
        //init
        Customer customer = new CustomerImpl("Test");
        Collection<Hazard> hazards = Arrays.asList(Hazard.flammable, Hazard.toxic);
        cargoTest = new CargoImpl("cargo", customer, new BigDecimal(5), hazards);
        cargoTest.setStorageLocation(1);

        //set lastInspectionDate to currentDate
        cargoTest.setLastInspectionDate();
        assertEquals(new Date().getTime(), cargoTest.getLastInspectionDate().getTime());
    }


    @Test
    void getValue() {
        //init
        Customer customer = new CustomerImpl("Test");
        Collection<Hazard> hazards = Arrays.asList(Hazard.flammable, Hazard.toxic);
        cargoTest = new CargoImpl("cargo", customer, new BigDecimal(5), hazards);
        cargoTest.setStorageLocation(1);

        //value = 5
        assertEquals(cargoTest.getValue(), new BigDecimal(5));
    }

    @Test
    void getHazards() {
        //init
        Customer customer = new CustomerImpl("Test");
        Collection<Hazard> hazards = Arrays.asList(Hazard.flammable, Hazard.toxic);
        cargoTest = new CargoImpl("cargo", customer, new BigDecimal(5), hazards);
        cargoTest.setStorageLocation(1);

        Collection<Hazard> hazardToTest = Arrays.asList(Hazard.flammable, Hazard.toxic);
        assertEquals(cargoTest.getHazards(), hazardToTest);
    }

    @Test
    void getCargoType() {
        //init
        Customer customer = new CustomerImpl("Test");
        Collection<Hazard> hazards = Arrays.asList(Hazard.flammable, Hazard.toxic);
        cargoTest = new CargoImpl("cargo", customer, new BigDecimal(5), hazards);
        cargoTest.setStorageLocation(1);

        assertEquals(cargoTest.getCargoType(), "cargo");
    }

    @Test
    void getCustomer() {
        //init
        Customer customer = new CustomerImpl("Test");
        Collection<Hazard> hazards = Arrays.asList(Hazard.flammable, Hazard.toxic);
        cargoTest = new CargoImpl("cargo", customer, new BigDecimal(5), hazards);
        cargoTest.setStorageLocation(1);

        assertEquals(cargoTest.getCustomer().name(), "Test");
    }

}