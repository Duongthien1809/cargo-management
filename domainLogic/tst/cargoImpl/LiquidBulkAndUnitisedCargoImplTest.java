package cargoImpl;

import administration.Customer;
import cargo.Hazard;
import cargo.LiquidBulkAndUnitisedCargo;
import manager.CustomerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertFalse;

class LiquidBulkAndUnitisedCargoImplTest {
    @Test
    void isFragile() {
        //init
        Customer customer = new CustomerImpl("Test");
        Collection<Hazard> hazards = Arrays.asList(Hazard.flammable, Hazard.toxic);
        LiquidBulkAndUnitisedCargo liquidBulkAndUnitisedCargoTest = new LiquidBulkAndUnitisedCargoImpl("liquidbulkandunitisedcargo", customer, new BigDecimal(5), hazards, false, true);

        assertFalse(liquidBulkAndUnitisedCargoTest.isFragile());
    }
}