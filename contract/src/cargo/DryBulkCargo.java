package cargo;

import java.io.Serializable;

public interface DryBulkCargo extends Cargo, Serializable {
    int getGrainSize();
}
