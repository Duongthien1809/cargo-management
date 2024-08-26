package manager.Warehouse;

import administration.Customer;
import cargo.Cargo;
import cargo.Hazard;
import eventSystem.ObserverPattern.Observer;
import exception.DomainLogicException;
import manager.CustomerImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WarehouseFacadeTest {
    @Test
    void insertCustomerValidEntryTest() {
        //init
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        CargoWarehouse<Cargo> cargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(customerWarehouse, cargoWarehouse);
        //Test
        assertTrue(warehouseFacade.insertCustomer("test"));
    }

    @Test
    void insertMultiCustomersValidEntryTest() {
        //init
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        CargoWarehouse<Cargo> cargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(customerWarehouse, cargoWarehouse);
        //Test
        assertTrue(warehouseFacade.insertCustomer("test"));
        assertTrue(warehouseFacade.insertCustomer("test1"));
    }

    @Test
    void insertCustomerNameAlreadyExistTest() {
        //init
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        CargoWarehouse<Cargo> cargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(customerWarehouse, cargoWarehouse);
        //Test
        assertTrue(warehouseFacade.insertCustomer("test"));
        //should false
        assertFalse(warehouseFacade.insertCustomer("test"));
    }

    @Test
    void insertCustomerEntryIsEmptyTest() {
        //init
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        CargoWarehouse<Cargo> cargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(customerWarehouse, cargoWarehouse);
        //Test
        assertThrows(DomainLogicException.class, () -> warehouseFacade.insertCustomer(""));
    }

    @Test
    void insertCustomerEntryIsNullTest() {
        //init
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        CargoWarehouse<Cargo> cargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(customerWarehouse, cargoWarehouse);
        //Test
        assertThrows(DomainLogicException.class, () -> warehouseFacade.insertCustomer(null));
    }

    @Test
    void removeCustomerValidEntryTest() {
        //init
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        CargoWarehouse<Cargo> cargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(customerWarehouse, cargoWarehouse);

        //add test to customer warehouse
        assertTrue(warehouseFacade.insertCustomer("test"));

        //test remove test from customer warehouse
        assertTrue(warehouseFacade.removeCustomer("test"));
    }

    @Test
    void removeCustomerInvalidEntryTest() {
        //init
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        CargoWarehouse<Cargo> cargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(customerWarehouse, cargoWarehouse);

        //add test to customer warehouse
        assertTrue(warehouseFacade.insertCustomer("test"));

        //test remove test from customer warehouse
        assertFalse(warehouseFacade.removeCustomer("test1"));
    }

    @Test
    void removeCustomerEntryIsEmptyTest() {
        //init
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        CargoWarehouse<Cargo> cargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(customerWarehouse, cargoWarehouse);
        //Test
        assertThrows(DomainLogicException.class, () -> warehouseFacade.removeCustomer(""));
    }

    @Test
    void removeCustomerEntryIsNullTest() {
        //init
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        CargoWarehouse<Cargo> cargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(customerWarehouse, cargoWarehouse);
        //Test
        assertThrows(DomainLogicException.class, () -> warehouseFacade.removeCustomer(null));
    }

    @Test
    void listCustomersValidTest() {
        //init set list for test
        Set<Customer> expectCustomers = new HashSet<>();
        expectCustomers.add(new CustomerImpl("test1"));
        expectCustomers.add(new CustomerImpl("test2"));
        //init
        CustomerWarehouse mockCustomerW = mock(CustomerWarehouse.class);
        when(mockCustomerW.listAllCustomers()).thenReturn(expectCustomers);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerW, mockCargoWarehouse);

        //init actual for test
        Set<Customer> actualCustomers = warehouseFacade.listCustomers();
        //test
        assertEquals(expectCustomers, actualCustomers);
        verify(mockCustomerW).listAllCustomers();//verify that methode was called

    }

    @Test
    void insertCargoValidEntryTest() {
        //init storage
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        customerWarehouse.addCustomer("test");//add new customer name test
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(customerWarehouse, cargoWarehouse);
        //init cargo to insert
        Customer mockCustomer = mock(Customer.class);
        when(mockCustomer.name()).thenReturn("test");
        Cargo mockCargo = mock(Cargo.class);
        when(mockCargo.getCustomer()).thenReturn(mockCustomer);
        when(mockCargo.getCustomer().name()).thenReturn("test");
        //test
        assertTrue(warehouseFacade.insertCargo(mockCargo));
    }

    @Test
    void insertCargoWhenCargoNameNotExistTest() {
        //init storage
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        customerWarehouse.addCustomer("test");//add new customer name test
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(customerWarehouse, cargoWarehouse);
        //init customer, cargo to insert
        Customer mockCustomer = mock(Customer.class);
        when(mockCustomer.name()).thenReturn("test1"); // test1 is not in storage
        Cargo mockCargo = mock(Cargo.class);
        when(mockCargo.getCustomer()).thenReturn(mockCustomer);

        //test
        assertFalse(warehouseFacade.insertCargo(mockCargo));
    }

    @Test
    void insertCargoWhenEntryIsNullTest() {
        //init storage
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);

        //test
        assertThrows(DomainLogicException.class, () -> warehouseFacade.insertCargo(null));
        verify(mockCargoWarehouse, never()).insert(any()); //this methode was never call
        verify(mockCustomerWarehouse, never()).isNameValidToAddCargo(anyString()); //also this was never call
    }

    @Test
    void removeCargoByLocationValidEntryTest() {
        //init storage
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);
        //prepare anything for test
        int location = 1;
        Cargo mockCargo = mock(Cargo.class);
        Customer mockCustomer = mock(Customer.class);
        when(mockCustomerWarehouse.getCustomer(any())).thenReturn(mockCustomer);
        when(mockCargo.getCustomer()).thenReturn(mockCustomer);
        when(mockCargoWarehouse.getCargo(1)).thenReturn(mockCargo);
        when(mockCargoWarehouse.removeCargoUsingSL(location)).thenReturn(true);
        //tests
        assertTrue(warehouseFacade.removeCargoByLocation(location));
        assertEquals(0, warehouseFacade.getCurrentSizeOfCargoWarehouse());

        //verify that method was call
        verify(mockCargoWarehouse).getCargo(anyInt());
        verify(mockCargo, times(1)).getCustomer();
        verify(mockCargoWarehouse).removeCargoUsingSL(location);
    }

    @Test
    void removeCargoByLocationValidEntryCustomerInstanceTrueTest() {
        //init storage
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);
        //prepare anything for test
        int location = 1;
        Cargo mockCargo = mock(Cargo.class);
        Customer mockCustomer = mock(CustomerImpl.class);
        when(mockCustomerWarehouse.getCustomer("test")).thenReturn(mockCustomer);
        when(mockCargo.getCustomer()).thenReturn(mockCustomer);
        when(mockCustomer.name()).thenReturn("test");
        when(mockCargoWarehouse.getCargo(1)).thenReturn(mockCargo);
        when(mockCargoWarehouse.removeCargoUsingSL(location)).thenReturn(true);
        when(mockCustomerWarehouse.isInstanceOfCustomerImpl(mockCustomer)).thenReturn(true);
        //tests
        assertTrue(warehouseFacade.removeCargoByLocation(location));
        assertEquals(0, warehouseFacade.getCurrentSizeOfCargoWarehouse());

        //verify that method was call
        verify(mockCargoWarehouse).getCargo(anyInt());
        verify(mockCargo).getCustomer();
        verify(mockCargoWarehouse).removeCargoUsingSL(location);
    }

    @Test
    void removeCargoByLocationValidEntryReturnFalseTest() {
        //init storage
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);
        //prepare anything for test
        int location = 1;
        Cargo mockCargo = mock(Cargo.class);
        Customer mockCustomer = mock(Customer.class);
        when(mockCustomerWarehouse.getCustomer(any())).thenReturn(mockCustomer);
        when(mockCargo.getCustomer()).thenReturn(mockCustomer);
        when(mockCargoWarehouse.getCargo(1)).thenReturn(mockCargo);
        when(mockCargoWarehouse.removeCargoUsingSL(location)).thenReturn(false);
        //tests
        assertFalse(warehouseFacade.removeCargoByLocation(location));
        assertEquals(0, warehouseFacade.getCurrentSizeOfCargoWarehouse());

        //verify that method were call
        verify(mockCargoWarehouse).removeCargoUsingSL(location);
        verify(mockCustomerWarehouse, never()).getCustomer(anyString());
    }

    @Test
    void removeCargoByLocationInvalidEntryTest() {
        //init storage
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);
        //prepare anything for test
        int location = -1;
        assertThrows(DomainLogicException.class, () -> warehouseFacade.removeCargoByLocation(location));
        //verify that method was never call
        verify(mockCargoWarehouse, never()).removeCargoUsingSL(location);
    }

    @Test
    void ListCargosByTypeValidEntryTest() {
        //init storage and cargos
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);
        Cargo mockCargo1 = mock(Cargo.class);
        Cargo mockCargo2 = mock(Cargo.class);
        //set value for mock storage
        List<Cargo> cargoList = Arrays.asList(mockCargo1, mockCargo2);
        String cargoType = "testCargoType";
        when(warehouseFacade.listCargosByType(cargoType)).thenReturn(cargoList);
        //tests
        assertEquals(cargoList, warehouseFacade.listCargosByType(cargoType));
        //verify that method was call
        verify(mockCargoWarehouse).listCargosByType(cargoType);
    }

    @Test
    void ListCargosByTypeEmptyEntryTest() {
        //init storage and cargos
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);
        String cargoType = "";
        //tests
        assertThrows(DomainLogicException.class, () -> warehouseFacade.listCargosByType(cargoType));
        //verify that method was call
        verify(mockCargoWarehouse, never()).listCargosByType(cargoType);
    }

    @Test
    void ListCargosByTypeEntryIsNullTest() {
        //init storage and cargos
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);
        String cargoType = null;
        //tests
        assertThrows(DomainLogicException.class, () -> warehouseFacade.listCargosByType(cargoType));
        //verify that method was call
        verify(mockCargoWarehouse, never()).listCargosByType(cargoType);
    }

    @Test
    void listCargosValidTest() {
        //init storage and cargos
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);
        Cargo mockCargo1 = mock(Cargo.class);
        Cargo mockCargo2 = mock(Cargo.class);
        //set value for mock storage
        List<Cargo> cargoList = Arrays.asList(mockCargo1, mockCargo2);
        when(warehouseFacade.listCargos()).thenReturn(cargoList);
        //test
        assertEquals(cargoList, warehouseFacade.listCargos());
        //verify that method was call
        verify(mockCargoWarehouse).listAllCargos();
    }

    @Test
    void updateCargoByStorageLocationValidEntryTest() {
        //init storage and cargos
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);
        Cargo mockCargo1 = mock(Cargo.class);

        // Set up the date for testing
        Date dateToTest = new Date();
        when(mockCargo1.getLastInspectionDate()).thenReturn(dateToTest);

        // Set value
        int location = 1;
        when(warehouseFacade.updateCargoByStorageLocation(location)).thenReturn(true);
        when(mockCargoWarehouse.getCargo(location)).thenReturn(mockCargo1);

        // Test
        assertTrue(warehouseFacade.updateCargoByStorageLocation(location));

        // Check that the update was called
        verify(mockCargoWarehouse).inspect(location);

        // Check that the date is updated
        assertEquals(dateToTest, mockCargo1.getLastInspectionDate());
    }

    @Test
    void updateCargoByStorageLocationCargoNotUpdatedTest() {
        //init storage and cargos
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);
        Cargo mockCargo1 = mock(Cargo.class);

        // Set up the date for testing
        Date dateToTest = new Date();

        // Set value
        int location = 1;
        when(warehouseFacade.updateCargoByStorageLocation(location)).thenReturn(false);
        when(mockCargoWarehouse.getCargo(location)).thenReturn(mockCargo1);

        // Test
        assertFalse(warehouseFacade.updateCargoByStorageLocation(location));

        // Check that the update was called
        verify(mockCargoWarehouse).inspect(location);

        // Check that the date is updated
        assertNotEquals(dateToTest, mockCargo1.getLastInspectionDate());
    }

    @Test
    void updateCargoByStorageLocationInvalidEntryTest() {
        //init storage and cargos
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);
        Cargo mockCargo1 = mock(Cargo.class);
        //Set value
        int location = -1;
        assertThrows(DomainLogicException.class, () -> warehouseFacade.updateCargoByStorageLocation(location));
        //Check that method was never call
        verify(mockCargoWarehouse, never()).inspect(location);
    }

    @Test
    void includeHazardValidTest() {
        //init storage and cargos
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);
        //mock Hazards
        // Mock Hazards
        Set<Hazard> mockHazards = new HashSet<>(Arrays.asList(Hazard.explosive, Hazard.flammable, Hazard.toxic));
        when(mockCargoWarehouse.listHazards()).thenReturn(mockHazards);
        //test
        assertEquals(new HashSet<>(warehouseFacade.includeHazard()), mockHazards);
        //verify that method was call
        verify(mockCargoWarehouse).listHazards();
    }

    @Test
    void excludeHazardValidTest() {
        //init storage and cargos
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);

        // Mock includeHazard method
        Set<Hazard> mockIncludedHazards = new HashSet<>(Arrays.asList(Hazard.explosive, Hazard.flammable));
        when(mockCargoWarehouse.listHazards()).thenReturn(mockIncludedHazards);

        // Test
        List<Hazard> excludedHazards = warehouseFacade.excludeHazard();

        // Expected result: [Hazard.toxic, Hazard.radioactive]
        assertEquals(Arrays.asList(Hazard.toxic, Hazard.radioactive), excludedHazards);
    }

    @Test
    void getCargoCountUsingCustomerNameValidTest() {
        // Initialize necessary objects
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);

        // Mock the listAllCargos method
        Cargo mockCargo1 = mock(Cargo.class);
        when(mockCargo1.getCustomer()).thenReturn(new CustomerImpl("customer1"));

        Cargo mockCargo2 = mock(Cargo.class);
        when(mockCargo2.getCustomer()).thenReturn(new CustomerImpl("customer2"));

        when(mockCargoWarehouse.listAllCargos()).thenReturn(List.of(mockCargo1, mockCargo2));

        // Test the method
        assertEquals(1, warehouseFacade.getCargoCountUsingCustomerName("customer1"));
    }

    @Test
    void getCargoCountUsingCustomerNameEmptyNameTest() {
        // Initialize necessary objects
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);

        // Test the method with an empty customer name
        assertThrows(DomainLogicException.class, () -> warehouseFacade.getCargoCountUsingCustomerName(""));
    }

    @Test
    void getCargoCountUsingCustomerNameNullNameTest() {
        // Initialize necessary objects
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);

        // Test the method with a null customer name
        assertThrows(DomainLogicException.class, () -> warehouseFacade.getCargoCountUsingCustomerName(null));
    }

    @Test
    void isEmptyIsTrueTest() {
        // Initialize necessary objects
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);
        when(mockCargoWarehouse.isEmpty()).thenReturn(true);
        //test
        assertTrue(warehouseFacade.isEmpty());
        //check method was call
        verify(mockCargoWarehouse).isEmpty();
    }

    @Test
    void isEmptyIsFalseTest() {
        // Initialize necessary objects
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);
        when(mockCargoWarehouse.isEmpty()).thenReturn(false);
        //test
        assertFalse(warehouseFacade.isEmpty());
        //check method was call
        verify(mockCargoWarehouse).isEmpty();
    }

    @Test
    void isFullIsTrueTest() {
        // Initialize necessary objects
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);
        when(mockCargoWarehouse.isFull()).thenReturn(true);
        //test
        assertTrue(warehouseFacade.isFull());
        //check method was call
        verify(mockCargoWarehouse).isFull();
    }

    @Test
    void isFullIsFalseTest() {
        // Initialize necessary objects
        CustomerWarehouse mockCustomerWarehouse = mock(CustomerWarehouse.class);
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mockCustomerWarehouse, mockCargoWarehouse);
        when(mockCargoWarehouse.isFull()).thenReturn(false);
        //test
        assertFalse(warehouseFacade.isFull());
        //check method was call
        verify(mockCargoWarehouse).isFull();
    }

    @Test
    void testCalculateCurrentCapacityPercentage() {
        // init storage
        int storageCapacity = 1000;
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(storageCapacity);
        CustomerWarehouse customerWarehouse = mock(CustomerWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(customerWarehouse, cargoWarehouse);

        // act
        double expectedPercentage = (double) warehouseFacade.getCurrentSizeOfCargoWarehouse() / storageCapacity * 100;
        // tests
        assertEquals(expectedPercentage, warehouseFacade.calculateCurrentCapacityPercentage(), 0.001);
    }

    @Test
    void testRegisterObserver() {
        // init
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mock(CustomerWarehouse.class), mock(CargoWarehouse.class));
        Observer mockObserver = Mockito.mock(Observer.class);

        // Act
        warehouseFacade.registerObserver(mockObserver);

        // test
        assertTrue(warehouseFacade.getObservers().contains(mockObserver));
    }

    @Test
    void testRemoveObserver() {
        // init
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mock(CustomerWarehouse.class), mock(CargoWarehouse.class));
        Observer mockObserver = Mockito.mock(Observer.class);

        // Act
        warehouseFacade.removeObserver(mockObserver);

        // test
        assertFalse(warehouseFacade.getObservers().contains(mockObserver));
    }

    @Test
    void testObserverNotify() {
        // init
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mock(CustomerWarehouse.class), mock(CargoWarehouse.class));
        Observer mockObserver = Mockito.mock(Observer.class);
        warehouseFacade.registerObserver(mockObserver);
        String status = "Some status";

        // Act
        warehouseFacade.observerNotify(status);

        // test
        Mockito.verify(mockObserver, Mockito.times(1)).update();
        assertEquals(status, warehouseFacade.getStatus());
    }

    @Test
    void testGetCurrentSizeOfCargoWarehouse() {
        // init
        CargoWarehouse<Cargo> mockCargoWarehouse = mock(CargoWarehouse.class);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mock(CustomerWarehouse.class), mockCargoWarehouse);
        Cargo mockCargo1 = Mockito.mock(Cargo.class);
        Cargo mockCargo2 = Mockito.mock(Cargo.class);
        List<Cargo> mockCargos = List.of(mockCargo1, mockCargo2);
        //set value
        when(mockCargoWarehouse.listAllCargos()).thenReturn(mockCargos);
        when(mockCargoWarehouse.getCurrentSize()).thenReturn(mockCargos.size());
        // test
        assertEquals(mockCargos.size(), warehouseFacade.getCurrentSizeOfCargoWarehouse());
    }

    @Test
    public void replaceContents_WhenGivenLocalWarehouse_ShouldReplaceContents() {
        // Arrange
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(new CustomerWarehouse(), new CargoWarehouse<>(3));
        CustomerWarehouse localCustomerWarehouse = new CustomerWarehouse();
        CargoWarehouse<Cargo> localCargoWarehouse = new CargoWarehouse<>(20);

        WarehouseFacade<Cargo> localWarehouse = new WarehouseFacade<>(localCustomerWarehouse, localCargoWarehouse);

        // Act
        warehouseFacade.replaceContents(localWarehouse);

        // Assert
        assertEquals(localCustomerWarehouse, warehouseFacade.getCustomerWarehouse());
        assertEquals(localCargoWarehouse, warehouseFacade.getCargoWarehouse());
    }

    @Test
    public void getCargoWarehouse_ShouldReturnCorrectCargoWarehouse() {
        // Arrange
        CargoWarehouse<Cargo> expectedCargoWarehouse = new CargoWarehouse<>(1);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(mock(CustomerWarehouse.class), expectedCargoWarehouse);

        // Act
        CargoWarehouse<Cargo> result = warehouseFacade.getCargoWarehouse();

        // Assert
        assertEquals(expectedCargoWarehouse, result);
    }

    @Test
    public void getCustomerWarehouse_ShouldReturnCorrectCustomerWarehouse() {
        // Arrange
        CustomerWarehouse expectedCustomerWarehouse = new CustomerWarehouse();
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(expectedCustomerWarehouse, mock(CargoWarehouse.class));

        // Act
        CustomerWarehouse result = warehouseFacade.getCustomerWarehouse();

        // Assert
        assertEquals(expectedCustomerWarehouse, result);
    }

    @Test
    public void constructorTest() {
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>();

        assertEquals(1000, warehouseFacade.getCargoWarehouse().getStorageCapacity());
        assertNotNull(warehouseFacade.getCargoWarehouse());
        assertNotNull(warehouseFacade.getCustomerWarehouse());
    }

}