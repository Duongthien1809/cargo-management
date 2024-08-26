package manager.Warehouse;

import cargo.Cargo;
import cargo.Hazard;
import cargoImpl.CargoImpl;
import cargoImpl.DryBulkCargoImpl;
import exception.DomainLogicException;
import manager.CustomerImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CargoWarehouseTest {

    @Test
    void storageCapacityLessThanZero() {
        assertThrows(DomainLogicException.class, () -> new CargoWarehouse<>(-1));
    }

    @Test
    void insertCargoValidTest() {
        //init
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);
        Cargo mockCargo = mock(Cargo.class);

        assertTrue(cargoWarehouse.insert(mockCargo));
    }

    @Test
    void insertNullTest() {
        //init
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);

        assertThrows(DomainLogicException.class, () -> cargoWarehouse.insert(null));
    }

    @Test
    void insertDryBulkCargoValidTest() {
        //init
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);
        //test insert with DryBulkCargo
        Cargo mockCargo = mock(DryBulkCargoImpl.class);
        when(mockCargo.getCargoType()).thenReturn("drybulkcargo");

        assertEquals(mockCargo.getCargoType(), "drybulkcargo");
        assertTrue(cargoWarehouse.insert(mockCargo));
    }

    @Test
    void insertCargoWithEmptyListTest() {
        //init
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);
        Cargo mockCargo = mock(Cargo.class);

        //test
        assertTrue(cargoWarehouse.isEmpty());
        assertTrue(cargoWarehouse.insert(mockCargo));
    }

    @Test
    void insertCargoMockSuccessTest() {
        //init
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);
        Cargo mockCargo = mock(Cargo.class);
        when(mockCargo.getCargoType()).thenReturn("testCargoType");

        boolean success = cargoWarehouse.insert(mockCargo);

        assertTrue(success);
        assertTrue(cargoWarehouse.isStorageLocationInWarehouse(1));
    }

    // spy Test
    @Test
    void insertCargoSpyTest() {
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        // Create a CargoWarehouse instance using spy
        CargoWarehouse<Cargo> cargoWarehouse = spy(new CargoWarehouse<>(2));

        // Create a Cargo instance
        Cargo cargo = mock(Cargo.class);

        // Perform the insertion
        cargoWarehouse.insert(cargo);

        // Verify that setStorageLocation method was called on the Cargo instance
        verify(cargo).setStorageLocation(captor.capture());

        // Get the captured storage location
        int storageLocation = captor.getValue();

        // Assert the captured storage location matches the expected value
        assertEquals(1, storageLocation);
    }

    @Test
    void insertCargoFullWarehouseTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);
        Cargo cargo1 = mock(Cargo.class);
        Cargo newCargo = mock(Cargo.class);
        // Insert cargo to fill the warehouse capacity
        cargoWarehouse.insert(cargo1);
        boolean success = cargoWarehouse.insert(newCargo);

        assertFalse(success);
        verify(newCargo, never()).setStorageLocation(anyInt()); // Ensure setStorageLocation is never called
    }

    @Test
    void insertCargoNotEnoughSpaceTest() {
        CargoWarehouse<Cargo> cargoWarehouse = mock(CargoWarehouse.class);
        Cargo newCargo = mock(Cargo.class);
        // Simulate a full warehouse
        doReturn(-1).when(cargoWarehouse).getNextAvailableStorageLocation();

        assertFalse(cargoWarehouse.insert(newCargo));
        verify(newCargo, never()).setStorageLocation(anyInt()); // Verify setStorageLocation is not called
    }

    @Test
    void insertWhenStorageIsNullTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>();

        Cargo mockCargo = mock(CargoImpl.class);

        assertThrows(DomainLogicException.class, () -> cargoWarehouse.insert(mockCargo));
    }

    @Test
    void listAllHazardTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(2);
        Collection<Hazard> hazards = Arrays.asList(Hazard.explosive, Hazard.toxic);
        Cargo mockCargo = mock(Cargo.class);
        when(mockCargo.getHazards()).thenReturn(hazards);
        cargoWarehouse.insert(mockCargo);

        assertEquals(new ArrayList<>(cargoWarehouse.listHazards()).size(), hazards.size());
        assertTrue(cargoWarehouse.listHazards().containsAll(hazards));
    }

    @Test
    void listAllHazardEmptyListTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(2);
        Collection<Hazard> hazards = new ArrayList<>();
        Cargo mockCargo = mock(Cargo.class);
        when(mockCargo.getHazards()).thenReturn(hazards);
        cargoWarehouse.insert(mockCargo);

        assertEquals(0, cargoWarehouse.listHazards().size());
    }

    @Test
    void listAllHazardDuplicateValueTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(2);
        Collection<Hazard> hazards = Arrays.asList(Hazard.explosive, Hazard.toxic, Hazard.explosive);
        Cargo mockCargo = mock(Cargo.class);
        when(mockCargo.getHazards()).thenReturn(hazards);
        cargoWarehouse.insert(mockCargo);

        assertEquals(new ArrayList<>(cargoWarehouse.listHazards()).size(), Arrays.asList(Hazard.explosive, Hazard.toxic).size());
        assertTrue(cargoWarehouse.listHazards().containsAll(hazards));
    }

    @Test
    void inspectEntryValidTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);
        Cargo mockCargo = mock(CargoImpl.class);
        cargoWarehouse.insert(mockCargo);

        assertTrue(cargoWarehouse.inspect(1));
    }

    @Test
    void inspectEntryLargeThanStorageCapacityTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);
        Cargo mockCargo = mock(CargoImpl.class);
        cargoWarehouse.insert(mockCargo);

        assertFalse(cargoWarehouse.inspect(2));
    }

    @Test
    void inspectEntryNegativTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);
        Cargo mockCargo = mock(CargoImpl.class);
        cargoWarehouse.insert(mockCargo);

        assertThrows(DomainLogicException.class, () -> cargoWarehouse.inspect(-1));
    }

    @Test
    void inspectWhenStorageIsNullTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>();

        assertThrows(DomainLogicException.class, () -> cargoWarehouse.inspect(1));
    }

    @Test
    void inspectWhenStorageEmptyTest() {
        CargoWarehouse<Cargo> cargoWarehouse = mock(CargoWarehouse.class);
        when(cargoWarehouse.isEmpty()).thenReturn(true);

        assertFalse(cargoWarehouse.inspect(1));
    }

    @Test
    void inspectEntryNotStoredAnyCargoTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);

        assertFalse(cargoWarehouse.inspect(1));
    }

    @Test
    void ListAllCargosSuccessTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);
        //insert cargo to warehouse
        Cargo mockCargo = mock(Cargo.class);
        cargoWarehouse.insert(mockCargo);

        assertEquals(1, cargoWarehouse.listAllCargos().size());
    }

    @Test
    void ListAllCargosStorageIsNullTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>();

        assertThrows(DomainLogicException.class, cargoWarehouse::listAllCargos);
    }

    @Test
    void ListAllCargosWhenStorageEmptyTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);

        assertEquals(0, cargoWarehouse.listAllCargos().size());
    }

    @Test
    void getCargoEntryValidTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);
        Cargo mockCargo = mock(Cargo.class);

        cargoWarehouse.insert(mockCargo);

        assertEquals(mockCargo, cargoWarehouse.getCargo(1));
    }

    @Test
    void getCargoEntryNegativTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);

        assertThrows(DomainLogicException.class, () -> cargoWarehouse.getCargo(-1));
    }

    @Test
    void getCargoWhenStorageIsNullTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>();

        assertThrows(DomainLogicException.class, () -> cargoWarehouse.getCargo(1));
    }

    @Test
    void removeCargoUsingSLEntryValidTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);
        Cargo mockCargo = mock(Cargo.class);
        cargoWarehouse.insert(mockCargo);

        assertTrue(cargoWarehouse.removeCargoUsingSL(1));
    }

    @Test
    void removeCargoUsingSLEntryLargeThanStorageCapacityTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);
        //storage capacity = 1
        assertFalse(cargoWarehouse.removeCargoUsingSL(2));
    }

    @Test
    void removeCargoUsingSLEntryNegativTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);

        assertThrows(DomainLogicException.class, () -> cargoWarehouse.removeCargoUsingSL(-1));
    }

    @Test
    void removeCargoUsingSLWhenStorageEmptyTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);

        assertFalse(cargoWarehouse.removeCargoUsingSL(1));
    }

    @Test
    void removeCargoUsingSLWhenStorageIsNullTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>();

        assertThrows(DomainLogicException.class, () -> cargoWarehouse.removeCargoUsingSL(1));
    }

    @Test
    void removeCargoUsingCustomerNameEntryValidTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);
        Cargo mockCargo = mock(Cargo.class);
        when(mockCargo.getCustomer()).thenReturn(new CustomerImpl("test"));
        //insert cargo
        cargoWarehouse.insert(mockCargo);
        //remove cargo using name test
        cargoWarehouse.removeCargosUsingCustomerName("test");

        // Verify that only cargos with the customer name "test" are removed
        assertNull(cargoWarehouse.getCargo(1));
    }

    @Test
    void removeCargoUsingCustomerNameEntryEmptyTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);

        assertThrows(DomainLogicException.class, () -> cargoWarehouse.removeCargosUsingCustomerName(""));
    }

    @Test
    void removeCargoUsingCustomerNameWhenStorageIsNullTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>();

        assertThrows(DomainLogicException.class, () -> cargoWarehouse.removeCargosUsingCustomerName("test"));
    }

    @Test
    void removeCargoUsingCustomerNameEntryNotFoundTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);
        Cargo mockCargo = mock(Cargo.class);
        when(mockCargo.getCustomer()).thenReturn(new CustomerImpl("test"));

        //insert cargo
        cargoWarehouse.insert(mockCargo);
        //remove cargo with not stored name in storage
        cargoWarehouse.removeCargosUsingCustomerName("test1");
        //verify that no cargo is removed
        assertNotNull(cargoWarehouse.getCargo(1));
    }

    @Test
    void listCargosByTypeValidTypeTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);
        Cargo cargo = mock(Cargo.class);
        when(cargo.getCargoType()).thenReturn("cargo");

        //insert to Cargo list
        assertTrue(cargoWarehouse.insert(cargo));

        //test
        assertEquals(1, cargoWarehouse.listCargosByType("cargo").size());
    }

    @Test
    void listCargosByMultiCargosInWarehouseValidTypeTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(2);
        Cargo cargo = mock(Cargo.class);
        when(cargo.getCargoType()).thenReturn("cargo");
        Cargo cargo1 = mock(Cargo.class);
        when(cargo1.getCargoType()).thenReturn("cargo1");
        //insert to Cargo list
        assertTrue(cargoWarehouse.insert(cargo));
        assertTrue(cargoWarehouse.insert(cargo1));

        //test
        assertEquals(1, cargoWarehouse.listCargosByType("cargo").size());
    }

    @Test
    void listCargosByTypeInvalidTypeTest() {
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(1);
        Cargo cargo = mock(Cargo.class);
        when(cargo.getCargoType()).thenReturn("cargo");

        //insert to Cargo list
        assertTrue(cargoWarehouse.insert(cargo));

        //test
        assertEquals(0, cargoWarehouse.listCargosByType("test").size());
    }

    @Test
    void listCargosByTypeEmptyEntryTest() {
        assertThrows(DomainLogicException.class, () -> new CargoWarehouse<Cargo>().listCargosByType(""));
    }

    @Test
    void listCargosByTypeEmptyIsNullTest() {
        assertThrows(DomainLogicException.class, () -> new CargoWarehouse<Cargo>().listCargosByType(null));
    }

    @Test
    void listCargosByTypeWhenStorageIsNullTest() {
        //init cargo warehouse with null storage
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>();

        assertThrows(DomainLogicException.class, () -> cargoWarehouse.listCargosByType("cargo"));
    }

    @Test
    void getStorageCapacityTest() {
        //init cargo warehouse with storage capacity = 2
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(2);

        //test
        assertEquals(2, cargoWarehouse.getStorageCapacity());
    }
}