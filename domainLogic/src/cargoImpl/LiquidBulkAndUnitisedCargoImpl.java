package cargoImpl;

import administration.Customer;
import cargo.Hazard;
import cargo.LiquidBulkAndUnitisedCargo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

public class LiquidBulkAndUnitisedCargoImpl extends LiquidBulkCargoImpl implements LiquidBulkAndUnitisedCargo, Serializable {
    private final boolean fragile;

    public LiquidBulkAndUnitisedCargoImpl(String cargoType, Customer owner, BigDecimal value, Collection<Hazard> hazards, boolean fragile, boolean pressurized) {
        super(cargoType, owner, value, hazards, pressurized);
        this.fragile = fragile;
    }

    @Override
    public boolean isFragile() {
        return fragile;
    }


//    @Override
//    public String toString() {
//        return super.toString() + ", isFragile: " + isFragile();
//    }
}
