import cargo.Cargo;
import eventSystem.EventHandler;
import eventSystem.ObserverPattern.CargoObserver;
import eventSystem.ObserverPattern.Observer;
import eventSystem.eventListeners.GLEventListener;
import manager.Warehouse.CargoWarehouse;
import manager.Warehouse.CustomerWarehouse;
import manager.Warehouse.WarehouseFacade;


public class CLI {
    public static void main(String[] args) {
        //init Warehouse Facade
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        customerWarehouse.addCustomer("Alice");
        customerWarehouse.addCustomer("Bob");
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(12);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(customerWarehouse, cargoWarehouse);
        //init event handler and add listener
        EventHandler eventHandler = new EventHandler();
        eventHandler.add(new GLEventListener<>(warehouseFacade));
        //init observer
        Observer storageObserver = new CargoObserver<>(warehouseFacade);
        warehouseFacade.registerObserver(storageObserver);

        //create cli LiquidAndDryBulkCargo Alice 4004,50 flammable,toxic true 10
        new cli.CLI(eventHandler).start();

    }


}
