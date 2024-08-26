package cargo;

import java.io.Serializable;

public interface UnitisedCargo extends Cargo, Serializable {
    boolean isFragile();
}
