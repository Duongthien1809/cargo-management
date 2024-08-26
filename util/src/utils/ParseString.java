package utils;

import administration.Customer;
import administration.Storable;
import cargo.*;
import manager.Warehouse.WarehouseFacade;

import java.io.Serializable;

public class ParseString<T extends Storable & Cargo> implements Serializable {

    public String printCargo(T cargo) {
        String res = cargo.getCargoType() + ", " + " customer name: " + cargo.getCustomer().name() + ", location: " + cargo.getStorageLocation() + ", value: " + cargo.getValue() + ", inspektion date: " + cargo.getLastInspectionDate() + ", Hazards: " + cargo.getHazards().toString() + ", duration: " + cargo.getDurationOfStorage();
        if (cargo instanceof DryBulkCargo)
            res += ", grain size: " + ((DryBulkCargo) cargo).getGrainSize();
        if (cargo instanceof UnitisedCargo)
            res += ", isFragile: " + ((UnitisedCargo) cargo).isFragile();
        if (cargo instanceof LiquidBulkCargo)
            res += ", isPressurized: " + ((LiquidBulkCargo) cargo).isPressurized();
        if (cargo instanceof DryBulkAndUnitisedCargo)
            res += ", grain size: " + ((DryBulkAndUnitisedCargo) cargo).getGrainSize() + ", isFragile: " + ((DryBulkAndUnitisedCargo) cargo).isFragile();
        if (cargo instanceof LiquidAndDryBulkCargo)
            res += ", isPressurized: " + ((LiquidAndDryBulkCargo) cargo).isPressurized() + ", grain size: " + ((LiquidAndDryBulkCargo) cargo).getGrainSize();
        if (cargo instanceof LiquidBulkAndUnitisedCargo)
            res += ", isPressurized: " + ((LiquidBulkAndUnitisedCargo) cargo).isPressurized() + ", isFragile: " + ((LiquidBulkAndUnitisedCargo) cargo).isFragile();
        return res;
    }

    public String getCustomerInfo(Customer customer, WarehouseFacade<T> warehouseFacade) {
        StringBuilder msg = new StringBuilder();
        msg.append("Customer ").append(customer.name()).append(" cargo count: ")
                .append(warehouseFacade.getCargoCountUsingCustomerName(customer.name()))
                .append("\n");
        return msg.toString();
    }
}
