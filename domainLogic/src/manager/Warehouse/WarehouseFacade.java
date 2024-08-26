package manager.Warehouse;

import administration.Customer;
import administration.Storable;
import cargo.Cargo;
import cargo.Hazard;
import eventSystem.ObserverPattern.Observable;
import eventSystem.ObserverPattern.Observer;
import exception.DomainLogicException;
import manager.CustomerImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WarehouseFacade<T extends Cargo & Storable> implements Serializable, Observable {
    private CustomerWarehouse customerWarehouse;
    private CargoWarehouse<T> cargoWarehouse;
    private final List<Observer> observers = new ArrayList<>();
    transient String status;
    private final Lock warehouseLock = new ReentrantLock();

    public WarehouseFacade(CustomerWarehouse customerWarehouse, CargoWarehouse<T> cargoWarehouse) {
        this.customerWarehouse = customerWarehouse;
        this.cargoWarehouse = cargoWarehouse;
    }
    //normal constructor for test
    public WarehouseFacade(){
        this.customerWarehouse = new CustomerWarehouse();
        this.cargoWarehouse = new CargoWarehouse<>(1000);
    }

    /* Methods for Customer Warehouse */
    /* ----------------------------- */

    public boolean insertCustomer(String name) {
        synchronized (warehouseLock) {
            if (customerWarehouse.addCustomer(name)) {
                this.observerNotify("customer insert");
                return true;
            }
        }
        return false;
    }

    public boolean removeCustomer(String name) {
        synchronized (warehouseLock) {
            boolean customerRemoved = customerWarehouse.removeCustomer(name);
            if (customerRemoved) {
                cargoWarehouse.removeCargosUsingCustomerName(name);
                this.observerNotify("customer remove");
                return true;
            }
        }
        return false;
    }

    public Set<Customer> listCustomers() {
        synchronized (warehouseLock) {
            return customerWarehouse.listAllCustomers();
        }
    }

    /* Methods for Cargo Warehouse */
    /* -------------------------- */

    public boolean insertCargo(T cargo) {
        synchronized (warehouseLock) {
            if (null == cargo) throw new DomainLogicException("cargo is null");
            String customerName = cargo.getCustomer().name();
            boolean validCustomerName = customerWarehouse.isNameValidToAddCargo(customerName);
            if (validCustomerName) {
                if (cargoWarehouse.insert(cargo)) {
                    if (this.customerWarehouse.getCustomer(customerName) instanceof CustomerImpl) {
                        ((CustomerImpl) this.customerWarehouse.getCustomer(customerName)).increment();
                    }
                    this.observerNotify("cargo insert");
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeCargoByLocation(int location) {
        synchronized (warehouseLock) {
            if (location < 0) {
                throw new DomainLogicException("location may not be less than 0");
            }
            T cargo = cargoWarehouse.getCargo(location);
            if (cargo != null) {
                String customerName = this.customerWarehouse.getCustomer(cargo.getCustomer().name()).name();
                if (cargoWarehouse.removeCargoUsingSL(location)) {
                    if (this.customerWarehouse.getCustomer(customerName) instanceof CustomerImpl) {
                        ((CustomerImpl) this.customerWarehouse.getCustomer(customerName)).decrement();
                    }
                    this.observerNotify("cargo remove");
                    return true;
                }
            }
        }
        return false;
    }

    public List<T> listCargosByType(String cargoType) {
        synchronized (warehouseLock) {
            if (null == cargoType || cargoType.isEmpty()) {
                throw new DomainLogicException("Name couldn't be empty or null!");
            }
            return new ArrayList<>(cargoWarehouse.listCargosByType(cargoType));
        }
    }

    public List<T> listCargos() {
        synchronized (warehouseLock) {
            return new ArrayList<>(cargoWarehouse.listAllCargos());
        }
    }

    public boolean updateCargoByStorageLocation(int storageLocation) {
        synchronized (warehouseLock) {
            if (0 > storageLocation) throw new DomainLogicException("location may not less than 0");
            if (cargoWarehouse.inspect(storageLocation)) {
                this.observerNotify("cargo update");
                return true;
            }
        }
        return false;
    }

    public List<Hazard> includeHazard() {
        synchronized (warehouseLock) {
            return new ArrayList<>(cargoWarehouse.listHazards());
        }
    }

    public List<Hazard> excludeHazard() {
        synchronized (warehouseLock) {
            List<Hazard> includeHazards = new ArrayList<>(cargoWarehouse.listHazards());
            List<Hazard> allHazards = new ArrayList<>(Arrays.asList(Hazard.explosive, Hazard.flammable, Hazard.toxic, Hazard.radioactive));
            for (Hazard hazard : includeHazards) {
                allHazards.remove(hazard);
            }
            return allHazards;
        }
    }

    public int getCargoCountUsingCustomerName(String customerName) {
        synchronized (warehouseLock) {
            if (null == customerName || customerName.isEmpty()) {
                throw new DomainLogicException("Customer name should not be empty or null");
            }
            int count = 0;
            for (Cargo cargo : cargoWarehouse.listAllCargos()) {
                if (cargo.getCustomer().name().equals(customerName)) {
                    count++;
                }
            }
            return count;
        }
    }

    public boolean isEmpty() {
        synchronized (warehouseLock) {
            return cargoWarehouse.isEmpty();
        }
    }

    public boolean isFull() {
        synchronized (warehouseLock) {
            return cargoWarehouse.isFull();
        }
    }

    public double calculateCurrentCapacityPercentage() {
        synchronized (warehouseLock) {
            return (double) this.getCurrentSizeOfCargoWarehouse() / this.cargoWarehouse.getStorageCapacity() * 100;
        }
    }

    @Override
    public void registerObserver(Observer observer) {
        synchronized (warehouseLock) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void observerNotify(String status) {
        this.status = status;
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public String getStatus() {
        return this.status;
    }

    public int getCurrentSizeOfCargoWarehouse() {
        synchronized (warehouseLock) {
            return this.cargoWarehouse.getCurrentSize();
        }
    }

    public List<Observer> getObservers() {
        return observers;

    }

    public void replaceContents(WarehouseFacade<T> localWarehouse) {
        synchronized (warehouseLock) {
            this.customerWarehouse = localWarehouse.getCustomerWarehouse();
            this.cargoWarehouse = localWarehouse.getCargoWarehouse();
        }
    }

    public CargoWarehouse<T> getCargoWarehouse() {
        return cargoWarehouse;
    }

    public CustomerWarehouse getCustomerWarehouse() {
        return customerWarehouse;
    }
}
