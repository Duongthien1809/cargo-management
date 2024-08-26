package manager.Warehouse;

import administration.Customer;
import exception.DomainLogicException;
import manager.CustomerImpl;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CustomerWarehouseTest {
    @Test
    void addCustomerValidEntryTest() {
        //init customerWarehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        //test
        assertTrue(customerWarehouse.addCustomer("Test"));
    }

    @Test
    void addCustomerExistedEntryTest() {
        //init customerWarehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        //add Test to warehouse
        assertTrue(customerWarehouse.addCustomer("Test"));

        //try to add this name one more time
        assertFalse(customerWarehouse.addCustomer("Test"));
    }

    @Test
    void addCustomerEmptyEntryTest() {
        //init customerWarehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        //test
        assertThrows(DomainLogicException.class, () -> customerWarehouse.addCustomer(""));
    }

    @Test
    void addCustomerEntryIsNullTest() {
        //init customerWarehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        //test
        assertThrows(DomainLogicException.class, () -> customerWarehouse.addCustomer(null));
    }

    @Test
    void removeCustomerValidEntryTest() {
        //init customerWarehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        //Test customer is inserted to warehouse
        assertTrue(customerWarehouse.addCustomer("Test"));

        //remove Test from warehouse
        assertTrue(customerWarehouse.removeCustomer("Test"));
    }

    @Test
    void removeCustomerEmptyEntryTest() {
        //init customerWarehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();

        //remove empty from warehouse
        assertThrows(DomainLogicException.class, () -> {
            customerWarehouse.removeCustomer("");
            verify(customerWarehouse, never()).getCustomer(anyString());//getCustomer is never called
        });
    }

    @Test
    void removeCustomerEntryIsNullTest() {
        //init customerWarehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();

        //remove null from warehouse
        assertThrows(DomainLogicException.class, () -> {
            customerWarehouse.removeCustomer(null);
            verify(customerWarehouse, never()).getCustomer(anyString());//getCustomer is never called
        });
    }

    @Test
    void removeCustomerWarehouseIsEmptyTest() {
        //init customerWarehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();

        //remove Test from warehouse without add any customer
        assertFalse(customerWarehouse.removeCustomer("Test"));
    }

    @Test
    void getCustomerValidEntryTest() {
        //init customerWarehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        //add Test to warehouse
        assertTrue(customerWarehouse.addCustomer("Test"));
        // test get customer with name Test
        assertEquals(customerWarehouse.getCustomer("Test").name(), "Test");
    }

    @Test
    void getCustomerWithMultipleEntriesTest() {
        //init customerWarehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();

        //add multiple customers to the warehouse
        assertTrue(customerWarehouse.addCustomer("Test"));
        assertTrue(customerWarehouse.addCustomer("Test1"));
        assertTrue(customerWarehouse.addCustomer("Test2"));

        // test get customer with name Bob
        assertEquals(customerWarehouse.getCustomer("Test").name(), "Test");
    }

    @Test
    void getCustomerEmptyEntryTest() {
        //init customerWarehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();

        // test get customer with empty name
        assertThrows(DomainLogicException.class, () -> customerWarehouse.getCustomer(""));
    }

    @Test
    void getCustomerEntryIsNullTest() {
        //init customerWarehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();

        // test get customer with empty name
        assertThrows(DomainLogicException.class, () -> customerWarehouse.getCustomer(null));
    }

    @Test
    void getCustomerWhenCustomerNotFoundTest() {
        //init customerWarehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();

        // test get customer with empty name
        assertNull(customerWarehouse.getCustomer("Test"));
    }

    @Test
    void ListAllCustomersValidTest() {
        //init customerWarehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();

        //add to customers to warehouse
        assertTrue(customerWarehouse.addCustomer("Test1"));
        assertTrue(customerWarehouse.addCustomer("Test2"));
        //expect result
        // Erwartetes Ergebnis
        Set<Customer> expectedCustomers = Set.of(
                new CustomerImpl("Test1"),
                new CustomerImpl("Test2")
        );
        //test
        assertEquals(customerWarehouse.listAllCustomers().size(), expectedCustomers.size());
    }

    @Test
    void listAllCustomersEmptyWarehouseTest() {
        // init customerWarehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();

        // expect result
        Set<Customer> expectedCustomers = Set.of();

        // test
        assertEquals(expectedCustomers, customerWarehouse.listAllCustomers());
    }

    @Test
    void listAllCustomersModifyResultTest() {
        // Initialize the customer warehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();

        // Add customers to the warehouse
        customerWarehouse.addCustomer("Test1");
        customerWarehouse.addCustomer("Test2");

        // Call the method under test
        Set<Customer> actualCustomers = customerWarehouse.listAllCustomers();

        // Modifying the result should not affect the warehouse
        actualCustomers.add(new CustomerImpl("Test3"));

        // Check if the warehouse was not affected
        assertFalse(customerWarehouse.listAllCustomers().contains(new CustomerImpl("Test3")));
    }

    @Test
    void listAllCustomersIsNewSetTest() {
        // Initialize the customer warehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();

        // Add customers to the warehouse
        customerWarehouse.addCustomer("Test1");
        customerWarehouse.addCustomer("Test2");


        // Check if the returned set is a separate instance
        assertNotSame(customerWarehouse.listAllCustomers(), customerWarehouse.listAllCustomers());
    }

    @Test
    void isNameValidToAddValidNameTest() {
        // Initialize the customer warehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();

        // Add a customer to the warehouse
        customerWarehouse.addCustomer("Test");

        // Check if the name is considered valid
        assertTrue(customerWarehouse.isNameValidToAddCargo("Test"));
    }

    @Test
    void isNameValidToAddInvalidNameTest() {
        // Initialize the customer warehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();

        // Check if the name is considered invalid
        assertFalse(customerWarehouse.isNameValidToAddCargo("Test"));
    }

    @Test
    void isNameValidToAddEmptyNameTest() {
        // Initialize the customer warehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        assertThrows(DomainLogicException.class, () -> {
            customerWarehouse.isNameValidToAddCargo("");
            verify(customerWarehouse, never()).getCustomer(anyString());//getCustomer is never called
        });
    }

    @Test
    void isNameValidToAddNullNameTest() {
        // Initialize the customer warehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();

        assertThrows(DomainLogicException.class, () -> {
            customerWarehouse.removeCustomer(null);
            verify(customerWarehouse, never()).getCustomer(anyString());//getCustomer is never called
        });
    }

    @Test
    void isInstanceOfCustomerImplValidTrueTest() {
        // Initialize the customer warehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();

        Customer customer = mock(CustomerImpl.class);

        assertTrue(customerWarehouse.isInstanceOfCustomerImpl(customer));

    }

    @Test
    void isInstanceOfCustomerImplFalseTest() {
        // Initialize the customer warehouse
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();

        Customer customer = mock(Customer.class);

        assertFalse(customerWarehouse.isInstanceOfCustomerImpl(customer));
    }
}