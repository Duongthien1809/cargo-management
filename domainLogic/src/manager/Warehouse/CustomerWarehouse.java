package manager.Warehouse;

import administration.Customer;
import exception.DomainLogicException;
import manager.CustomerImpl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * inspire of: <a href="https://www.youtube.com/watch?v=vcV0DK45P-Q">...</a>
 */

public class CustomerWarehouse implements Serializable {
    private final Set<Customer> customers;

    public CustomerWarehouse() {
        this.customers = new HashSet<>();
    }

    public boolean addCustomer(String name) {
        validateEntry(name);
        if (!customerExists(name)) {
            Customer customer = new CustomerImpl(name);
            return customers.add(customer);
        }
        return false;
    }

    public boolean removeCustomer(String name) {
        validateEntry(name);

        Customer customer = getCustomer(name);
        return customerExists(name) && customers.remove(customer);
    }

    public Customer getCustomer(String name) {
        validateEntry(name);
        for (Customer customer : customers) {
            if (customer.name().equalsIgnoreCase(name)) {
                return customer;
            }
        }
        return null;
    }

    public Set<Customer> listAllCustomers() {
        return new HashSet<>(customers);
    }

    public boolean isNameValidToAddCargo(String name) {
        validateEntry(name);
        return getCustomer(name) != null;
    }

    private void validateEntry(String entry) {
        if (null == entry || entry.isEmpty()) {
            throw new DomainLogicException("Name couldn't be empty or null!");
        }
    }

    private boolean customerExists(String name) {
        return customers.stream().anyMatch(customer -> customer.name().equalsIgnoreCase(name));
    }

    //help klass vor test
    boolean isInstanceOfCustomerImpl(Customer customer) {
        return customer instanceof CustomerImpl;
    }
}