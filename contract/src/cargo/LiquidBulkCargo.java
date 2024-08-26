package cargo;

import java.io.Serializable;

public interface LiquidBulkCargo extends Cargo, Serializable {
    boolean isPressurized();
}
