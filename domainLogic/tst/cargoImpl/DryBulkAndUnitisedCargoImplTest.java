package cargoImpl;

import administration.Customer;
import cargo.DryBulkAndUnitisedCargo;
import cargo.Hazard;
import manager.CustomerImpl;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DryBulkAndUnitisedCargoImplTest {

    @Test
    void getGrainSize() {
        //init
        Customer customer = new CustomerImpl("Test");
        Collection<Hazard> hazards = Arrays.asList(Hazard.flammable, Hazard.toxic);
        DryBulkAndUnitisedCargo dryBulkAndUnitisedCargoTest = new DryBulkAndUnitisedCargoImpl("drybulkandunitisedcargo", customer, new BigDecimal(5), hazards, false, 10);
        //grainSize = 10
        assertEquals(dryBulkAndUnitisedCargoTest.getGrainSize(), 10);
    }


}