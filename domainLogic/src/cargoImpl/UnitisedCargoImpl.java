package cargoImpl;

import administration.Customer;
import cargo.Hazard;
import cargo.UnitisedCargo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

public class UnitisedCargoImpl extends CargoImpl implements UnitisedCargo, Serializable {
    private final boolean fragile;

    public UnitisedCargoImpl(String cargoType, Customer owner, BigDecimal value, Collection<Hazard> hazards, boolean fragile) {
        super(cargoType, owner, value, hazards);
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
