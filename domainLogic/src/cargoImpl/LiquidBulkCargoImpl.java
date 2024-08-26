package cargoImpl;

import administration.Customer;
import cargo.Hazard;
import cargo.LiquidBulkCargo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

public class LiquidBulkCargoImpl extends CargoImpl implements LiquidBulkCargo, Serializable {
    private final boolean pressurized;

    public LiquidBulkCargoImpl(String cargoType, Customer owner, BigDecimal value, Collection<Hazard> hazards, boolean pressurized) {
        super(cargoType, owner, value, hazards);
        this.pressurized = pressurized;
    }


    @Override
    public boolean isPressurized() {
        return pressurized;
    }


//    @Override
//    public String toString() {
//        return super.toString() + ", pressurized: " + isPressurized();
//    }
}
