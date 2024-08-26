package eventSystem.events;

import administration.Storable;
import cargo.Cargo;

import java.io.Serializable;
import java.util.EventObject;

public class CargoCreateEvent<T extends Cargo & Storable> extends EventObject implements Serializable {
    private final T cargo;

    public CargoCreateEvent(T cargo) {
        super(cargo);
        this.cargo = cargo;
    }

    public T getCargo() {
        return cargo;
    }

}
