package eventSystem.events;

import administration.Storable;
import cargo.Cargo;

import java.io.Serializable;
import java.util.EventObject;

public class CustomerCreateEvent extends EventObject implements Serializable {
    private final String customerName;

    public CustomerCreateEvent(String customerName) {
        super(customerName);
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }
}
