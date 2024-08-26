package eventSystem;

import administration.Customer;
import cargo.Cargo;
import cargo.Hazard;
import eventSystem.eventListeners.GLEventListener;
import manager.CustomerImpl;
import manager.Warehouse.CargoWarehouse;
import manager.Warehouse.CustomerWarehouse;
import manager.Warehouse.WarehouseFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EventGenerator;
import utils.ParseString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.EventObject;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GLEventListenerTest {
    //test über Verhalten über CLI
    WarehouseFacade<Cargo> warehouseFacade;
    CustomerWarehouse customerWarehouse;
    CargoWarehouse<Cargo> cargoWarehouse;
    EventHandler eventHandler;
    GLEventListener<Cargo> glEventListener;
    String sampleCustomerName = "Alice";
    ParseString<Cargo> parseString;

    @BeforeEach
    void setUp() {
        customerWarehouse = new CustomerWarehouse();
        cargoWarehouse = new CargoWarehouse<>(100);
        warehouseFacade = new WarehouseFacade<>(customerWarehouse, cargoWarehouse);

        eventHandler = new EventHandler();
        glEventListener = new GLEventListener<>(warehouseFacade);
        this.eventHandler.add(glEventListener);
        parseString = new ParseString<>();
    }

    @Test
    void insertCustomerValidTest() {
        String name = "test";
        EventObject eventObject = EventGenerator.createEvent("c", name.split(" "));

        //act
        this.eventHandler.handle(eventObject);
        Customer customer = customerWarehouse.getCustomer(name);
        //test
        assertEquals(name, customer.name());
    }

    @Test
    void insertCustomerWithNameAlreadyExist() {
        //add exist name to input
        customerWarehouse.addCustomer(sampleCustomerName);
        EventObject eventObject = EventGenerator.createEvent("c", sampleCustomerName.split(" "));
        //act
        String msg = this.eventHandler.handleWithMsg(eventObject);

        //test
        assertEquals("customer insert failed!", msg);
    }

    @Test
    void showCustomersValidTest() {
        //add name to warehouse
        customerWarehouse.addCustomer(sampleCustomerName);
        //list all customers event
        EventObject eventObject = EventGenerator.createEvent("r", "customers".split(" "));
        //act
        String msg = this.eventHandler.handleWithMsg(eventObject);
        StringBuilder expectMsg = new StringBuilder();
        Set<Customer> customers = warehouseFacade.getCustomerWarehouse().listAllCustomers();
        expectMsg.append(parseString.getCustomerInfo(customers.iterator().next(), warehouseFacade));
        //test
        assertEquals(expectMsg.toString(), msg);
    }

    @Test
    void showCustomersFalseTest() {
        //add new Customers
        EventObject eventObject = EventGenerator.createEvent("r", "customers".split(" "));
        String msg = this.eventHandler.handleWithMsg(eventObject);

        assertEquals("no customers in storage", msg);
    }

    @Test
    void insertCargoValidTest() {
        //add name to warehouse
        customerWarehouse.addCustomer(sampleCustomerName);
        String input = "UnitisedCargo " + sampleCustomerName + " 10000 , false";
        //list all customers event
        EventObject eventObject = EventGenerator.createEvent("c", input.split(" "));
        //act
        String msg = this.eventHandler.handleWithMsg(eventObject);

        assertEquals("cargo is inserted!", msg);
    }

    @Test
    void insertCargoInValidTest() {
        //add name to warehouse
        customerWarehouse.addCustomer(sampleCustomerName);
        //insert invalid name
        String input = "UnitisedCargo " + "test" + " 10000 , false";
        //list all customers event
        EventObject eventObject = EventGenerator.createEvent("c", input.split(" "));
        //act
        String msg = this.eventHandler.handleWithMsg(eventObject);

        assertEquals("cargo insert failed!", msg);
    }

    @Test
    void showCargosValidTest() {
        //init
        customerWarehouse.addCustomer(sampleCustomerName);
        Cargo cargo = mock(Cargo.class);
        when(cargo.getCargoType()).thenReturn("UnitisedCargo");
        when(cargo.getCustomer()).thenReturn(new CustomerImpl(sampleCustomerName));
        when(cargo.getValue()).thenReturn(BigDecimal.valueOf(100));
        when(cargo.getHazards()).thenReturn(List.of(Hazard.flammable));
        cargoWarehouse.insert(cargo);

        //list All Cargo
        EventObject eventObject = EventGenerator.createEvent("r", "cargos".split(" "));
        String msg = this.eventHandler.handleWithMsg(eventObject);

        //test
        assertNotEquals("no customers in storage", msg);
    }

    @Test
    void showCargosNotFoundTest() {
        //init
        customerWarehouse.addCustomer(sampleCustomerName);

        //list All Cargo
        EventObject eventObject = EventGenerator.createEvent("r", "cargos".split(" "));
        String msg = this.eventHandler.handleWithMsg(eventObject);

        //test
        assertEquals(0, warehouseFacade.listCargos().size());
        assertEquals("no cargos in storage", msg);
    }

    @Test
    void showCargoByCargoTypeValidTest() {
        //init
        customerWarehouse.addCustomer(sampleCustomerName);
        Cargo cargo = mock(Cargo.class);
        when(cargo.getCargoType()).thenReturn("UnitisedCargo");
        when(cargo.getCustomer()).thenReturn(new CustomerImpl(sampleCustomerName));
        cargoWarehouse.insert(cargo);

        EventObject eventObject = EventGenerator.createEvent("r", "cargos UnitisedCargo".split(" ")); //search UnitisedCargo
        String msg = this.eventHandler.handleWithMsg(eventObject);

        //test
        assertTrue(msg.contains("UnitisedCargo"));
    }

    @Test
    void showCargoByCargoTypeInvalidTest() {
        //init
        customerWarehouse.addCustomer(sampleCustomerName);
        Cargo cargo = mock(Cargo.class);
        when(cargo.getCargoType()).thenReturn("UnitisedCargo");
        when(cargo.getCustomer()).thenReturn(new CustomerImpl(sampleCustomerName));
        cargoWarehouse.insert(cargo);

        EventObject eventObject = EventGenerator.createEvent("r", "cargos DryBulkCargo".split(" ")); //search DryBulkCargo
        String msg = this.eventHandler.handleWithMsg(eventObject);

        //test
        assertEquals("no cargos in storage", msg);
    }

    @Test
    void showHazardsIncludeValidTest() {
        //init
        customerWarehouse.addCustomer(sampleCustomerName);
        Cargo cargo = mock(Cargo.class);
        when(cargo.getCargoType()).thenReturn("UnitisedCargo");
        when(cargo.getCustomer()).thenReturn(new CustomerImpl(sampleCustomerName));
        when(cargo.getHazards()).thenReturn(List.of(Hazard.flammable));
        cargoWarehouse.insert(cargo);

        EventObject eventObject = EventGenerator.createEvent("r", "hazards i".split(" ")); //search include hazards
        String msg = this.eventHandler.handleWithMsg(eventObject);

        assertEquals("include hazards: flammable, ", msg);
    }

    @Test
    void showHazardsExcludeValidTest() {
        //init
        customerWarehouse.addCustomer(sampleCustomerName);
        Cargo cargo = mock(Cargo.class);
        when(cargo.getCargoType()).thenReturn("UnitisedCargo");
        when(cargo.getCustomer()).thenReturn(new CustomerImpl(sampleCustomerName));
        when(cargo.getHazards()).thenReturn(List.of(Hazard.flammable));
        cargoWarehouse.insert(cargo);

        EventObject eventObject = EventGenerator.createEvent("r", "hazards e".split(" ")); //search exclude hazard
        String msg = this.eventHandler.handleWithMsg(eventObject);

        assertEquals("exclude hazards: explosive, toxic, radioactive, ", msg);
    }

    @Test
    void showHazardsIncludeNoHazardsTest() {
        //init
        customerWarehouse.addCustomer(sampleCustomerName);
        Cargo cargo = mock(Cargo.class);
        when(cargo.getCargoType()).thenReturn("UnitisedCargo");
        when(cargo.getCustomer()).thenReturn(new CustomerImpl(sampleCustomerName));
        cargoWarehouse.insert(cargo);

        EventObject eventObject = EventGenerator.createEvent("r", "hazards i".split(" ")); //search include hazard
        String msg = this.eventHandler.handleWithMsg(eventObject);

        assertEquals("include hazards: ", msg);
    }

    @Test
    void showHazardsExcludeNoHazardTest() {
        //init
        customerWarehouse.addCustomer(sampleCustomerName);
        Cargo cargo = mock(Cargo.class);
        when(cargo.getCargoType()).thenReturn("UnitisedCargo");
        when(cargo.getCustomer()).thenReturn(new CustomerImpl(sampleCustomerName));
        cargoWarehouse.insert(cargo);

        EventObject eventObject = EventGenerator.createEvent("r", "hazards e".split(" ")); //search exclude hazard
        String msg = this.eventHandler.handleWithMsg(eventObject);

        assertEquals("exclude hazards: explosive, flammable, toxic, radioactive, ", msg);//because there are no include hazard in warehouse
    }

    @Test
    void inspectCargoValidTest() {
        //init
        customerWarehouse.addCustomer(sampleCustomerName);
        Cargo cargo = mock(Cargo.class);
        when(cargo.getCargoType()).thenReturn("UnitisedCargo");
        when(cargo.getCustomer()).thenReturn(new CustomerImpl(sampleCustomerName));
        when(cargo.getLastInspectionDate()).thenReturn(new Date());
        int location = 1;
        when(cargo.getStorageLocation()).thenReturn(location);
        cargoWarehouse.insert(cargo);

        EventObject eventObject = EventGenerator.createEvent("u", String.valueOf(location).split(" ")); //search exclude hazard
        String msg = this.eventHandler.handleWithMsg(eventObject);

        //test
        assertEquals("cargo at location: " + location + " updated!", msg);
    }

    @Test
    void inspectCargoInValidTest() {
        //init
        customerWarehouse.addCustomer(sampleCustomerName);
        Cargo cargo = mock(Cargo.class);
        when(cargo.getCargoType()).thenReturn("UnitisedCargo");
        when(cargo.getCustomer()).thenReturn(new CustomerImpl(sampleCustomerName));
        when(cargo.getLastInspectionDate()).thenReturn(new Date());
        int location = 1;
        when(cargo.getStorageLocation()).thenReturn(location);
        cargoWarehouse.insert(cargo);

        EventObject eventObject = EventGenerator.createEvent("u", String.valueOf(2).split(" ")); //search exclude hazard
        String msg = this.eventHandler.handleWithMsg(eventObject);

        //test
        assertEquals("Inspection failed!", msg);
    }

    @Test
    void removeCustomerValidTest() {
        customerWarehouse.addCustomer(sampleCustomerName);

        EventObject eventObject = EventGenerator.createEvent("d", sampleCustomerName.split(" ")); //search exclude hazard
        String msg = this.eventHandler.handleWithMsg(eventObject);

        assertEquals("Customer " + sampleCustomerName + " removed!", msg);

    }

    @Test
    void removeCustomerInvalidTest() {
        customerWarehouse.addCustomer(sampleCustomerName);

        EventObject eventObject = EventGenerator.createEvent("d", "test".split(" ")); //search exclude hazard
        String msg = this.eventHandler.handleWithMsg(eventObject);

        assertEquals("customer remove failed!", msg);

    }

    @Test
    void removeCargoValidTest() {
        //init
        customerWarehouse.addCustomer(sampleCustomerName);
        Cargo cargo = mock(Cargo.class);
        when(cargo.getCustomer()).thenReturn(new CustomerImpl(sampleCustomerName));
        int location = 1;
        when(cargo.getStorageLocation()).thenReturn(location);
        cargoWarehouse.insert(cargo);

        EventObject eventObject = EventGenerator.createEvent("d", String.valueOf(location).split(" ")); //search exclude hazard
        String msg = this.eventHandler.handleWithMsg(eventObject);

        //test
        assertEquals("cargo at location: " + location + " removed!", msg);
    }

    @Test
    void removeCargoInValidTest() {
        //init
        customerWarehouse.addCustomer(sampleCustomerName);
        Cargo cargo = mock(Cargo.class);
        when(cargo.getCustomer()).thenReturn(new CustomerImpl(sampleCustomerName));
        int location = 1;
        when(cargo.getStorageLocation()).thenReturn(location);
        cargoWarehouse.insert(cargo);

        EventObject eventObject = EventGenerator.createEvent("d", String.valueOf(2).split(" ")); //search exclude hazard
        String msg = this.eventHandler.handleWithMsg(eventObject);

        //test
        assertEquals("cargo remove failed!", msg);
    }

}