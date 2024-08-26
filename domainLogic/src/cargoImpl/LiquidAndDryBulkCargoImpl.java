package cargoImpl;

import administration.Customer;
import cargo.Hazard;
import cargo.LiquidAndDryBulkCargo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

public class LiquidAndDryBulkCargoImpl extends DryBulkCargoImpl implements LiquidAndDryBulkCargo, Serializable {
    private final boolean pressurized;

    public LiquidAndDryBulkCargoImpl(String cargoType, Customer owner, BigDecimal value, Collection<Hazard> hazards, boolean pressurized, int grainSize) {
        super(cargoType, owner, value, hazards, grainSize);
        this.pressurized = pressurized;
    }


    @Override
    public boolean isPressurized() {
        return pressurized;
    }

//    @Override
//    public String toString() {
//        return super.toString() + ", isPressurized: " + isPressurized();
//    }
}
