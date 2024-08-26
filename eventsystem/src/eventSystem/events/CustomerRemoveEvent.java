package eventSystem.events;

import java.util.EventObject;

public class CustomerRemoveEvent extends EventObject {
    private final String customerName;

    public CustomerRemoveEvent(String customerName) {
        super(customerName);
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }
}
