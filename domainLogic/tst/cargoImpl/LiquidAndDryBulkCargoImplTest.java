package cargoImpl;

import administration.Customer;
import cargo.Hazard;
import cargo.LiquidAndDryBulkCargo;
import manager.CustomerImpl;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertFalse;

class LiquidAndDryBulkCargoImplTest {

    @Test
    void isPressurized() {
        //init
        Customer customer = new CustomerImpl("Test");
        Collection<Hazard> hazards = Arrays.asList(Hazard.flammable, Hazard.toxic);
        LiquidAndDryBulkCargo liquidAndDryBulkCargoTest = new LiquidAndDryBulkCargoImpl("liquidanddrybulkcargo", customer, new BigDecimal(5), hazards, false, 10);

        assertFalse(liquidAndDryBulkCargoTest.isPressurized());
    }
}