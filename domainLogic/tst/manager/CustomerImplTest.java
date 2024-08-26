package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerImplTest {
    private CustomerImpl customer;

    @BeforeEach
    public void setUp() {
        // Initialize your instance before each test
        customer = new CustomerImpl("TestCustomer");
    }

    @Test
    public void testGetName() {
        assertEquals("TestCustomer", customer.name(), "Name should match");
    }

    @Test
    public void testIncrementAndGetCounts() {
        customer.increment();
        assertEquals(1, customer.getCounts(), "Counts should be one after incrementing");
    }

    @Test
    public void testMultipleIncrementAndGetCounts() {
        customer.increment();
        customer.increment();
        assertEquals(2, customer.getCounts(), "Counts should be two after incrementing multiple times");
    }

    @Test
    public void testDecrementAndGetCounts() {
        // Assuming you have a method to decrement counts, e.g., decrement()
        customer.increment(); // Counts = 1
        customer.decrement();
        assertEquals(0, customer.getCounts(), "Counts should be zero after incrementing and then decrementing");
    }

    @Test
    public void testDecrementBelowZero() {
        // Assuming you have a method to decrement counts, e.g., decrement()
        customer.decrement(); // Trying to decrement when counts is already zero
        assertEquals(0, customer.getCounts(), "Counts should remain zero");
    }

    @Test
    public void testIncrementDecrementBalance() {
        customer.increment(); // Counts = 1
        customer.decrement(); // Counts = 0
        customer.increment(); // Counts = 1 again
        assertEquals(1, customer.getCounts(), "Counts should be one after a series of increment and decrement");
    }

    @Test
    public void testDecrementBelowZeroMultiple() {
        customer.decrement(); // Trying to decrement when counts is already zero
        customer.decrement(); // Trying to decrement when counts is already zero again
        assertEquals(0, customer.getCounts(), "Counts should remain zero");
    }
}