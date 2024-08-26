package eventSystem;

import cargo.Cargo;
import eventSystem.ObserverPattern.CargoObserver;
import eventSystem.eventListeners.GLEventListener;
import manager.Warehouse.CargoWarehouse;
import manager.Warehouse.CustomerWarehouse;
import manager.Warehouse.WarehouseFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EventGenerator;

import java.util.EventObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CargoObserverTest {
    EventHandler eventHandler;
    CustomerWarehouse customerWarehouse;
    GLEventListener<Cargo> glEventListener;
    WarehouseFacade<Cargo> warehouseFacade;
    CargoObserver<Cargo> cargoCargoObserver;
    CargoWarehouse<Cargo> cargoWarehouse;

    @BeforeEach
    void setUp() {
        customerWarehouse = new CustomerWarehouse();
        cargoWarehouse = mock(CargoWarehouse.class);
        eventHandler = new EventHandler();
        warehouseFacade = new WarehouseFacade<>(customerWarehouse, cargoWarehouse);
        glEventListener = new GLEventListener<>(warehouseFacade);
        this.eventHandler.add(glEventListener);
        cargoCargoObserver = new CargoObserver<>(warehouseFacade);
        warehouseFacade.registerObserver(cargoCargoObserver);
    }

    @Test
    void notifyCreateCargoStorageIs90PercentTest() {
        customerWarehouse.addCustomer("Alice");
        String input = "UnitisedCargo Alice 10000 , false";
        when(cargoWarehouse.getStorageCapacity()).thenReturn(100);
        when(cargoWarehouse.getCurrentSize()).thenReturn(90);
        // Test
        EventObject eventObject = EventGenerator.createEvent("c", input.split(" "));
        String msg = this.eventHandler.handleWithMsg(eventObject);

        // Verify
        assertEquals("90.0 of storage reached!", warehouseFacade.getStatus());
    }
    @Test
    void notifyCreateCargoStorageAbove90PercentTest() {
        customerWarehouse.addCustomer("Alice");
        String input = "UnitisedCargo Alice 10000 , false";
        when(cargoWarehouse.getStorageCapacity()).thenReturn(100);
        when(cargoWarehouse.getCurrentSize()).thenReturn(91);
        // Test
        EventObject eventObject = EventGenerator.createEvent("c", input.split(" "));
        String msg = this.eventHandler.handleWithMsg(eventObject);

        // Verify
        assertEquals("91.0 of storage reached!", warehouseFacade.getStatus());
    }
    @Test
    void notifyCreateCargoStorageIs89PercentTest() {
        customerWarehouse.addCustomer("Alice");
        String input = "UnitisedCargo Alice 10000 , false";
        when(cargoWarehouse.getStorageCapacity()).thenReturn(100);
        when(cargoWarehouse.getCurrentSize()).thenReturn(89);
        // Test
        EventObject eventObject = EventGenerator.createEvent("c", input.split(" "));
        String msg = this.eventHandler.handleWithMsg(eventObject);

        // Verify
        assertNull(warehouseFacade.getStatus());
    }
}