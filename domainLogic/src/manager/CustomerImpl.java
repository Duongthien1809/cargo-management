package manager;

import administration.Customer;

import java.io.Serializable;

public class CustomerImpl implements Customer, Serializable {
    private final String name;
    private int counts = 0;

    public CustomerImpl(String name) {
        this.name = name;
    }

    public void increment() {
        this.counts++;
    }

    public void decrement() {
        if (counts > 0) {
            this.counts--;
        }
    }
    public int getCounts() {
        return counts;
    }

    @Override
    public String name() {
        return this.name;
    }
}
