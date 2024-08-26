package manager.Warehouse;

import administration.Storable;
import cargo.Cargo;
import cargo.Hazard;
import exception.DomainLogicException;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class CargoWarehouse<T extends Cargo & Storable> implements Serializable {
    private final Map<Integer, T> storage;
    private final int storageCapacity;
    private final Boolean[] storageLocationArr;

    public CargoWarehouse(int storageCapacity) {
        if (storageCapacity <= 0) {
            throw new DomainLogicException("Capacity must be greater than 0");
        }
        this.storage = new HashMap<>(storageCapacity);
        this.storageCapacity = storageCapacity;
        this.storageLocationArr = new Boolean[storageCapacity];
        initialiseStorageLocation();
    }

    /**
     * This constructor is only for test
     */
    public CargoWarehouse() {
        this.storage = null;
        this.storageLocationArr = null;
        this.storageCapacity = 1;
    }

    public boolean inspect(int storageLocation) {
        // Error handling
        if (null == storage) throw new DomainLogicException("Storage is null");
        if (0 > storageLocation) throw new DomainLogicException("Storage location may not be less than 0");

        if (!isEmpty() && storage.containsKey(storageLocation)) {
            T cargo = storage.get(storageLocation);
            setLastInspectionDate(cargo);
            return true;
        }
        return false;
    }

    public T getCargo(int storageLocation) {
        // Error handling
        if (null == storage) throw new DomainLogicException("Storage is null");
        if (0 > storageLocation) throw new DomainLogicException("Storage location may not be less than 0");

        return storage.get(storageLocation);
    }

    boolean removeCargoUsingSL(int storageLocation) {
        // Error handling
        if (null == storage) throw new DomainLogicException("Storage is null");
        if (0 >= storageLocation) throw new DomainLogicException("Storage location may not be less than 0");

        if (!isEmpty() && storage.containsKey(storageLocation)) {
            T cargoOptional = getCargo(storageLocation);
            storage.remove(storageLocation);
            storageLocationArr[storageLocation - 1] = false; // Set status of this location free!
            return true;
        }
        return false;
    }

    void removeCargosUsingCustomerName(String customerName) {
        // Error handling
        if (customerName.equals("")) throw new DomainLogicException("Customer name may not be empty!");
        if (null == storage) throw new DomainLogicException("Storage is null");

        Iterator<Map.Entry<Integer, T>> iterator = storage.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, T> entry = iterator.next();
            T cargo = entry.getValue();
            if (cargo.getCustomer() != null && cargo.getCustomer().name().equals(customerName)) {
                iterator.remove();
            }
        }
    }

    public boolean insert(T cargo) {
        // Error handling
        if (null == cargo) throw new DomainLogicException("Cargo can't be null");
        if (null == storage) throw new DomainLogicException("Storage is null");

        // Check if any location is free and not full
        int availableLocation = getNextAvailableStorageLocation();
        if (!isFull() && availableLocation != -1) {
            cargo.setStorageLocation(availableLocation);
            this.storageLocationArr[availableLocation - 1] = true; // Set status of this location used!
            storage.put(availableLocation, cargo);
            return true;
        }
        return false;
    }

    public List<T> listAllCargos() {
        // Error handling
        if (null == storage) throw new DomainLogicException("Storage is null");

        return new ArrayList<>(storage.values());
    }

    public List<T> listCargosByType(String cargoType) {
        // Error handling
        if (cargoType == null || cargoType.isEmpty()) {
            throw new DomainLogicException("Cargo type should not be empty or null");
        }
        if (null == storage) throw new DomainLogicException("Storage is null");

        return storage.values().stream().filter(cargo -> cargoType.equals(cargo.getCargoType())).collect(Collectors.toList());
    }

    /**
     * Update last inspection date
     *
     * @param cargo Cargo to be updated
     */
    private void setLastInspectionDate(T cargo) {
        if (cargo == null) throw new DomainLogicException("Cargo may not be null");
        cargo.setLastInspectionDate();
    }

    public boolean isStorageLocationInWarehouse(int storageLocation) {
        if (0 > storageLocation) throw new DomainLogicException("Storage location may be less than 0");
        return storage.containsKey(storageLocation);
    }

    public Set<Hazard> listHazards() {
        // Init
        Set<Hazard> hazardSet = new HashSet<>();
        for (T cargo : listAllCargos()) {
            hazardSet.addAll(cargo.getHazards());
        }
        return hazardSet;
    }

    public boolean isFull() {
        return storage.size() >= storageCapacity;
    }

    public boolean isEmpty() {
        return storage.isEmpty();
    }

    // Help methods
    private void initialiseStorageLocation() {
        Arrays.fill(storageLocationArr, false);
    }

    int getNextAvailableStorageLocation() {
        for (int i = 0; i < storageCapacity; i++) {
            if (!storageLocationArr[i]) {
                return i + 1;
            }
        }
        return -1; // No available storage location
    }

    public int getStorageCapacity() {
        return storageCapacity;
    }

    public int getCurrentSize() {
        return this.storage.size();
    }

}
