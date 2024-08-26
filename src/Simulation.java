import cargo.Cargo;
import eventSystem.EventHandler;
import eventSystem.ObserverPattern.CargoObserver;
import eventSystem.ObserverPattern.Observer;
import eventSystem.eventListeners.GLEventListener;
import manager.Warehouse.CargoWarehouse;
import manager.Warehouse.CustomerWarehouse;
import manager.Warehouse.WarehouseFacade;

public class Simulation {
    public static void main(String[] args) {
        //init Warehouse Facade
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        customerWarehouse.addCustomer("Alice");
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(100);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(customerWarehouse, cargoWarehouse);
        //init event handler and add listener
        EventHandler eventHandler = new EventHandler();
        eventHandler.add(new GLEventListener<>(warehouseFacade));
        //init observer
        Observer storageObserver = new CargoObserver<>(warehouseFacade);
        warehouseFacade.registerObserver(storageObserver);

        //sim1 start
        //sim1(warehouseFacade, "Alice", eventHandler);
        //sim2 start with 4 insert and 4 delete Thread
//        sim2(warehouseFacade, "Alice", eventHandler, 4);
        sim3(warehouseFacade, "Alice", eventHandler, 1000, 3);

    }

    private static void sim1(WarehouseFacade<Cargo> warehouseFacade, String name, EventHandler eventHandler) {
        new Sim1<>(warehouseFacade, eventHandler, name);
    }

    private static void sim2(WarehouseFacade<Cargo> warehouseFacade, String name, EventHandler eventHandler, int numThreads) {
        new Sim2<>(warehouseFacade, eventHandler, name, numThreads);
    }

    private static void sim3(WarehouseFacade<Cargo> warehouseFacade, String name, EventHandler eventHandler, int capacity, int counts) {
        new Sim3<>(warehouseFacade, eventHandler, name, capacity, counts);
    }
}
