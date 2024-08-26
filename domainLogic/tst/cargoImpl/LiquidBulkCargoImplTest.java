package cargoImpl;

import administration.Customer;
import cargo.Hazard;
import cargo.LiquidBulkCargo;
import manager.CustomerImpl;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LiquidBulkCargoImplTest {

    @Test
    void isPressurized() {
        //init
        Customer customer = new CustomerImpl("Test");
        Collection<Hazard> hazards = Arrays.asList(Hazard.flammable, Hazard.toxic);
        LiquidBulkCargo liquidBulkCargoTest = new LiquidBulkCargoImpl("liquidbulkcargo", customer, new BigDecimal(5), hazards, true);

        assertTrue(liquidBulkCargoTest.isPressurized());
    }
}