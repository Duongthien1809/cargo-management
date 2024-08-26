package cargoImpl;

import administration.Customer;
import cargo.Hazard;
import cargo.UnitisedCargo;
import manager.CustomerImpl;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertFalse;

class UnitisedCargoImplTest {

    @Test
    void isFragile() {
        //init
        Customer customer = new CustomerImpl("Test");
        Collection<Hazard> hazards = Arrays.asList(Hazard.flammable, Hazard.toxic);
        UnitisedCargo unitisedCargoTest = new UnitisedCargoImpl("unitisedcargo", customer, new BigDecimal(5), hazards, false);

        assertFalse(unitisedCargoTest.isFragile());
    }
}