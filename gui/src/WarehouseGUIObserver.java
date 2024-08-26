import administration.Storable;
import cargo.Cargo;
import eventSystem.ObserverPattern.Observer;
import manager.Warehouse.WarehouseFacade;

import java.io.Serializable;

public class WarehouseGUIObserver<T extends Storable & Cargo> implements Observer, Serializable {
    private final WarehouseFacade<Cargo> warehouseFacade;
    private final Controller controller;

    public WarehouseGUIObserver(WarehouseFacade<Cargo> warehouseFacade, Controller controller) {
        this.warehouseFacade = warehouseFacade;
        this.controller = controller;
    }

    @Override
    public void update() {
        controller.updateDB(this.warehouseFacade.listCargos(), this.warehouseFacade.listCustomers());
        controller.setStatus(this.warehouseFacade.getStatus());
    }
}
