package model;

import administration.Customer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import manager.CustomerImpl;

public class CustomerModel {
    private final Customer customer;
    private StringProperty name;
    private IntegerProperty counts;

    public CustomerModel(Customer customer) {
        this.customer = customer;
        this.setCustomerData();
    }

    private void setCustomerData() {
        this.name = new SimpleStringProperty(this.customer.name());
        if (this.customer instanceof CustomerImpl) {
            this.counts = new SimpleIntegerProperty(((CustomerImpl) this.customer).getCounts());
        }
    }

    public String getName() {
        return this.name.get();
    }

    public Integer getCounts() {
        return this.counts.get();
    }

    public StringProperty nameProperty() {
        return this.name;
    }

    public IntegerProperty countsProperty() {
        return this.counts;
    }
}
