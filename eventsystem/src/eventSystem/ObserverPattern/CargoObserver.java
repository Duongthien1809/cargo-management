package eventSystem.ObserverPattern;


import administration.Storable;
import cargo.Cargo;
import manager.Warehouse.WarehouseFacade;

import java.io.Serializable;

public class CargoObserver<T extends Cargo & Storable> implements Observer, Serializable {
    private final WarehouseFacade<T> warehouseFacade;

    public CargoObserver(WarehouseFacade<T> warehouseFacade) {
        this.warehouseFacade = warehouseFacade;
    }

    @Override
    public void update() {
        System.out.printf("%n%s%n", this.warehouseFacade.getStatus());
    }

}
