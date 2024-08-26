package cargoImpl;

import administration.Customer;
import cargo.DryBulkCargo;
import cargo.Hazard;
import manager.CustomerImpl;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class DryBulkCargoImplTest {

    @Test
    void getGrainSize() {
        //init
        Customer customer = new CustomerImpl("Test");
        Collection<Hazard> hazards = Arrays.asList(Hazard.flammable, Hazard.toxic);
        DryBulkCargo dryBulkCargoTest = new DryBulkCargoImpl("drybulkcargo", customer, new BigDecimal(5), hazards, 10);

        assertEquals(dryBulkCargoTest.getGrainSize(), 10);
    }

    @Test
    void grainSizeNegative() {
        Collection<Hazard> hazards = Arrays.asList(Hazard.flammable, Hazard.toxic);
        assertThrows(IllegalArgumentException.class, () -> {
            new DryBulkCargoImpl("drybulkcargo", mock(Customer.class), new BigDecimal(5), hazards, -10);
        });
    }

}