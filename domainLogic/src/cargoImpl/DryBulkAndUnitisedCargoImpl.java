package cargoImpl;

import administration.Customer;
import cargo.DryBulkAndUnitisedCargo;
import cargo.Hazard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

public class DryBulkAndUnitisedCargoImpl extends UnitisedCargoImpl implements DryBulkAndUnitisedCargo, Serializable {
    private final int grainSize;

    public DryBulkAndUnitisedCargoImpl(String cargoType, Customer owner, BigDecimal value, Collection<Hazard> hazards, boolean fragile, int grainSize) {
        super(cargoType, owner, value, hazards, fragile);
        this.grainSize = grainSize;
    }


    @Override
    public int getGrainSize() {
        return grainSize;
    }


//    @Override
//    public String toString() {
//        return super.toString() + ", grainSize: " + getGrainSize();
//    }
}
