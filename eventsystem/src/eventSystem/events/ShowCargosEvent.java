package eventSystem.events;

import java.util.EventObject;

public class ShowCargosEvent extends EventObject {
    private final String cargoType;

    public ShowCargosEvent(String cargoType) {
        super(cargoType);
        this.cargoType = cargoType;
    }

    public String getCargoType() {
        return cargoType;
    }
}
