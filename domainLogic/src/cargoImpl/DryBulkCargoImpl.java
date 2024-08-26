package cargoImpl;

import administration.Customer;
import cargo.DryBulkCargo;
import cargo.Hazard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

public class DryBulkCargoImpl extends CargoImpl implements DryBulkCargo, Serializable {
    private final int grainSize;

    public DryBulkCargoImpl(String cargoType, Customer owner, BigDecimal value, Collection<Hazard> hazards, int grainSize) {
        super(cargoType, owner, value, hazards);
        if (grainSize <= 0) {
            throw new IllegalArgumentException("grain size may not be smaller than 0");
        }
        this.grainSize = grainSize;
    }


    @Override
    public int getGrainSize() {
        return grainSize;
    }

//    @Override
//    public String toString() {
//        return super.toString() + ", grain size: " + getGrainSize();
//    }
}
